package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionIdentificacaoDoSindicatoNaoPodeSerNula extends RuntimeException{
    public ExceptionIdentificacaoDoSindicatoNaoPodeSerNula () {
        super("Identificacao do sindicato nao pode ser nula.");
    }
}
