package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionEmpregadoNaoEhTipo extends RuntimeException{

    public ExceptionEmpregadoNaoEhTipo(String tipo) {
        super("Empregado nao eh " + tipo + ".");
    }
}
