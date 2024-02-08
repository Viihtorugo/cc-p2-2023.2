package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;

import java.time.LocalDate;
import java.util.ArrayList;

public class MembroSindicalizado {
    private String idMembro;
    private double taxaSindical;
    private ArrayList<TaxaServico> taxaServicos;

    public MembroSindicalizado(String idMembro, double taxaSindical) {
        this.idMembro = idMembro;
        this.taxaSindical = taxaSindical;
        this.taxaServicos = new ArrayList<>();
    }

    public double getTaxaServicos(LocalDate dataInicial, LocalDate dataFinal) throws Exception {

        double countTaxas = 0;

        if (dataInicial.isAfter(dataFinal)) {
            ExceptionEmpregado e = new ExceptionEmpregado();
            e.msgDataInicialPosteriorDataFinal();

            return countTaxas;
        }

        if (dataInicial.isEqual(dataFinal)) {
            return countTaxas;
        }

        for (TaxaServico t : this.taxaServicos) {
            if (t.getData().isEqual(dataInicial) ||
                    (t.getData().isAfter(dataInicial) && t.getData().isBefore(dataFinal))) {
                countTaxas += t.getValor();
            }
        }

        return countTaxas;
    }

    public double getTaxaSindical() {
        return this.taxaSindical;
    }

    public String getIdMembro() {
        return this.idMembro;
    }

    public void addTaxaServico (TaxaServico taxaServico) {
        this.taxaServicos.add(taxaServico);
    }
}
