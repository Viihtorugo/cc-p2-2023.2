package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.TaxaServico;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.EmMaos;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.MetodoPagamento;

public abstract class Empregado{
    private String nome;
    private String endereco;
    private String agendaDePagamento;
    private MembroSindicalizado sindicalizado;
    private MetodoPagamento metodoPagamento;

    public Empregado () {

    }

    public Empregado(String nome, String endereco, String agendaDePagamento) {
        this.nome = nome;
        this.endereco = endereco;
        this.agendaDePagamento = agendaDePagamento;
        this.sindicalizado = null;
        this.metodoPagamento = new EmMaos();
    }

    public String getAgendaDePagamento() {
        return agendaDePagamento;
    }

    public void setAgendaDePagamento(String agendaDePagamento) {
        this.agendaDePagamento = agendaDePagamento;
    }

    public void setSindicalizado(MembroSindicalizado sindicalizado) {
        this.sindicalizado = sindicalizado;
    }

    public MembroSindicalizado getSindicalizado() {
        return this.sindicalizado;
    }

    public void addTaxaServico (TaxaServico taxaServico) {
        this.sindicalizado.addTaxaServico(taxaServico);
    }

    public String getNome() {
        return this.nome;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public abstract double getSalario();

    public abstract void setSalario(double salario);

    public abstract String getTipo();


}
