package br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado;

import java.lang.String;

public class TaxaServico {
    private String data;
    private double valor;

    public TaxaServico () {

    }

    public TaxaServico (String data, double valor) {
        this.data = data;
        this.valor = valor;
    }

    public String getData() {
        return this.data;
    }

    public double getValor() {
        return this.valor;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
