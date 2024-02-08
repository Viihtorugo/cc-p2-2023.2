package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.controller.FolhaDePagamentoController;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Facade {

    public void zerarSistema() {
        EmpregadoController.empregados = new HashMap<String, Empregado>();
        EmpregadoController.key = 0;
        System.out.println("-> Sistema zerado");
    }

    public void encerrarSistema() {
        System.out.println("-> Sistema encerrado");
    }

    //4 variaveis
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {

        if (!Utils.validCriarEmpregado(nome, endereco, tipo, salario))
            return null;

        if (!Utils.validTipoNotComissionado(tipo))
            return null;

        double salarioFormato = Utils.validSalario(salario);

        if (salarioFormato <= 0)
            return null;

        if (tipo.equals("assalariado")) {
            return EmpregadoController.setEmpregado(new EmpregadoAssalariado(nome, endereco, salarioFormato));
        } else if (tipo.equals("horista")) {
            return EmpregadoController.setEmpregado(new EmpregadoHorista(nome, endereco, salarioFormato));
        }

        return null;
    }

    //5 variaveis
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {

        if (!Utils.validCriarEmpregado(nome, endereco, tipo, salario, comissao))
            return null;

        if (!Utils.validTipoComissionado(tipo))
            return null;

        double salarioFormato = Utils.validSalario(salario);

        if (salarioFormato <= 0)
            return null;

        double comissaoFormato = Utils.validComissao(comissao);

        if (comissaoFormato <= 0)
            return null;

        return EmpregadoController.setEmpregado(new EmpregadoComissionado(nome, endereco, salarioFormato, comissaoFormato));
    }

    public void lancaCartao(String emp, String data, String horas) throws Exception {

        Empregado e = Utils.validEmpregado(emp);

        if (e == null)
            return;

        if (Utils.validTipoEmpregado(e, "horista")) {
            double horasFormato = Utils.validHoras(horas);
            LocalDate dataFormato = Utils.validData(data, " ");

            if (horasFormato <= 0 || dataFormato == null)
                return;

            ((EmpregadoHorista) e).addRegistro(dataFormato, horasFormato);
        }
    }

    public void lancaVenda(String emp, String data, String valor) throws Exception {
        Empregado e = Utils.validEmpregado(emp);

        if (e == null)
            return;

        if (Utils.validTipoEmpregado(e, "comissionado")) {
            double valorFormato = Utils.validValor(valor);
            LocalDate dataFormato = Utils.validData(data, " ");

            if (valorFormato <= 0 || dataFormato == null)
                return;

            ((EmpregadoComissionado) e).addVenda(dataFormato, valorFormato);
        }
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws Exception {

        String id = Utils.validMembroSindicalizadoPeloID(membro);

        if (id == null)
            return;

        double valorFormato = Utils.validValor(valor);
        LocalDate dataFormato = Utils.validData(data, " ");

        if (valorFormato <= 0 || dataFormato == null)
            return;

        EmpregadoController.getEmpregado(id).addTaxaServico(new TaxaServico(dataFormato, valorFormato));
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {

        int count = 0;

        for (Map.Entry<String, Empregado> entry : EmpregadoController.empregados.entrySet()) {

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

        Empregado e = Utils.validEmpregado(emp);

        if (e == null)
            return null;

        if (!Utils.validGetAtributo(emp, atributo))
            return null;

        switch (atributo) {

            case "nome" -> {
                return e.getNome();
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
        Empregado e = Utils.validEmpregado(emp);

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
        Empregado e = Utils.validEmpregado(emp);

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
        Empregado e = Utils.validEmpregado(emp);

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

        Empregado e = Utils.validEmpregado(emp);

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

        Empregado e = Utils.validEmpregado(emp);

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
                    case "horista" -> EmpregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, salario));
                    case "assalariado" ->
                            EmpregadoController.setValue(emp, new EmpregadoAssalariado(nome, endereco, salario));
                    case "comissionado" ->
                            EmpregadoController.setValue(emp, new EmpregadoComissionado(nome, endereco, salario, 0));
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
        Empregado e = Utils.validEmpregado(emp);

        if (e == null)
            return;

        if (!Utils.validGetAtributo(emp, atributo))
            return;

        String nome = e.getNome();
        String endereco = e.getEndereco();

        if (valor.equals("comissionado")) {
            double salario = e.getSalario();
            double comissao = Utils.validComissao(sal);

            if (comissao < 0)
                return;

            EmpregadoController.setValue(emp, new EmpregadoComissionado(nome, endereco, salario, comissao));

        } else if (valor.equals("horista")) {
            double novoSalario = Utils.validSalario(sal);

            if (novoSalario < 0)
                return;

            EmpregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, novoSalario));
        }
    }

    // 5 variaveis
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception {

        if (Utils.validIdSindical(idSindicato))
            return;

        double taxaSindicalNumber = Utils.validTaxaSindical(taxaSindical);

        if (taxaSindicalNumber <= 0.0)
            return;

        if (atributo.equals("sindicalizado") && valor.equals("true")) {

            if (Utils.sindicalizarEmpregado(idSindicato)) {
                Empregado e = Utils.validEmpregado(emp);

                if (e == null) return;

                e.setSindicalizado(new MembroSindicalizado(idSindicato, taxaSindicalNumber));
            }
        }
    }

    // 6 variaveis
    public void alteraEmpregado(String emp, String atributo, String tipo, String banco, String agencia, String contaCorrente) throws Exception {
        Empregado e = Utils.validEmpregado(emp);

        if (e == null)
            return;

        if (!Utils.validGetAtributo(emp, atributo))
            return;

        if (Utils.validBanco(banco, agencia, contaCorrente))
            e.setMetodoPagamento(new Banco(banco, agencia, contaCorrente));
    }

    public void removerEmpregado(String emp) throws Exception {

        Empregado e = Utils.validEmpregado(emp);

        if (e == null)
            return;

        EmpregadoController.empregados.remove(emp);
    }

    public String totalFolha(String data) throws Exception {

        FolhaDePagamento folha = new FolhaDePagamento(   EmpregadoController.getEmpregadoHoristas(),
                                        EmpregadoController.getEmpregadoComissionado(),
                                        EmpregadoController.getEmpregadoAssalariado());

        LocalDate dataFormato = Utils.validData(data, " ");

        folha.rodaFolha(dataFormato);

        double total = folha.totalFolhaSalarioBruto();

        return Utils.convertDoubleToString(total, 2);
    }

}
