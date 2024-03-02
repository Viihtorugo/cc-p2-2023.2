package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionEmpregadoNaoExiste extends RuntimeException{
    public ExceptionEmpregadoNaoExiste () {
        super("Empregado nao existe.");
    }
}
