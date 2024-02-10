package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {

    private double salarioMensal;

    public EmpregadoAssalariado () {

    }

    public EmpregadoAssalariado(String nome, String endereco, double salarioMensal) {
        super(nome, endereco);
        this.salarioMensal = salarioMensal;
    }

    public double getSalario () {
        return salarioMensal;
    }

    public double getSalarioBruto() { return salarioMensal; }

    @Override
    public MembroSindicalizado getSindicalizado() {
        return super.getSindicalizado();
    }

    @Override
    public void setSalario(double salario) {
        this.salarioMensal = salario;
    }

    @Override
    public String getTipo() {
        return "assalariado";
    }

    @Override
    public MetodoPagamento getMetodoPagamento() {
        return super.getMetodoPagamento();
    }

    @Override
    public String getEndereco() {
        return super.getEndereco();
    }

}
