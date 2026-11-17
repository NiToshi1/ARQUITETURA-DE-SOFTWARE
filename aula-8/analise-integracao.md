# Análise da Integração Distribuída: Vendas (MVC) -> Hub de Faturamento

Quando a aplicação de **Vendas (MVC)** passa a chamar o **Hub de Faturamento** pela rede via REST síncrono (`POST /api/faturas`), a comunicação deixa de ser uma chamada local em memória e fica sujeita às falhas inerentes aos sistemas distribuídos.

---

## Problema 1 — Atraso ou Indisponibilidade do Hub (Falha em Cascata)

### O que acontece com o pedido
Como a chamada REST é síncrona, a thread da aplicação de Vendas fica bloqueada aguardando o Hub executar todas as etapas internas (persistir fatura, publicar boleto na fila, emitir NF-e via SOAP e enviar e-mail via SMTP). Se o emissor externo de NF-e (SOAP) demorar 30 segundos ou o Hub estiver fora do ar, a requisição estoura erro de conexão/socket, a transação local de fechamento é abortada e o pedido permanece em estado `ABERTO` sem `faturaId`.

### O que o negócio percebe
O vendedor ou o cliente final vê a tela travar no momento de finalizar a compra e recebe uma mensagem de erro. Mesmo com o módulo de Vendas saudável e com estoque disponível, a operação comercial para completamente (**falha em cascata**), causando perda de vendas e insatisfação no atendimento.

### Solução indicada e onde atuaria
* **Curto prazo (na chamada síncrona): Timeout + Circuit Breaker** atuando no **`FaturamentoHubClient` dentro da aplicação de Vendas (MVC)**. O *Timeout* limita o tempo máximo de espera (ex.: 2 segundos) para evitar o esgotamento de threads, enquanto o *Circuit Breaker* abre o circuito após falhas consecutivas, falhando imediatamente sem sobrecarregar o Hub instável.
* **Solução estrutural: Comunicação Assíncrona via Fila/Broker (Orientada a Eventos)** entre **Vendas e o Hub de Faturamento**. Ao fechar o pedido, Vendas publica o evento `PedidoFechado` no broker (ex.: RabbitMQ) e conclui o fechamento imediatamente; o Hub consome a mensagem no seu próprio ritmo e devolve o `faturaId` posteriormente via evento `FaturaCriada`.

---

## Problema 2 — Repetição de Requisição / Duplicidade de Faturamento (Falta de Idempotência)

### O que acontece com o pedido
Imagine que o Hub recebe a requisição de fechamento, cria a fatura, emite o boleto e a NF-e, mas a rede oscila no exato instante de devolver a resposta HTTP 200 com o `faturaId` para Vendas. Para a aplicação de Vendas, ocorreu uma falha de rede/timeout e o pedido continua `ABERTO` e sem `faturaId`. Ao tentar fechar o pedido novamente (retentativa automática ou novo clique do vendedor), Vendas envia o mesmo `pedidoId` uma segunda vez, fazendo o Hub gerar uma **segunda fatura**, um segundo boleto e uma segunda NF-e para o mesmo pedido.

### O que o negócio percebe
O cliente recebe dois boletos de cobrança e duas notas fiscais distintas referentes a uma única compra, gerando reclamações, necessidade de estorno financeiro, cancelamento fiscal junto à SEFAZ e inconsistência contábil entre Vendas e Faturamento.

### Solução indicada e onde atuaria
* **Idempotência** atuando diretamente no **Hub de Faturamento (`FaturamentoService` / `FaturaRepositoryPort`)**. O Hub passa a manter um registro indexado pelo `pedidoId` (chave de idempotência). Ao receber um `POST /api/faturas`, o Hub verifica antes de processar se aquele `pedidoId` já foi faturado:
  * Se **não foi**, executa o faturamento normalmente e salva o vínculo `pedidoId -> faturaId`.
  * Se **já foi**, ignora a reexecução de boleto/NF-e/e-mail e devolve imediatamente o `faturaId` já existente, garantindo que o pedido em Vendas receba o identificador correto sem duplicar efeitos no negócio.
