package br.edu.up.hub.domain.model;

/** Falha de uma porta de saída (fila, SOAP, SMTP, banco), nas palavras do negócio. */
public class EmissorIndisponivelException extends RuntimeException {

    public EmissorIndisponivelException(String emissor, Throwable causa) {
        super("Emissor indisponível: " + emissor, causa);
    }
}

