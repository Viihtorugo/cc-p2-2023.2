package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionNaoHaEmpregadoComEsseNome extends RuntimeException{
    public ExceptionNaoHaEmpregadoComEsseNome () {
        super("Nao ha empregado com esse nome.");
    }
}
