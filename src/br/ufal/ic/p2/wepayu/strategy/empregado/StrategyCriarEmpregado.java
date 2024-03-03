package br.ufal.ic.p2.wepayu.strategy.empregado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

public interface StrategyCriarEmpregado {

    Empregado executeCriarEmpregado(String nome, String endereco, String tipo, String salario,
                                    EmpregadoController empregadoController) throws Exception;
    Empregado executeCriarEmpregado(String nome, String endereco, String tipo, String salario,
                                    String comissao, EmpregadoController empregadoController);

}
