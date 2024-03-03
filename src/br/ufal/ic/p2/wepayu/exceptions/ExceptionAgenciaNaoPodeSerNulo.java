package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionAgenciaNaoPodeSerNulo extends RuntimeException{
    public ExceptionAgenciaNaoPodeSerNulo() {
        super("Agencia nao pode ser nulo.");
    }
}
