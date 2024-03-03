package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionValorDeveSerNumerico extends RuntimeException{
    public ExceptionValorDeveSerNumerico() {
        super("Valor deve ser numerico.");
    }
}
