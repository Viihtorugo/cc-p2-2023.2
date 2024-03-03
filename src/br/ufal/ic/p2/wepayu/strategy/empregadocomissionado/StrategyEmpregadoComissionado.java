package br.ufal.ic.p2.wepayu.strategy.empregadocomissionado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;

public interface StrategyEmpregadoComissionado {

    String getVendasRealizadas(String emp, String dataInicial, String dataFinal,
                               EmpregadoController empregadoController);
    void executeLancaVenda(String emp, String data, String valor, EmpregadoController empregadoController);
}
