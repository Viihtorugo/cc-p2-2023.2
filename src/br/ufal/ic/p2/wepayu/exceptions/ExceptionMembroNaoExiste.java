package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionMembroNaoExiste extends RuntimeException{
    public ExceptionMembroNaoExiste () {
        super("Membro nao existe.");
    }
}
