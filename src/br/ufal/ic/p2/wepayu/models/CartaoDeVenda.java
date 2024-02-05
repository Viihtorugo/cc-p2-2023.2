package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;

public class CartaoDeVenda {
    private LocalDate data;
    private Double horas;

    public CartaoDeVenda (LocalDate data, Double horas) {
        this.data = data;
        this.horas = horas;
    }

    public Double getHoras() {
        return horas;
    }

    public LocalDate getData() {
        return data;
    }
}
