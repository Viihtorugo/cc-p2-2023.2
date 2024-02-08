package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {

    private double salarioMensal;

    public EmpregadoAssalariado(String nome, String endereco, double salarioMensal) {
        super(nome, endereco);
        this.salarioMensal = salarioMensal;
    }

    public double getSalarioMensal() {
        return salarioMensal;
    }


    @Override
    public void setSalario(double salario) {
        this.salarioMensal = salario;
    }

    public double getSalario () {
        return salarioMensal;
    }

    @Override
    public String getTipo() {
        return "assalariado";
    }
}
