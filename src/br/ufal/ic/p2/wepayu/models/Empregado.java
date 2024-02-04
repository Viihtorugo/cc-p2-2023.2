package br.ufal.ic.p2.wepayu.models;

public class Empregado {
    private String nome;
    private String endereco;
    private String tipo;
    private String salario;
    private String comissao;

    public Empregado(String nome, String endereco, String tipo, String salario){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
        this.comissao = "0";
    }
    public Empregado(String nome, String endereco, String tipo, String salario, String comissao){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
        this.comissao = comissao;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getSalario() { return this.salario; }

    public String getComissao() {
        return this.comissao;
    }

}
