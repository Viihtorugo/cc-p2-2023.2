package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionContaCorrenteNaoPodeSerNulo extends RuntimeException {
    public ExceptionContaCorrenteNaoPodeSerNulo() {
        super("Conta corrente nao pode ser nulo.");
    }
}
