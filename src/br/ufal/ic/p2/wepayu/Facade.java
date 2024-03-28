package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.controller.SystemController;
import br.ufal.ic.p2.wepayu.utils.StateSystem;

public class Facade {

    private SystemController systemController;

    public Facade() {
        this.systemController = SystemController.getInstance();
        StateSystem.systemStart();
    }

    public void zerarSistema() throws Exception {
        this.systemController.zerarSistema();
    }

    public void encerrarSistema() {
        this.systemController.encerrarSistema();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        return this.systemController.criarEmpregado(nome, endereco, tipo, salario);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        return this.systemController.criarEmpregado(nome, endereco, tipo, salario, comissao);
    }

    public void lancaCartao(String emp, String data, String horas) throws Exception {
        this.systemController.lancaCartao(emp, data, horas);
    }

    public void lancaVenda(String emp, String data, String valor) throws Exception {
        this.systemController.lancaVenda(emp, data, valor);
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws Exception {
        this.systemController.lancaTaxaServico(membro, data, valor);
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        return this.systemController.getEmpregadoPorNome(nome, indice);
    }

    public String getAtributoEmpregado(String emp, String atributo) throws Exception {
        return this.systemController.getAtributoEmpregado(emp, atributo);
    }

    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {
        return this.systemController.getHorasNormaisTrabalhadas(emp, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {
        return this.systemController.getHorasExtrasTrabalhadas(emp, dataInicial, dataFinal);
    }

    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws Exception {
        return this.systemController.getVendasRealizadas(emp, dataInicial, dataFinal);
    }

    public String getTaxasServico(String emp, String dataInicial, String dataFinal) throws Exception {
        return this.systemController.getTaxasServico(emp, dataInicial, dataFinal);
    }

    // 3 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor) throws Exception {
        this.systemController.alteraEmpregado(emp, atributo, valor);
    }

    // 4 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String salario) throws Exception {
        this.systemController.alteraEmpregado(emp, atributo, valor, salario);
    }

    // 5 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception {
        this.systemController.alteraEmpregado(emp, atributo, valor, idSindicato, taxaSindical);
    }

    // 6 variaveis
    public void alteraEmpregado(String emp, String atributo, String tipo, String banco, String agencia, String contaCorrente) throws Exception {
        this.systemController.alteraEmpregado(emp, atributo, tipo, banco, agencia, contaCorrente);
    }

    public void removerEmpregado(String emp) throws Exception {
        this.systemController.removerEmpregado(emp);
    }

    public String totalFolha(String data) throws Exception {
        return this.systemController.totalFolha(data);
    }

    public void rodaFolha(String data, String saida) throws Exception {
        this.systemController.rodaFolha(data, saida);
    }

    public String getNumeroDeEmpregados() {
        return this.systemController.getNumeroDeEmpregados();
    }

    public void undo() throws Exception {
        this.systemController.undo();
    }

    public void redo() throws Exception {
        this.systemController.redo();
    }

    public void criarAgendaDePagamentos(String descricao) throws Exception {
        this.systemController.criarAgendaDePagamentos(descricao);
    }
}
