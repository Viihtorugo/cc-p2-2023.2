package br.ufal.ic.p2.wepayu.strategy;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.strategy.criarempregado.StrategyCriarEmpregado;
import br.ufal.ic.p2.wepayu.strategy.getatributo.StrategyGetAtributo;
import br.ufal.ic.p2.wepayu.strategy.removerempregado.StrategyRemoverEmpregado;

public class Contexto {

    public Empregado criarEmpregado(StrategyCriarEmpregado strategy, String nome, String endereco,
                                    String tipo, String salario,
                                    EmpregadoController empregadoController) throws Exception {
        try {
            return strategy.executeCriarEmpregado(nome, endereco, tipo, salario, empregadoController);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Empregado criarEmpregado (StrategyCriarEmpregado strategy, String nome, String endereco,
                                     String tipo, String salario, String comissao,
                                     EmpregadoController empregadoController) throws Exception {
        try {
            return strategy.executeCriarEmpregado(nome, endereco, tipo, salario, comissao, empregadoController);
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

    public void removerEmpregado(StrategyRemoverEmpregado strategyRemoverEmpregado, String emp, EmpregadoController empregadoController) throws Exception {
        try {
            strategyRemoverEmpregado.executeRemoveEmpregado(emp, empregadoController);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
