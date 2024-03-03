package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionTaxaSindicalDeveSerNaoNegativa extends RuntimeException {
    public ExceptionTaxaSindicalDeveSerNaoNegativa () {
        super("Taxa sindical deve ser nao-negativa.");
    }
}
