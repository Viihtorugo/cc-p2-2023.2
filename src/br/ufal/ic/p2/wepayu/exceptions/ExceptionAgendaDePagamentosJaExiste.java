package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionAgendaDePagamentosJaExiste extends RuntimeException{
    public ExceptionAgendaDePagamentosJaExiste () {
        super("Agenda de pagamentos ja existe");
    }
}
