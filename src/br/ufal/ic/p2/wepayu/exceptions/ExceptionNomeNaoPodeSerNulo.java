package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionNomeNaoPodeSerNulo extends RuntimeException{
    public ExceptionNomeNaoPodeSerNulo() {
        super("Nome nao pode ser nulo.");
    }

}
