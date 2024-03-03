package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionHorasDevemSerPositivas extends RuntimeException{
    public ExceptionHorasDevemSerPositivas() {
        super("Horas devem ser positivas.");
    }
}
