package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionIdentificacaoDoEmpregadoNaoPodeSerNula extends RuntimeException{
    public ExceptionIdentificacaoDoEmpregadoNaoPodeSerNula() {
        super("Identificacao do empregado nao pode ser nula.");
    }
}
