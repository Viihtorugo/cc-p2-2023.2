package br.ufal.ic.p2.wepayu.strategy;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.strategy.criarempregado.StrategyCriarEmpregado;
import br.ufal.ic.p2.wepayu.strategy.getatributo.StrategyGetAtributo;

public class Contexto {

    public Empregado criarEmpregado(StrategyCriarEmpregado strategy, String nome, String endereco, String tipo, String salario) throws Exception {
        try {
            return strategy.executeCriarEmpregado(nome, endereco, tipo, salario);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Empregado criarEmpregado (StrategyCriarEmpregado strategy, String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        try {
            return strategy.executeCriarEmpregado(nome, endereco, tipo, salario, comissao);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String getAtributo(StrategyGetAtributo strategyGetAtributo, String emp, String atributo, EmpregadoController empregadoController) throws Exception {
        try {
            return strategyGetAtributo.executeGetAtributo(emp, atributo, empregadoController);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
