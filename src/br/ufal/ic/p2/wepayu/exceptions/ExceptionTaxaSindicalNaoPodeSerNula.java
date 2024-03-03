package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionTaxaSindicalNaoPodeSerNula extends RuntimeException{
    public ExceptionTaxaSindicalNaoPodeSerNula () {
        super("Taxa sindical nao pode ser nula.");
    }
}
