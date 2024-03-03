package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionDataInvalida extends RuntimeException{
    public ExceptionDataInvalida (String tipo) {
        super("Data" + tipo + "invalida.");
    }
}
