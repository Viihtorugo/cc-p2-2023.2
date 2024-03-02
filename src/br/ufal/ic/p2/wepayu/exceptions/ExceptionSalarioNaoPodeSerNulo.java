package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionSalarioNaoPodeSerNulo extends RuntimeException{
    public ExceptionSalarioNaoPodeSerNulo() {
        super("Salario nao pode ser nulo.");
    }
}
