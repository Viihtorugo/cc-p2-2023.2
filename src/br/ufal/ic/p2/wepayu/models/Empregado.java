package br.ufal.ic.p2.wepayu.models;

public abstract class Empregado {
    private String nome;
    private String endereco;

    private MembroSindicalizado sindicalizado;

    public Empregado(String nome, String endereco){
        this.nome = nome;
        this.endereco = endereco;
        sindicalizado = null;
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

    public abstract String getSalario();

    public abstract void setSalario(String salario);

    public abstract String getTipo();

}
