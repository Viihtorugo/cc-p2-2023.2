package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionValorDeveSerTrueOuFalse extends RuntimeException{
    public ExceptionValorDeveSerTrueOuFalse() {
        super("Valor deve ser true ou false.");
    }
}
