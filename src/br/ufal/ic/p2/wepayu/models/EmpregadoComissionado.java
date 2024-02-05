package br.ufal.ic.p2.wepayu.models;

public class EmpregadoComissionado extends Empregado {

    private String taxaDeComissao;
    private String salarioMensal;

    public EmpregadoComissionado(String nome, String endereco, String salarioMensal, String taxaDeComissao) {
        super(nome, endereco);
        this.taxaDeComissao = taxaDeComissao;
        this.salarioMensal = salarioMensal;
    }

    public String getSalarioMensal() {
        return salarioMensal;
    }

    public String getTaxaDeComissao() {
        return taxaDeComissao;
    }
    @Override
    public String getSalario () {
        return salarioMensal;
    }


    public String getTipo() {
        return "comissionado";
    }
}
