package br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados;

import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

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

        String [] pagamento = super.getAgendaDePagamento().split(" ");

        if (pagamento[0].equals("semanal")) {
            if (pagamento.length == 3) {
                int week = Integer.parseInt(pagamento[1]);

                return Math.floor((this.salarioMensal * 12D / 52D) * week * 100) / 100F;
            } else {
                return Math.floor((this.salarioMensal*12D/52D) * 100)/100F;
            }
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
