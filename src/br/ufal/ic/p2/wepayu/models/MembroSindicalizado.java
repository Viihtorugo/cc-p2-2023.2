package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

public class MembroSindicalizado {
    private String idMembro;
    private double taxaSindical;
    private ArrayList<TaxaServico> taxaServicos;

    public MembroSindicalizado(String idMembro, double taxaSindical) {
        this.idMembro = idMembro;
        this.taxaSindical = taxaSindical;
        this.taxaServicos = new ArrayList<TaxaServico>();
    }


    public ArrayList<TaxaServico> getTaxaServicos() {
        return this.taxaServicos;
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
