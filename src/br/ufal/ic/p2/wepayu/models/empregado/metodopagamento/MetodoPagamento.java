package br.ufal.ic.p2.wepayu.models.empregado.metodopagamento;

public abstract class MetodoPagamento {

    public MetodoPagamento () {

    }

    public abstract String getMetodoPagamento();

    public abstract String getOutputFile();
}
