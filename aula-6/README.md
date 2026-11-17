# Aula 6 — Estruturas dinâmicas (módulo de Estoque no MVC)

Projeto Spring Boot + Thymeleaf + H2 (`mvn spring-boot:run`, porta 8080).
Telas: `/products` (cadastro) e `/estoque` (entrada, saída, descontinuar).

Cada diagrama responde uma pergunta diferente (arquivos `.mermaid` em Mermaid):

| Diagrama | Pergunta | Arquivo |
|---|---|---|
| Sequência | Quem chama quem, e em que ordem? | `diagrama-sequencia-estoque.mermaid` |
| Atividades | Quais passos o processo executa? | `diagrama-atividade-saida-estoque.mermaid` |
| Estados | Em que estado o objeto está e como muda? | `diagrama-estado-produto.mermaid` |

## Diagrama de sequência — entrada e saída de estoque

```mermaid
sequenceDiagram
    actor TimeEstoque as Time de Estoque
    participant EstoqueController
    participant EstoqueService
    participant ProductRepository

    Note over TimeEstoque, ProductRepository: Rotina de Entrada de Estoque
    TimeEstoque->>EstoqueController: POST /estoque/entrada (productId, qtd)
    EstoqueController->>EstoqueService: registrarEntrada(productId, qtd)
    EstoqueService->>ProductRepository: findById(productId)
    ProductRepository-->>EstoqueService: Product
    EstoqueService->>EstoqueService: darEntrada(qtd) [se saiu de 0 -> DISPONIVEL]
    EstoqueService->>ProductRepository: save(product)
    ProductRepository-->>EstoqueService: Product atualizado
    EstoqueService-->>EstoqueController: Product atualizado
    EstoqueController-->>TimeEstoque: redirect /estoque

    Note over TimeEstoque, ProductRepository: Rotina de Saída de Estoque
    TimeEstoque->>EstoqueController: POST /estoque/saida (productId, qtd)
    EstoqueController->>EstoqueService: registrarSaida(productId, qtd)
    EstoqueService->>ProductRepository: findById(productId)
    ProductRepository-->>EstoqueService: Product
    EstoqueService->>EstoqueService: darSaida(qtd) [se zerou -> INDISPONIVEL]
    EstoqueService->>ProductRepository: save(product)
    ProductRepository-->>EstoqueService: Product atualizado
    EstoqueService-->>EstoqueController: Product atualizado
    EstoqueController-->>TimeEstoque: redirect /estoque
```

## Diagrama de atividades — saída de estoque

```mermaid
flowchart TD
    Inicio(( )) --> A[Recebe solicitação de saída de estoque]
    A --> B{Produto está DESCONTINUADO?}
    B -- [sim] --> C[Recusa operação: produto descontinuado]
    B -- [não] --> D{Status é DISPONÍVEL e estoque >= qtd?}
    D -- [não] --> E[Recusa operação: sem estoque disponível]
    D -- [sim] --> F[Subtrai quantidade do estoque]
    F --> G{Estoque zerou?}
    G -- [sim] --> H[Altera status do produto para INDISPONIVEL]
    G -- [não] --> I[Mantém status como DISPONIVEL]
    H --> J[Salva produto atualizado]
    I --> J
    C --> Fim(( ))
    E --> Fim
    J --> Fim
```

## Diagrama de estados — Product

```mermaid
stateDiagram-v2
    [*] --> INDISPONIVEL : criarProduto() [estoque == 0]
    INDISPONIVEL --> DISPONIVEL : darEntrada() [estoque > 0]
    DISPONIVEL --> INDISPONIVEL : darSaida() [estoque == 0]
    DISPONIVEL --> DESCONTINUADO : descontinuar() [isAdmin == true]
    INDISPONIVEL --> DESCONTINUADO : descontinuar() [isAdmin == true]
    DESCONTINUADO --> [*]
```

## Correspondência diagrama → código

- Estados e transições: `model/Product.java` (`darEntrada`, `darSaida`, `descontinuar`) e `ProductStatus`.
- Fluxo da sequência: `EstoqueController` → `EstoqueService` → `ProductRepository`.
- Decisões do diagrama de atividades: validações em `Product.darSaida`.


