package br.ufal.ic.p2.wepayu.controller;

import br.ufal.ic.p2.wepayu.database.EmpregadoXML;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FacadeController {

    private EmpregadoController empregadoController;

    public FacadeController () {
        this.empregadoController = new EmpregadoController();
        EmpregadoXML xml = new EmpregadoXML();
        this.empregadoController.setEmpregados(xml.readEmpregados());
    }
    public void zerarSistema() throws Exception {
        SystemController.pushUndo(this.empregadoController);

        this.empregadoController = new EmpregadoController();
        Utils.deleteFilesXML();
        Utils.deleteFolhas();
        System.out.println("-> Sistema zerado");
    }

    public void encerrarSistema () {
        System.out.println("-> Sistema encerrado");

        EmpregadoXML xml = new EmpregadoXML();
        xml.save(this.empregadoController.getEmpregados());
        SystemController.systemOff();
    }

    //4 variaveis
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        if (!Utils.validCriarEmpregado(nome, endereco, tipo, salario))
            return null;

        if (!Utils.validTipoNotComissionado(tipo))
            return null;

        double salarioFormato = Utils.validSalario(salario);

        if (salarioFormato <= 0)
            return null;

        if (tipo.equals("assalariado")) {
            return this.empregadoController.setEmpregado(new EmpregadoAssalariado(nome, endereco, "mensal $", salarioFormato));
        } else if (tipo.equals("horista")) {
            return this.empregadoController.setEmpregado(new EmpregadoHorista(nome, endereco, "semanal 5", salarioFormato));
        }

        return null;
    }

    public String criarEmpregado (String nome, String endereco, String tipo, String salario, String comissao) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        if (!Utils.validCriarEmpregado(nome, endereco, tipo, salario, comissao)) {
            return null;
        }

        if (!Utils.validTipoComissionado(tipo)) {
            return null;
        }

        double salarioFormato = Utils.validSalario(salario);

        if (salarioFormato <= 0) {
            return null;
        }

        double comissaoFormato = Utils.validComissao(comissao);

        if (comissaoFormato <= 0) {
            return null;
        }

        return this.empregadoController.setEmpregado(new EmpregadoComissionado(nome, endereco, "semanal 2 5", salarioFormato, comissaoFormato));
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
        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null)
            return null;

        if (!Utils.validGetAtributo(emp, atributo))
            return null;

        switch (atributo) {

            case "nome" -> {
                return e.getNome();
            }
            case "agendaPagamento" -> {
                return e.getAgendaDePagamento();
            }
            case "tipo" -> {
                return e.getTipo();
            }
            case "salario" -> {
                return Utils.convertDoubleToString(e.getSalario(), 2);
            }
            case "endereco" -> {
                return e.getEndereco();
            }
            case "comissao" -> {

                if (Utils.empregadoIsNotComissionado(e))
                    return Utils.convertDoubleToString(((EmpregadoComissionado) e).getTaxaDeComissao(), 2);

                return null;
            }

            case "metodoPagamento" -> {
                return e.getMetodoPagamento().getMetodoPagamento();
            }

            case "banco", "agencia", "contaCorrente" -> {
                MetodoPagamento metodoPagamento = e.getMetodoPagamento();

                if (!Utils.metodoPagamentoIsBanco(metodoPagamento))
                    return null;

                if (atributo.equals("banco")) return ((Banco) metodoPagamento).getBanco();
                if (atributo.equals("agencia")) return ((Banco) metodoPagamento).getAgencia();

                return ((Banco) metodoPagamento).getContaCorrente();
            }

            case "sindicalizado" -> {
                if (e.getSindicalizado() == null) return "false";
                else return "true";
            }

            case "idSindicato", "taxaSindical" -> {
                MembroSindicalizado ms = e.getSindicalizado();

                if (!Utils.validSindicato(ms))
                    return null;

                if (atributo.equals("idSindicato")) return ms.getIdMembro();

                return Utils.convertDoubleToString(e.getSindicalizado().getTaxaSindical(), 2);
            }

        }

        return null;
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
                if (Utils.validAgendaPagamento(valor))
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
        SystemController.pushUndo(this.empregadoController);

        Empregado e = Utils.validEmpregado(emp, this.empregadoController);

        if (e == null)
            return;

        File file = new File( emp + ".xml" );

        if (file.exists()) {
            System.out.println("Empredado deletado!");
            file.delete();
        }

        this.empregadoController.removeEmpregado(emp);
    }

    public String totalFolha(String data) throws Exception {

        LocalDate dataFormato = Utils.validData(data, "");

        FolhaDePagamento folha = new FolhaDePagamento(dataFormato);

        double total = folha.totalFolha(this.empregadoController);

        return Utils.convertDoubleToString(total, 2);
    }

    public void rodaFolha(String data, String saida) throws Exception {
        SystemController.pushUndo(this.empregadoController);

        LocalDate dataFormato = Utils.validData(data, "");

        FolhaDePagamento folha = new FolhaDePagamento(dataFormato, saida);

        folha.geraFolha(this.empregadoController);
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
}
