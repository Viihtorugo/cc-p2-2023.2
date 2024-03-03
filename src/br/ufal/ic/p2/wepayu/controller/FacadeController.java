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
    private boolean systemOn;

    public FacadeController() {
        this.systemOn = true;
        this.empregadoController = new EmpregadoController();
        EmpregadoXML xmlEmpregado = new EmpregadoXML();
        this.empregadoController.setEmpregados(xmlEmpregado.readEmpregados());
        FolhaDePagamentoXML xmlFolha = new FolhaDePagamentoXML();
        this.folhaDePagamentoController = xmlFolha.readFolha();
        this.folhaDePagamentoController = new FolhaDePagamentoController(this.systemOn);
    }

    public void zerarSistema() throws Exception {
        SystemController.pushUndo(this.empregadoController);
        this.empregadoController = new EmpregadoController();
        this.folhaDePagamentoController = new FolhaDePagamentoController(this.systemOn);
        Utils.deleteFilesXML();
        Utils.deleteFolhas();
        System.out.println("-> Sistema zerado");
    }

    public void encerrarSistema() {
        System.out.println("-> Sistema encerrado");

        EmpregadoXML xmlEmpregado = new EmpregadoXML();
        xmlEmpregado.save(this.empregadoController.getEmpregados());
        FolhaDePagamentoXML xmlFolha = new FolhaDePagamentoXML();
        xmlFolha.saveFolha(this.folhaDePagamentoController);
        this.systemOn = false;
    }

    //4 variaveis
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        Contexto contexto = new Contexto();

        Empregado emp = contexto.criarEmpregado(new ContextoEmpregado(), nome, endereco, tipo, salario, empregadoController);

        return this.empregadoController.setEmpregado(emp);
    }

    //5 variaveis - Empregado Comissionado
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        Contexto contexto = new Contexto();

        Empregado emp = contexto.criarEmpregado(new ContextoEmpregado(), nome, endereco,
                tipo, salario, comissao, this.empregadoController);

        return this.empregadoController.setEmpregado(emp);
    }

    public void lancaCartao(String emp, String data, String horas) throws Exception {
        Contexto contexto = new Contexto();
        contexto.lancaCartao(new ContextoEmpregadoHorista(), emp, data, horas, this.empregadoController);
    }

    public void lancaVenda(String emp, String data, String valor) {

        Contexto contexto = new Contexto();
        contexto.lancaVenda(new ContextoEmpregadoComissionado(), emp, data, valor, this.empregadoController);
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        String id = Utils.validMembroSindicalizadoPeloID(membro, this.empregadoController);

        if (id == null)
            return;

        double valorFormato = Utils.validValor(valor);
        LocalDate dataFormato = Utils.validData(data, " ");

        if (valorFormato <= 0 || dataFormato == null)
            return;

        this.empregadoController.getEmpregado(id).addTaxaServico(new TaxaServico(data, valorFormato));
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

    public String getTaxasServico(String emp, String dataInicial, String dataFinal) throws Exception {

        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null)
            return "0,00";

        LocalDate dataInicialFormato = Utils.validData(dataInicial, " inicial ");

        if (dataInicialFormato == null)
            return "0,00";

        LocalDate dataFinalFormato = Utils.validData(dataFinal, " final ");

        if (dataFinalFormato == null)
            return "0,00";

        MembroSindicalizado m = Utils.validMembroSindicalizado(e);

        if (m == null) {
            return "0,00";
        }

        double taxa = m.getTaxaServicos(dataInicialFormato, dataFinalFormato);

        return Utils.convertDoubleToString(taxa, 2);
    }

    // 3 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null)
            return;

        if (!Utils.validGetAtributo(emp, atributo)) {
            return;
        }

        switch (atributo) {
            case "nome" -> {
                if (Utils.validNome(valor))
                    e.setNome(valor);
            }
            case "endereco" -> {
                if (Utils.validEndereco(valor))
                    e.setEndereco(valor);
            }
            case "agendaPagamento" -> {
                if (Utils.validAgendaPagamento(valor, this.folhaDePagamentoController.getAgendaDePagamentoList()))
                    e.setAgendaDePagamento(valor);
            }
            case "salario" -> {
                double salario = Utils.validSalario(valor);

                if (salario < 0)
                    return;

                e.setSalario(salario);
            }
            case "sindicalizado" -> {
                if (Utils.validSindicalizado(valor)) {
                    e.setSindicalizado(null);
                }
            }
            case "comissao" -> {

                double comissao = Utils.validComissao(valor);

                if (comissao < 0)
                    return;

                if (!Utils.empregadoIsNotComissionado(e))
                    return;

                ((EmpregadoComissionado) e).setTaxaDeComissao(comissao);
            }
            case "tipo" -> {

                if (Utils.validAlterarTipo(e, valor)) {
                    return;
                }

                String nome = e.getNome();
                String endereco = e.getEndereco();
                double salario = e.getSalario();

                switch (valor) {
                    case "horista" ->
                            this.empregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, "semanal 5", salario));
                    case "assalariado" ->
                            this.empregadoController.setValue(emp, new EmpregadoAssalariado(nome, endereco, "mensal $", salario));
                    case "comissionado" ->
                            this.empregadoController.setValue(emp, new EmpregadoComissionado(nome, endereco, "semanal 2 5", salario, 0));
                }
            }

            case "metodoPagamento" -> {

                if (!Utils.validMetodoPagamento(valor))
                    return;

                if (valor.equals("correios")) e.setMetodoPagamento(new Correios());

                else if (valor.equals("emMaos")) e.setMetodoPagamento(new EmMaos());

            }
        }
    }

    // 4 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String sal) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null) {
            return;
        }


        if (!Utils.validGetAtributo(emp, atributo))
            return;

        String nome = e.getNome();
        String endereco = e.getEndereco();

        if (valor.equals("comissionado")) {
            double salario = e.getSalario();
            double comissao = Utils.validComissao(sal);

            if (comissao < 0)
                return;

            this.empregadoController.setValue(emp, new EmpregadoComissionado(nome, endereco, "semanal 2 5", salario, comissao));

        } else if (valor.equals("horista")) {
            double novoSalario = Utils.validSalario(sal);

            if (novoSalario < 0)
                return;

            this.empregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, "semanal 5", novoSalario));
        }
    }

    // 5 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        if (Utils.validIdSindical(idSindicato))
            return;

        double taxaSindicalNumber = Utils.validTaxaSindical(taxaSindical);

        if (taxaSindicalNumber <= 0.0)
            return;

        if (atributo.equals("sindicalizado") && valor.equals("true")) {

            if (Utils.sindicalizarEmpregado(idSindicato, this.empregadoController)) {
                Empregado e = Utils.validEmpregado(emp, this.empregadoController);

                if (e == null) return;

                e.setSindicalizado(new MembroSindicalizado(idSindicato, taxaSindicalNumber));
            }
        }
    }

    // 6 variaveis
    public void alteraEmpregado(String emp, String atributo, String tipo, String banco, String agencia, String contaCorrente) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null)
            return;

        if (!Utils.validGetAtributo(emp, atributo))
            return;

        if (Utils.validBanco(banco, agencia, contaCorrente))
            e.setMetodoPagamento(new Banco(banco, agencia, contaCorrente));
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
