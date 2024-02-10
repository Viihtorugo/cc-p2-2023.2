package br.ufal.ic.p2.wepayu.models;

public class Correios extends MetodoPagamento {

    public Correios() {

    }

    @Override
    public String getMetodoPagamento() {
        return "correios";
    }

    @Override
    public String getOutputFile() {
        return "Correios";
    }
}
