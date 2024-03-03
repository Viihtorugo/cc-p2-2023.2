package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionIdentificacaoDoMembroNaoPodeSerNula extends RuntimeException{
    public ExceptionIdentificacaoDoMembroNaoPodeSerNula() {
        super("Identificacao do membro nao pode ser nula.");
    }
}
