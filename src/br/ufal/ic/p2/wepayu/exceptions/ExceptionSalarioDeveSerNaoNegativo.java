package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionSalarioDeveSerNaoNegativo extends RuntimeException {
    public ExceptionSalarioDeveSerNaoNegativo(){
        throw new RuntimeException("Salario deve ser nao-negativo.");
    }
}
