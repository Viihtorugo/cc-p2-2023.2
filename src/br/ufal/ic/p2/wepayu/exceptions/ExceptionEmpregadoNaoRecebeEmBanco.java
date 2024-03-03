package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionEmpregadoNaoRecebeEmBanco extends RuntimeException{
    public ExceptionEmpregadoNaoRecebeEmBanco() {
        super("Empregado nao recebe em banco.");
    }
}
