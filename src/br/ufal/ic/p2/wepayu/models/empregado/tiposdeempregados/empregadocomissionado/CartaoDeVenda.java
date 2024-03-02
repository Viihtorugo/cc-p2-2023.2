package br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado;

import java.lang.String;

public class CartaoDeVenda {
    private String data;
    private Double valor;

    public CartaoDeVenda () {

    }

    public CartaoDeVenda (String data, Double valor) {
        this.data = data;
        this.valor = valor;
    }

    public Double getValor() {
        return this.valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
