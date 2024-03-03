package br.ufal.ic.p2.wepayu.controller;

import br.ufal.ic.p2.wepayu.database.EmpregadoXML;
import br.ufal.ic.p2.wepayu.database.FolhaDePagamentoXML;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.TaxaServico;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Banco;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Correios;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.EmMaos;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.strategy.Contexto;
import br.ufal.ic.p2.wepayu.strategy.empregado.ContextoEmpregado;
import br.ufal.ic.p2.wepayu.strategy.empregadocomissionado.ContextoEmpregadoComissionado;
import br.ufal.ic.p2.wepayu.strategy.empregadohorista.ContextoEmpregadoHorista;
import br.ufal.ic.p2.wepayu.strategy.getatributo.GetAtributo;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FacadeController {

    private EmpregadoController empregadoController;
    private FolhaDePagamentoController folhaDePagamentoController;

    public FacadeController() {
        SystemController.systemStart();

        //Persistencia
        this.empregadoController = new EmpregadoController();
        EmpregadoXML xmlEmpregado = new EmpregadoXML();
        this.empregadoController.setEmpregados(xmlEmpregado.readEmpregados());

        FolhaDePagamentoXML xmlFolha = new FolhaDePagamentoXML();
        this.folhaDePagamentoController = new FolhaDePagamentoController(true);
        this.folhaDePagamentoController = xmlFolha.readFolha();
    }

    public void zerarSistema() throws Exception {
        SystemController.pushUndo(this.empregadoController);

        this.empregadoController = new EmpregadoController();
        this.folhaDePagamentoController = new FolhaDePagamentoController(true);

        Utils.deleteFilesXML();
        Utils.deleteFolhas();

        System.out.println("-> Sistema zerado");
    }

    public void encerrarSistema() {
        System.out.println("-> Sistema encerrado");

        //Salvar no xml
        EmpregadoXML xmlEmpregado = new EmpregadoXML();
        xmlEmpregado.save(this.empregadoController.getEmpregados());

        FolhaDePagamentoXML xmlFolha = new FolhaDePagamentoXML();
        xmlFolha.saveFolha(this.folhaDePagamentoController);

        //Encerrar o sistema
        SystemController.setSystemOn(false);
    }

    //4 variaveis
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {

        Contexto contexto = new Contexto();

        Empregado emp = contexto.criarEmpregado(new ContextoEmpregado(), nome, endereco, tipo, salario, empregadoController);
        return this.empregadoController.setEmpregado(emp);
    }

    //5 variaveis - Empregado Comissionado
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {

        Contexto contexto = new Contexto();

        Empregado emp = contexto.criarEmpregado(new ContextoEmpregado(), nome, endereco,
                tipo, salario, comissao, this.empregadoController);

        return this.empregadoController.setEmpregado(emp);
    }

    public void lancaCartao(String emp, String data, String horas) throws Exception {

        Contexto contexto = new Contexto();
        contexto.lancaCartao(new ContextoEmpregadoHorista(), emp, data, horas, this.empregadoController);
    }

    public void lancaVenda(String emp, String data, String valor) throws Exception {

        Contexto contexto = new Contexto();
        contexto.lancaVenda(new ContextoEmpregadoComissionado(), emp, data, valor, this.empregadoController);
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws Exception {

        Contexto contexto = new Contexto();
        contexto.lancaoTaxaDeServico(new ContextoEmpregado(), membro, data, valor,
                this.empregadoController);
    }

    public String getEmpregadoPorNome(String nome, int indice) {

        Contexto contexto = new Contexto();
        String id = contexto.getEmpregadoPorNome(new ContextoEmpregado(), nome, indice, this.empregadoController);

        return id;
    }

    public String getAtributoEmpregado(String emp, String atributo) {

        Contexto contexto = new Contexto();

        return contexto.getAtributo(new GetAtributo(), emp, atributo, this.empregadoController);
    }

    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) {

        Contexto contexto = new Contexto();

        String horas = contexto.getHorasNormaisTrabalhadas(new ContextoEmpregadoHorista(),
                emp, dataInicial, dataFinal, this.empregadoController);

        if (horas != null) {
            return horas;
        }

        return "0";
    }

    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal){
        Contexto contexto = new Contexto();

        String horas = contexto.getHorasExtrasTrabalhadas(new ContextoEmpregadoHorista(),
                emp, dataInicial, dataFinal, this.empregadoController);

        if (horas != null) {
            return horas;
        }

        return "0,00";
    }

    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws Exception {

        Contexto contexto = new Contexto();
        String vendasRealizadas = contexto.getVendasRealizadas(new ContextoEmpregadoComissionado(), emp,
                dataInicial, dataFinal, this.empregadoController);

        if (vendasRealizadas != null)
            return vendasRealizadas;

        return "0,00";
    }

    public String getTaxasServico(String emp, String dataInicial, String dataFinal) {

        Contexto contexto = new Contexto();

        String taxa = contexto.getTaxasServico(new ContextoEmpregado(), emp, dataInicial, dataFinal,
                this.empregadoController);

        if (taxa != null) {
            return taxa;
        }

        return "0,00";
    }

    // 3 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor) throws Exception {

        Contexto contexto = new Contexto();
        contexto.alteraEmpregado(new ContextoEmpregado(), emp, atributo, valor,
                this.empregadoController, this.folhaDePagamentoController);

    }

    // 4 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String sal) throws Exception {
        Contexto contexto = new Contexto();
        contexto.alteraEmpregado(new ContextoEmpregado(), emp, atributo, valor, sal,
                this.empregadoController);

    }

    // 5 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception {
        Contexto contexto = new Contexto();
        contexto.alteraEmpregado(new ContextoEmpregado(), emp, atributo, valor,
                idSindicato, taxaSindical, this.empregadoController);
    }

    // 6 variaveis
    public void alteraEmpregado(String emp, String atributo, String tipo, String banco, String agencia, String contaCorrente) throws Exception {
        Contexto contexto = new Contexto();
        contexto.alteraEmpregado(new ContextoEmpregado(), emp, atributo, tipo,
                banco, agencia, contaCorrente, this.empregadoController);
    }

    public void removerEmpregado(String emp) throws Exception {
        Contexto contexto = new Contexto();
        contexto.removerEmpregado(new ContextoEmpregado(), emp, this.empregadoController);
    }

    public String totalFolha(String data) throws Exception {
        return this.folhaDePagamentoController.totalFolha(data, this.empregadoController);
    }

    public void rodaFolha(String data, String saida) throws Exception {
        SystemController.pushUndo(this.empregadoController);
        this.folhaDePagamentoController.rodaFolha(data, saida, this.empregadoController);
    }

    public String getNumeroDeEmpregados() {
        int n = this.empregadoController.getNumeroDeEmpregados();

        return Integer.toString(n);
    }

    public void undo() throws Exception {
        this.empregadoController.setEmpregados(SystemController.popUndo());
    }

    public void redo() throws Exception {
        this.empregadoController.setEmpregados(SystemController.popRedo());
    }

    public void criarAgendaDePagamentos(String descricao) throws Exception {
        this.folhaDePagamentoController.criarAgendaDePagamentos(descricao);
    }

}
