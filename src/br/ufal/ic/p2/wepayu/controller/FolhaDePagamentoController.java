package br.ufal.ic.p2.wepayu.controller;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionFolhaDePagamento;
import br.ufal.ic.p2.wepayu.models.folhadepagamento.FolhaDePagamento;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;

public class FolhaDePagamentoController {

    private ArrayList <String> agendaDePagamentoList;

    public FolhaDePagamentoController () {

    }

    public FolhaDePagamentoController (boolean systemOn) {
        this.agendaDePagamentoList = new ArrayList<>();

        if (systemOn) {
            this.agendaDePagamentoList.add("semanal 5");
            this.agendaDePagamentoList.add("semanal 2 5");
            this.agendaDePagamentoList.add("mensal $");
        }
    }

    public ArrayList<String> getAgendaDePagamentoList() {
        return agendaDePagamentoList;
    }

    public void setAgendaDePagamentoList(ArrayList<String> agendaDePagamentoList) {
        this.agendaDePagamentoList = agendaDePagamentoList;
    }

    public boolean verificarAgendaDePagamento(String descricao) throws Exception {

        for (String s: this.agendaDePagamentoList) {
            if (s.equals(descricao)) {
                ExceptionFolhaDePagamento e = new ExceptionFolhaDePagamento();
                e.msgAgendaDePagamentosJaExiste();
                return false;
            }
        }

        return true;
    }

    public void criarAgendaDePagamentos (String descricao) throws Exception {
        if (this.verificarAgendaDePagamento(descricao) && Utils.validCriarAgendaDePagamentos(descricao))
            this.agendaDePagamentoList.add(descricao);
    }


    public String totalFolha(String data, EmpregadoController empregadoController) throws Exception {

        LocalDate dataFormato = Utils.validData(data, "");

        FolhaDePagamento folha = new FolhaDePagamento(dataFormato);

        double total = folha.totalFolha(empregadoController);

        return Utils.convertDoubleToString(total, 2);
    }

    public void rodaFolha(String data, String saida, EmpregadoController empregadoController) throws Exception {
        LocalDate dataFormato = Utils.validData(data, "");

        FolhaDePagamento folha = new FolhaDePagamento(dataFormato, saida);

        folha.geraFolha(empregadoController);
    }
}
