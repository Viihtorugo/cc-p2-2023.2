package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionValorDeveSerPositivo extends RuntimeException {
    public ExceptionValorDeveSerPositivo() {
        super("Valor deve ser positivo.");
    }
}
