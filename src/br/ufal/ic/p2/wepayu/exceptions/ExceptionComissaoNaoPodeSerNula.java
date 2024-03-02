package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionComissaoNaoPodeSerNula extends RuntimeException{
    public ExceptionComissaoNaoPodeSerNula() {
        super("Comissao nao pode ser nula.");
    }
}
