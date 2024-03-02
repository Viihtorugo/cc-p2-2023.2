package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionTipoNaoAplicavel extends RuntimeException{
    public ExceptionTipoNaoAplicavel() {
        super("Tipo nao aplicavel.");
    }
}
