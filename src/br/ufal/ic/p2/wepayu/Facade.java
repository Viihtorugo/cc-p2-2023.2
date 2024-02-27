package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.controller.FacadeController;

public class Facade {

    private FacadeController facedeController;

    public Facade() {
        this.facedeController = new FacadeController();
    }

    public void zerarSistema() throws Exception {
        this.facedeController.zerarSistema();
    }

    public void encerrarSistema() {
        this.facedeController.encerrarSistema();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        return this.facedeController.criarEmpregado(nome, endereco, tipo, salario);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        return this.facedeController.criarEmpregado(nome, endereco, tipo, salario, comissao);
    }

    public void lancaCartao(String emp, String data, String horas) throws Exception {
        this.facedeController.lancaCartao(emp, data, horas);
    }

    public void lancaVenda(String emp, String data, String valor) throws Exception {
        this.facedeController.lancaVenda(emp, data, valor);
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws Exception {
        this.facedeController.lancaTaxaServico(membro, data, valor);
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        return this.facedeController.getEmpregadoPorNome(nome, indice);
    }

    public String getAtributoEmpregado(String emp, String atributo) throws Exception {
        return this.facedeController.getAtributoEmpregado(emp, atributo);
    }

    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {
        return this.facedeController.getHorasNormaisTrabalhadas(emp, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {
        return this.facedeController.getHorasExtrasTrabalhadas(emp, dataInicial, dataFinal);
    }

    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws Exception {
        return this.facedeController.getVendasRealizadas(emp, dataInicial, dataFinal);
    }

    public String getTaxasServico(String emp, String dataInicial, String dataFinal) throws Exception {
        return this.facedeController.getTaxasServico(emp, dataInicial, dataFinal);
    }

    // 3 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor) throws Exception {
        this.facedeController.alteraEmpregado(emp, atributo, valor);
    }

    // 4 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String salario) throws Exception {
        this.facedeController.alteraEmpregado(emp, atributo, valor, salario);
    }

    // 5 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception {
        this.facedeController.alteraEmpregado(emp, atributo, valor, idSindicato, taxaSindical);
    }

    // 6 variaveis
    public void alteraEmpregado(String emp, String atributo, String tipo, String banco, String agencia, String contaCorrente) throws Exception {
        this.facedeController.alteraEmpregado(emp, atributo, tipo, banco, agencia, contaCorrente);
    }

    public void removerEmpregado(String emp) throws Exception {
        this.facedeController.removerEmpregado(emp);
    }

    public String totalFolha(String data) throws Exception {
        return this.facedeController.totalFolha(data);
    }

    public void rodaFolha(String data, String saida) throws Exception {
        this.facedeController.rodaFolha(data, saida);
    }

    public String getNumeroDeEmpregados() {
        return this.facedeController.getNumeroDeEmpregados();
    }

    public void undo() throws Exception {
        this.facedeController.undo();
    }

    public void redo() throws Exception {
        this.facedeController.redo();
    }
}
