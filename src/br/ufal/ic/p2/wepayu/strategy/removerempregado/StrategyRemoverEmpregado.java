package br.ufal.ic.p2.wepayu.strategy.removerempregado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;

public interface StrategyRemoverEmpregado {
     void executeRemoveEmpregado(String emp, EmpregadoController empregadoController);
}
