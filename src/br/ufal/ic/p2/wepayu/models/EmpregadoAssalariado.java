package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {

    private String salarioMensal;

    public EmpregadoAssalariado(String nome, String endereco, String salarioMensal) {
        super(nome, endereco);
        this.salarioMensal = salarioMensal;
    }

    public String getSalarioMensal() {
        return salarioMensal;
    }

    public String getSalario () {
        return salarioMensal;
    }

    @Override
    public String getTipo() {
        return "assalariado";
    }
}
