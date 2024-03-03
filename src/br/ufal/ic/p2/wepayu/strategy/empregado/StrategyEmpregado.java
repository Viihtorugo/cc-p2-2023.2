package br.ufal.ic.p2.wepayu.strategy.empregado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.FolhaDePagamentoController;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

public interface StrategyEmpregado {

    Empregado executeCriarEmpregado(String nome, String endereco, String tipo, String salario,
                                    EmpregadoController empregadoController);
    Empregado executeCriarEmpregado(String nome, String endereco, String tipo, String salario,
                                    String comissao, EmpregadoController empregadoController);
    void executeRemoveEmpregado(String emp, EmpregadoController empregadoController);
    String getEmpregadoPorNome(String nome, int indice,
                                      EmpregadoController empregadoController);
    String getTaxasServico(String emp, String dataInicial, String dataFinal,
                                  EmpregadoController empregadoController);
    void executeLancaTaxaServico(String membro, String data, String valor,
                                 EmpregadoController empregadoController);
    void executeAlteraEmpregado(String emp, String atributo, String valor,
                                EmpregadoController empregadoController,
                                FolhaDePagamentoController folhaDePagamentoController);

    void executeAlteraEmpregado(String emp, String atributo, String valor,
                                String sal, EmpregadoController empregadoController);

    void executeAlteraEmpregado(String emp, String atributo, String valor,
                                String idSindicato, String taxaSindical,
                                EmpregadoController empregadoController);

    void executeAlteraEmpregado(String emp, String atributo, String tipo,
                                String banco, String agencia, String contaCorrente,
                                EmpregadoController empregadoController);
    }
