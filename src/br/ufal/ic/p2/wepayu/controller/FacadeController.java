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
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.strategy.Contexto;
import br.ufal.ic.p2.wepayu.strategy.criarempregado.CriarEmpregado;
import br.ufal.ic.p2.wepayu.strategy.getatributo.GetAtributo;
import br.ufal.ic.p2.wepayu.strategy.removerempregado.RemoverEmpregado;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FacadeController {

    private EmpregadoController empregadoController;
    private FolhaDePagamentoController folhaDePagamentoController;

    public FacadeController () {
        this.empregadoController = new EmpregadoController();
        this.folhaDePagamentoController = new FolhaDePagamentoController(SystemController.getSystemOn());
        EmpregadoXML xmlEmpregado = new EmpregadoXML();
        this.empregadoController.setEmpregados(xmlEmpregado.readEmpregados());
        FolhaDePagamentoXML xmlFolha = new FolhaDePagamentoXML();
        this.folhaDePagamentoController = xmlFolha.readFolha();
    }
    public void zerarSistema() throws Exception {
        SystemController.pushUndo(this.empregadoController);
        this.empregadoController = new EmpregadoController();
        this.folhaDePagamentoController = new FolhaDePagamentoController(SystemController.getSystemOn());
        Utils.deleteFilesXML();
        Utils.deleteFolhas();
        System.out.println("-> Sistema zerado");
    }

    public void encerrarSistema () {
        System.out.println("-> Sistema encerrado");

        EmpregadoXML xmlEmpregado = new EmpregadoXML();
        xmlEmpregado.save(this.empregadoController.getEmpregados());
        FolhaDePagamentoXML xmlFolha = new FolhaDePagamentoXML();
        xmlFolha.saveFolha(this.folhaDePagamentoController);
        SystemController.systemOff();
    }

    //4 variaveis
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        Contexto contexto = new Contexto();

        Empregado emp = contexto.criarEmpregado(new CriarEmpregado(), nome, endereco, tipo, salario, empregadoController);

        return this.empregadoController.setEmpregado(emp);
    }

    //5 variaveis - Empregado Comissionado
    public String criarEmpregado (String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        Contexto contexto = new Contexto();

        Empregado emp = contexto.criarEmpregado(new CriarEmpregado(), nome, endereco,
                                                tipo, salario, comissao, this.empregadoController);

        return this.empregadoController.setEmpregado(emp);
    }

    public void lancaCartao(String emp, String data, String horas) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null)
            return;

        if (Utils.validTipoEmpregado(e, "horista")) {
            double horasFormato = Utils.validHoras(horas);
            LocalDate dataFormato = Utils.validData(data, " ");

            if (horasFormato <= 0 || dataFormato == null)
                return;

            ((EmpregadoHorista) e).addRegistro(data, horasFormato);
        }
    }

    public void lancaVenda(String emp, String data, String valor) throws Exception {

        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null) {
            return;
        }

        if (Utils.validTipoEmpregado(e, "comissionado")) {
            double valorFormato = Utils.validValor(valor);
            LocalDate dataFormato = Utils.validData(data, " ");

            if (valorFormato <= 0 || dataFormato == null) {
                return;
            }

            SystemController.pushUndo(this.empregadoController);
            ((EmpregadoComissionado) e).addVenda(data, valorFormato);
        }
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

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {

        int count = 0;

        HashMap<String, Empregado> empregados = this.empregadoController.getEmpregados();

        for (Map.Entry<String, Empregado> entry : empregados.entrySet()) {

            Empregado e = entry.getValue();

            if (nome.contains(e.getNome()))
                count++;

            if (count == indice)
                return entry.getKey();
        }

        ExceptionEmpregado ex = new ExceptionEmpregado();

        ex.msgEmpregadoNaoExistePorNome();

        return null;
    }

    public String getAtributoEmpregado(String emp, String atributo) throws Exception {
//        Empregado e = Utils.validEmpregado(emp, this.empregadoController);
//
//        if (e == null)
//            return null;
//
//        if (!Utils.validGetAtributo(emp, atributo))
//            return null;
//
//        switch (atributo) {
//
//            case "nome" -> {
//                return e.getNome();
//            }
//            case "agendaPagamento" -> {
//                return e.getAgendaDePagamento();
//            }
//            case "tipo" -> {
//                return e.getTipo();
//            }
//            case "salario" -> {
//                return Utils.convertDoubleToString(e.getSalario(), 2);
//            }
//            case "endereco" -> {
//                return e.getEndereco();
//            }
//            case "comissao" -> {
//
//                if (Utils.empregadoIsNotComissionado(e))
//                    return Utils.convertDoubleToString(((EmpregadoComissionado) e).getTaxaDeComissao(), 2);
//
//                return null;
//            }
//
//            case "metodoPagamento" -> {
//                return e.getMetodoPagamento().getMetodoPagamento();
//            }
//
//            case "banco", "agencia", "contaCorrente" -> {
//                MetodoPagamento metodoPagamento = e.getMetodoPagamento();
//
//                if (!Utils.metodoPagamentoIsBanco(metodoPagamento))
//                    return null;
//
//                if (atributo.equals("banco")) return ((Banco) metodoPagamento).getBanco();
//                if (atributo.equals("agencia")) return ((Banco) metodoPagamento).getAgencia();
//
//                return ((Banco) metodoPagamento).getContaCorrente();
//            }
//
//            case "sindicalizado" -> {
//                if (e.getSindicalizado() == null) return "false";
//                else return "true";
//            }
//
//            case "idSindicato", "taxaSindical" -> {
//                MembroSindicalizado ms = e.getSindicalizado();
//
//                if (!Utils.validSindicato(ms))
//                    return null;
//
//                if (atributo.equals("idSindicato")) return ms.getIdMembro();
//
//                return Utils.convertDoubleToString(e.getSindicalizado().getTaxaSindical(), 2);
//            }
//
//        }

        Contexto contexto = new Contexto();

        return contexto.getAtributo(new GetAtributo(), emp, atributo, this.empregadoController);
    }

    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {
        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null) return "0";

        if (Utils.validTipoEmpregado(e, "horista")) {

            LocalDate dataFinalFormato = Utils.validData(dataFinal, " final ");

            if (dataFinalFormato == null)
                return "0";

            LocalDate dataInicialFormato = Utils.validData(dataInicial, " inicial ");

            if (dataInicialFormato == null)
                return "0";

            double horasTrabalhadas = ((EmpregadoHorista) e).getHorasNormaisTrabalhadas(dataInicialFormato, dataFinalFormato);

            return Utils.convertDoubleToString(horasTrabalhadas);
        }

        return "0";
    }

    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {
        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null) return null;

        if (Utils.validTipoEmpregado(e, "horista")) {

            LocalDate dataInicialFormato = Utils.validData(dataInicial, " inicial ");

            if (dataInicialFormato == null)
                return "0,00";

            LocalDate dataFinalFormato = Utils.validData(dataFinal, " final ");

            if (dataFinalFormato == null)
                return "0,00";

            double horasAcumuladas = ((EmpregadoHorista) e).getHorasExtrasTrabalhadas(dataInicialFormato, dataFinalFormato);

            return Utils.convertDoubleToString(horasAcumuladas);
        }

        return "0,00";
    }

    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws Exception {
        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null) return null;

        if (Utils.validTipoEmpregado(e, "comissionado")) {

            LocalDate dataInicialFormato = Utils.validData(dataInicial, " inicial ");

            if (dataInicialFormato == null)
                return "0,00";

            LocalDate dataFinalFormato = Utils.validData(dataFinal, " final ");

            if (dataFinalFormato == null)
                return "0,00";

            double vendas = ((EmpregadoComissionado) e).getVendasRealizadas(dataInicialFormato, dataFinalFormato);

            return Utils.convertDoubleToString(vendas, 2);
        }

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
                    case "horista" -> this.empregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, "semanal 5", salario));
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
        contexto.removerEmpregado(new RemoverEmpregado(), emp, this.empregadoController);
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
