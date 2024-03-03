package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionBancoNaoPodeSerNulo extends RuntimeException{
    public ExceptionBancoNaoPodeSerNulo() {
        super("Banco nao pode ser nulo.");
    }
}
