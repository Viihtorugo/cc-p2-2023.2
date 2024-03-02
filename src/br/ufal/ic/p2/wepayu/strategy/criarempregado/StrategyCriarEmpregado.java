package br.ufal.ic.p2.wepayu.strategy.criarempregado;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

public interface StrategyCriarEmpregado {

    Empregado executeCriarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception;
    Empregado executeCriarEmpregado(String nome, String endereco, String tipo, String salario, String comissao);

}
