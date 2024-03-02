package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionComissaoDeveSerNaoNegativa extends RuntimeException{
    public ExceptionComissaoDeveSerNaoNegativa() {
        super("Comissao deve ser nao-negativa.");
    }
}
