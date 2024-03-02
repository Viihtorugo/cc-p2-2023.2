package br.ufal.ic.p2.wepayu.strategy.getatributo;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;

public interface StrategyGetAtributo {
    String executeGetAtributo(String emp, String atributo, EmpregadoController empregadoController);
}
