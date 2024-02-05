package br.ufal.ic.p2.wepayu.models;

public abstract class Empregado {
    private String nome;
    private String endereco;

    public Empregado(String nome, String endereco){
        this.nome = nome;
        this.endereco = endereco;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public abstract String getSalario();

    public abstract String getTipo();

}
