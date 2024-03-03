package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionAgendaDePagamentoNaoEstaDisponivel extends RuntimeException {
    public ExceptionAgendaDePagamentoNaoEstaDisponivel () {
        super("Agenda de pagamento nao esta disponivel");
    }
}
