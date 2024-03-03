package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionHorasDeveSerNumerico extends RuntimeException{
    public ExceptionHorasDeveSerNumerico() {
        super("Horas deve ser numerico.");
    }
}
