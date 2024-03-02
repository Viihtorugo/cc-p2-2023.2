package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionEmpregadoNaoEhSindicalizado extends RuntimeException {
    public ExceptionEmpregadoNaoEhSindicalizado () {
        super("Empregado nao eh sindicalizado.");
    }
}
