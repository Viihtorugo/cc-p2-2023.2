package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;

public class EmpregadoComissionado extends Empregado {

    private double taxaDeComissao;
    private double salarioMensal;
    private ArrayList<CartaoDeVenda> vendas;

    public EmpregadoComissionado() {

    }

    public EmpregadoComissionado(String nome, String endereco, String agendaDePagamento, double salarioMensal, double taxaDeComissao) {
        super(nome, endereco, agendaDePagamento);
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

    public void addVenda(String data, double valor) {
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

            LocalDate dataFormato = Utils.validData(c.getData(), "");

            if (dataFormato != null) {
                if (dataFormato.isEqual(dataInicial) ||
                        (dataFormato.isAfter(dataInicial) && dataFormato.isBefore(dataFinal))) {
                    vendasRealizadas += c.getValor();
                }
            }
        }

        return vendasRealizadas;
    }

    @Override
    public void setSalario(double salario) {
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

    @Override
    public MetodoPagamento getMetodoPagamento() {
        return super.getMetodoPagamento();
    }

    @Override
    public String getEndereco() {
        return super.getEndereco();
    }

    @Override
    public MembroSindicalizado getSindicalizado() {
        return super.getSindicalizado();
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    public ArrayList<CartaoDeVenda> getVendas() {
        return this.vendas;
    }

    public double getSalarioBruto(LocalDate dataInicial, LocalDate dataCriacao) throws Exception {
        double comissao = this.getVendasRealizadas(dataInicial, dataCriacao) * this.taxaDeComissao;
        comissao = Math.floor(comissao*100)/100F;

        double salarioFixo = getSalario();

        if (super.getAgendaDePagamento().equals("semanal 2 5")) {
            salarioFixo = Math.floor((salarioFixo*12D/52D)*2D * 100)/100F;
        } else if (super.getAgendaDePagamento().equals("semanal 5")) {
            salarioFixo = Math.floor((salarioFixo*12D/52D) * 100)/100F;
        }

        return comissao + salarioFixo;
    }

    @Override
    public void setEndereco(String endereco) {
        super.setEndereco(endereco);
    }

    @Override
    public void setSindicalizado(MembroSindicalizado sindicalizado) {
        super.setSindicalizado(sindicalizado);
    }

    @Override
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        super.setMetodoPagamento(metodoPagamento);
    }

    public void setVendas(ArrayList<CartaoDeVenda> vendas) {
        this.vendas = vendas;
    }
}
