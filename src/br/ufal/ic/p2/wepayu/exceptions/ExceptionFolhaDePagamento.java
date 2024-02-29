package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionFolhaDePagamento extends Exception{
    public void msgAgendaDePagamentosJaExiste () throws Exception{
        throw new Exception("Agenda de pagamentos ja existe");
    }
}
