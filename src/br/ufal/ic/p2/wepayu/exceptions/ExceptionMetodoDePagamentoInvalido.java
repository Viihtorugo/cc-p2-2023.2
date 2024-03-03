package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionMetodoDePagamentoInvalido extends RuntimeException{
    public ExceptionMetodoDePagamentoInvalido () {
        super("Metodo de pagamento invalido.");
    }
}
