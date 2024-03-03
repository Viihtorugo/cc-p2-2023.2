package br.ufal.ic.p2.wepayu.strategy;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.strategy.empregado.StrategyEmpregado;
import br.ufal.ic.p2.wepayu.strategy.empregadocomissionado.StrategyEmpregadoComissionado;
import br.ufal.ic.p2.wepayu.strategy.empregadohorista.StrategyEmpregadoHorista;
import br.ufal.ic.p2.wepayu.strategy.getatributo.StrategyGetAtributo;

public class Contexto {

    public Empregado criarEmpregado(StrategyEmpregado strategyEmpregado, String nome, String endereco,
                                    String tipo, String salario,
                                    EmpregadoController empregadoController) {
        try {
            return strategyEmpregado.executeCriarEmpregado(nome, endereco, tipo, salario, empregadoController);
        } catch (Exception e) {
            throw e;
        }
    }

    public Empregado criarEmpregado (StrategyEmpregado strategyEmpregado, String nome, String endereco,
                                     String tipo, String salario, String comissao,
                                     EmpregadoController empregadoController) {
        try {
            return strategyEmpregado.executeCriarEmpregado(nome, endereco, tipo, salario, comissao, empregadoController);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmpregadoPorNome(StrategyEmpregado strategyEmpregado, String nome, int indice,
                                      EmpregadoController empregadoController) {
        try {
            return strategyEmpregado.getEmpregadoPorNome(nome, indice, empregadoController);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getAtributo(StrategyGetAtributo strategyGetAtributo, String emp,
                              String atributo, EmpregadoController empregadoController) {
        try {
            return strategyGetAtributo.executeGetAtributo(emp, atributo, empregadoController);
        } catch (Exception e) {
            throw e;
        }
    }

    public void removerEmpregado(StrategyEmpregado strategyEmpregado,
                                 String emp, EmpregadoController empregadoController) {
        try {
            strategyEmpregado.executeRemoveEmpregado(emp, empregadoController);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getHorasNormaisTrabalhadas(StrategyEmpregadoHorista strategyEmpregadoHorista,
                                             String emp, String dataInicial, String dataFinal,
                                             EmpregadoController empregadoController)  {
        try {
            return strategyEmpregadoHorista.getHorasTrabalhadas(emp,
                    dataInicial, dataFinal, empregadoController, true);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getHorasExtrasTrabalhadas(StrategyEmpregadoHorista strategyEmpregadoHorista,
                                             String emp, String dataInicial, String dataFinal,
                                             EmpregadoController empregadoController) {
        try {
            return strategyEmpregadoHorista.getHorasTrabalhadas(emp,
                    dataInicial, dataFinal, empregadoController, false);
        } catch (Exception e) {
            throw e;
        }
    }

    public void lancaCartao(StrategyEmpregadoHorista strategyEmpregadoHorista,
                                             String emp, String data, String horas,
                                             EmpregadoController empregadoController) {
        try {
            strategyEmpregadoHorista.executeLancaCartao(emp, data, horas, empregadoController);
        } catch (Exception e) {
            throw e;
        }
    }

    public void lancaVenda(StrategyEmpregadoComissionado strategyEmpregadoComissionado,
                           String emp, String data, String valor,
                           EmpregadoController empregadoController) {
        try {
            strategyEmpregadoComissionado.executeLancaVenda(emp, data, valor, empregadoController);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getVendasRealizadas(StrategyEmpregadoComissionado strategyEmpregadoComissionado,
                                      String emp, String dataInicial, String dataFinal,
                                      EmpregadoController empregadoController) {
        try {
            return strategyEmpregadoComissionado.getVendasRealizadas(emp, dataInicial, dataFinal,
                    empregadoController);
        } catch (Exception e) {
            throw e;
        }
    }
}
