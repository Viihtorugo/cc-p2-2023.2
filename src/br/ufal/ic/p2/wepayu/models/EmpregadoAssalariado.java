package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {

    private double salarioMensal;

    public EmpregadoAssalariado () {

    }

    public EmpregadoAssalariado(String nome, String endereco, String agendaDePagamento, double salarioMensal) {
        super(nome, endereco, agendaDePagamento);
        this.salarioMensal = salarioMensal;
    }

    public double getSalario () {
        return salarioMensal;
    }

    public double getSalarioBruto() {
        if (super.getAgendaDePagamento().equals("semanal 2 5")) {
            return Math.floor((this.salarioMensal*12D/52D)*2D * 100)/100F;
        } else if (super.getAgendaDePagamento().equals("semanal 5")) {
            return Math.floor((this.salarioMensal*12D/52D) * 100)/100F;
        } else {
            return this.salarioMensal;
        }
    }

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
