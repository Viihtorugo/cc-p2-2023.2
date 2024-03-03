package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionValorNaoPodeSerNulo extends RuntimeException{
    public ExceptionValorNaoPodeSerNulo () {
        super("Valor nao pode ser nulo.");
    }
}
