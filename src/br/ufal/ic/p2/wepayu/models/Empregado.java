package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exceptions.EmpregadoNaoExisteException;

public class Empregado {
    private String nome;
    private String endereco;
    private String tipo;
    private int salario;

    public Empregado(String nome, String endereco, String tipo, int salario){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
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

    public int getSalario() {
        return this.salario;
    }

}
