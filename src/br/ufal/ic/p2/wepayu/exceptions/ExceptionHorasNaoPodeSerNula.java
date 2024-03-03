package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionHorasNaoPodeSerNula extends RuntimeException{
    public ExceptionHorasNaoPodeSerNula () {
        super("Horas nao pode ser nula.");
    }
}
