package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionHaOutroEmpregadoComEstaIdentificacaoDeSindicato extends RuntimeException{
    public ExceptionHaOutroEmpregadoComEstaIdentificacaoDeSindicato () {
        super("Ha outro empregado com esta identificacao de sindicato");
    }
}
