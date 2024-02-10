package br.ufal.ic.p2.wepayu.models;

public class EmMaos extends MetodoPagamento {

    public EmMaos () {

    }

    @Override
    public String getMetodoPagamento() {
        return "emMaos";
    }

    @Override
    public String getOutputFile() {
        return "Em maos";
    }
}
