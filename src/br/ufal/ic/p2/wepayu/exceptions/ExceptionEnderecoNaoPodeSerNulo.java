package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionEnderecoNaoPodeSerNulo extends RuntimeException{
    public ExceptionEnderecoNaoPodeSerNulo(){
        super("Endereco nao pode ser nulo.");
    }

}
