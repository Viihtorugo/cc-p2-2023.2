package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionAtributoNaoExiste extends RuntimeException{
    public ExceptionAtributoNaoExiste () {
        super("Atributo nao existe.");
    }
}
