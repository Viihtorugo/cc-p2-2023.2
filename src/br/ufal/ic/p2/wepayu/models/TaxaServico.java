package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;

public class TaxaServico {
    private LocalDate data;
    private double valor;

    public TaxaServico (LocalDate data, double valor) {
        this.data = data;
        this.valor = valor;
    }

    public LocalDate getData() {
        return this.data;
    }

    public double getValor() {
        return this.valor;
    }
}
