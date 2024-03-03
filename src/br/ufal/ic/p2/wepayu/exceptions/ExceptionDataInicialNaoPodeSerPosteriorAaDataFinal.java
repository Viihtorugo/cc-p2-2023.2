package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionDataInicialNaoPodeSerPosteriorAaDataFinal extends RuntimeException{
    public ExceptionDataInicialNaoPodeSerPosteriorAaDataFinal() {
        super("Data inicial nao pode ser posterior aa data final.");
    }
}
