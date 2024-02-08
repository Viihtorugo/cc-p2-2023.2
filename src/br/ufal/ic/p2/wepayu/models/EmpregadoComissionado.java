package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;

import java.time.LocalDate;
import java.util.ArrayList;

public class EmpregadoComissionado extends Empregado {

    private double taxaDeComissao;
    private double salarioMensal;
    private ArrayList<CartaoDeVenda> vendas;

    public EmpregadoComissionado(String nome, String endereco, double salarioMensal, double taxaDeComissao) {
        super(nome, endereco);
        this.taxaDeComissao = taxaDeComissao;
        this.salarioMensal = salarioMensal;
        this.vendas = new ArrayList<CartaoDeVenda>();
    }

    public double getTaxaDeComissao() {
        return taxaDeComissao;
    }

    public void setTaxaDeComissao(double taxaDeComissao) {
        this.taxaDeComissao = taxaDeComissao;
    }

    public void addVenda(LocalDate data, double valor) {
        this.vendas.add(new CartaoDeVenda(data, valor));
    }

    public double getVendasRealizadas(LocalDate dataInicial, LocalDate dataFinal) throws Exception {

        double vendasRealizadas = 0;

        if (dataInicial.isEqual(dataFinal))
            return vendasRealizadas;

        if (dataInicial.isAfter(dataFinal)) {
            ExceptionEmpregado e = new ExceptionEmpregado();
            e.msgDataInicialPosteriorDataFinal();

            return vendasRealizadas;
        }

        for (CartaoDeVenda c : this.vendas) {
            if (c.getData().isEqual(dataInicial) ||
                    (c.getData().isAfter(dataInicial) && c.getData().isBefore(dataFinal))) {
                vendasRealizadas += c.getHoras();
            }
        }

        return vendasRealizadas;
    }

    @Override
    public void setSalario (double salario) {
        this.salarioMensal = salario;
    }

    @Override
    public double getSalario() {
        return salarioMensal;
    }

    @Override
    public String getTipo() {
        return "comissionado";
    }
}
