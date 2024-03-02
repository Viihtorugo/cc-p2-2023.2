package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionSalarioDeveSerNumerico extends RuntimeException {

    public ExceptionSalarioDeveSerNumerico() {
        throw new RuntimeException("Salario deve ser numerico.");
    }
}
