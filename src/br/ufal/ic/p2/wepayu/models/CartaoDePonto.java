package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;

public class CartaoDePonto {

    private LocalDate data;
    private Double horas;

    public CartaoDePonto (LocalDate data, Double horas) {
        this.data = data;
        this.horas = horas;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Double getHoras() {
        return this.horas;
    }
}
