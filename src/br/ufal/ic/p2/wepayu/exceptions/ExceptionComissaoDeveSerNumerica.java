package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionComissaoDeveSerNumerica extends RuntimeException{
    public ExceptionComissaoDeveSerNumerica() {
        super("Comissao deve ser numerica.");
    }
}
