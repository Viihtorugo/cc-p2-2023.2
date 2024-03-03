package br.ufal.ic.p2.wepayu.strategy.empregadohorista;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;

public interface StrategyEmpregadoHorista {
    String getHorasTrabalhadas(String emp, String dataInicial, String dataFinal,
                               EmpregadoController empregadoController, boolean normais);

    void executeLancaCartao(String emp, String data, String horas,
                              EmpregadoController empregadoController);
}
