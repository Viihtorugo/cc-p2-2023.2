package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.exceptions.*;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.MetodoPagamento;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class Valid {

    public boolean validNome(String nome) {
        if (nome.isEmpty())
            throw new ExceptionNomeNaoPodeSerNulo();

        return true;
    }

    public boolean validEndereco(String endereco) {

        if (endereco.isEmpty())
            throw new ExceptionEnderecoNaoPodeSerNulo();

        return true;
    }

    public boolean validTipoNaoComissionado(String tipo) {

        if (tipo.equals("assalariado") || tipo.equals("horista"))
            return true;

        if (tipo.equals("comissionado"))
            throw new ExceptionTipoNaoAplicavel();

        throw new ExceptionTipoInvalido();
    }

    public boolean validTipoComissionado(String tipo) {

        if (tipo.equals("comissionado"))
            return true;

        if (tipo.equals("assalariado") || tipo.equals("horista"))
            throw new ExceptionTipoNaoAplicavel();

        throw new ExceptionTipoInvalido();
    }

    public boolean validSalario(String salario) {

        if (salario.isEmpty()) {
            throw new ExceptionSalarioNaoPodeSerNulo();
        }

        if (!salario.matches("[0-9,-]+")) {
            throw new ExceptionSalarioDeveSerNumerico();
        }

        if (salario.contains("-")) {
            throw new ExceptionSalarioDeveSerNaoNegativo();
        }

        return true;
    }

    public boolean validComissao(String comissao) {

        if (comissao.isEmpty()) {
            throw new ExceptionComissaoNaoPodeSerNula();
        }

        if (!comissao.matches("[0-9,-]+")) {
            throw new ExceptionComissaoDeveSerNumerica();
        }

        if (comissao.contains("-")) {
            throw new ExceptionComissaoDeveSerNaoNegativa();
        }

        return true;
    }

    public boolean validEmpregado(String emp, EmpregadoController empregadoController) {

        if (emp.isEmpty())
            throw new ExceptionIdentificacaoDoEmpregadoNaoPodeSerNula();

        if (empregadoController.getEmpregado(emp) == null)
            throw new ExceptionEmpregadoNaoExiste();

        return true;
    }

    public boolean validGetAtributo(String atributo) {
        switch (atributo) {
            case "nome", "tipo", "salario", "endereco",
                    "comissao", "metodoPagamento", "banco",
                    "agencia", "contaCorrente", "sindicalizado",
                    "idSindicato", "taxaSindical", "agendaPagamento" -> {
                return true;
            }
            default -> {
                throw new ExceptionAtributoNaoExiste();
            }
        }
    }

    public boolean validTipoEmpregado(Empregado empregado, String tipo) {

        if (tipo.equals("comissionado") || tipo.equals("assalariado") || tipo.equals("horista")) {

            if (empregado.getTipo().equals(tipo)) {
                return true;
            } else {
                throw new ExceptionEmpregadoNaoEhTipo(tipo);
            }

        }

        throw new ExceptionTipoInvalido();
    }

    public boolean validAlterarTipoEmpregado(Empregado empregado, String tipo) {
        if (tipo.equals("comissionado") || tipo.equals("assalariado") || tipo.equals("horista")) {
            if (empregado.getTipo().equals(tipo)) {
                return false;
            } else {
                return true;
            }
        }

        throw new ExceptionTipoInvalido();
    }

    public boolean validData(String data, String tipo) {

        String[] blocos = data.split("/");

        int d = Integer.parseInt(blocos[0]);
        int m = Integer.parseInt(blocos[1]);
        int y = Integer.parseInt(blocos[2]);

        if (m > 12 || m < 1)
            throw new ExceptionDataInvalida(tipo);

        YearMonth yearMonth = YearMonth.of(y, m);

        if (d > yearMonth.lengthOfMonth())
            throw new ExceptionDataInvalida(tipo);

        return true;
    }

    public boolean validHoras(String horas) {

        if (horas.isEmpty()) {
            throw new ExceptionHorasNaoPodeSerNula();
        }

        if (!horas.matches("[0-9,-]+")) {
            throw new ExceptionHorasDeveSerNumerico();
        }

        double horasDouble = Utils.convertStringToDouble(horas);

        if (horasDouble <= 0) {
            throw new ExceptionHorasDevemSerPositivas();
        }

        return true;
    }

    public boolean validValor(String valor) {

        if (valor.isEmpty()) {
            throw new ExceptionValorNaoPodeSerNulo();
        }

        if (!valor.matches("[0-9,-]+")) {
            throw new ExceptionValorDeveSerNumerico();
        }

        double valorFormato = Utils.convertStringToDouble(valor);

        if (valorFormato <= 0) {
            throw new ExceptionValorDeveSerPositivo();
        }

        return true;
    }

    public boolean validSindicalizado(String valor) {
        if (valor.equals("true") || valor.equals("false"))
            return true;

        throw new ExceptionValorDeveSerTrueOuFalse();
    }

    public boolean validMetodoPagamento(String valor) {
        if (valor.equals("correios") || valor.equals("emMaos") || valor.equals("banco"))
            return true;

        throw new ExceptionMetodoDePagamentoInvalido();
    }

    public boolean validIdSindical(String idSindicato) {

        if (idSindicato.isEmpty()) {
            throw new ExceptionIdentificacaoDoSindicatoNaoPodeSerNula();
        }

        return true;
    }

    public boolean validMembroSindicalizadoPeloID(String membro, EmpregadoController empregadoController) {

        if (membro.isEmpty())
            throw new ExceptionIdentificacaoDoMembroNaoPodeSerNula();

        String id = empregadoController.getEmpregadoPorIdSindical(membro);

        if (id == null)
            throw new ExceptionMembroNaoExiste();

        return true;
    }

    public boolean validTaxaSindical(String taxaSindical) {

        if (taxaSindical.isEmpty()) {
            throw new ExceptionTaxaSindicalNaoPodeSerNula();
        }

        if (!taxaSindical.matches("[0-9,-]+")) {
            throw new ExceptionTaxaSindicalDeveSerNumerica();
        }

        double taxaSindicalNumber = Utils.convertStringToDouble(taxaSindical);

        if (taxaSindicalNumber <= 0.0) {
            throw new ExceptionTaxaSindicalDeveSerNaoNegativa();
        }

        return true;
    }

    public boolean sindicalizarEmpregado(String idSindicato, EmpregadoController empregadoController) {

        HashMap<String, Empregado> empregados = empregadoController.getEmpregados();

        for (Map.Entry<String, Empregado> entry : empregados.entrySet()) {
            MembroSindicalizado membroSindicalizado = entry.getValue().getSindicalizado();

            if (membroSindicalizado != null)
                if (membroSindicalizado.getIdMembro().equals(idSindicato))
                    throw new ExceptionHaOutroEmpregadoComEstaIdentificacaoDeSindicato();
        }

        return true;
    }

    public boolean validBanco(String banco, String agencia, String contaCorrente) {

        if (banco.isEmpty()) {
            throw new ExceptionBancoNaoPodeSerNulo();
        }

        if (agencia.isEmpty()) {
            throw new ExceptionAgenciaNaoPodeSerNulo();
        }

        if (contaCorrente.isEmpty()) {
            throw new ExceptionContaCorrenteNaoPodeSerNulo();
        }

        return true;
    }

    public boolean validBanco(MetodoPagamento metodo) {
        if (metodo.getMetodoPagamento().equals("banco"))
            return true;

        throw new ExceptionEmpregadoNaoRecebeEmBanco();
    }

    public boolean validMembroSindicalizado(Empregado empregado) {
        MembroSindicalizado membroSindicalizado = empregado.getSindicalizado();

        if (membroSindicalizado == null) {
            throw new ExceptionEmpregadoNaoEhSindicalizado();
        }

        return true;
    }
}
