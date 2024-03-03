package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionConversao extends Exception {

    public void msgDescricaoDeAgendaInvalida () throws Exception{
        throw new Exception("Descricao de agenda invalida");
    }

    public void msgDataInvalida (String tipo) throws Exception {
        throw new Exception("Data" + tipo + "invalida.");
    }

}
