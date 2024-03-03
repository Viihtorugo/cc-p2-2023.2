package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.exceptions.*;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Valid {

    public boolean validNome(String nome) {
        if (nome.isEmpty()) throw new ExceptionNomeNaoPodeSerNulo();

        return true;
    }

    public boolean validEndereco(String endereco) {

        if (endereco.isEmpty()) throw new ExceptionEnderecoNaoPodeSerNulo();

        return true;
    }

    public boolean validTipoNaoComissionado(String tipo) {

        if (tipo.equals("assalariado") || tipo.equals("horista")) return true;

        if (tipo.equals("comissionado")) throw new ExceptionTipoNaoAplicavel();

        throw new ExceptionTipoInvalido();
    }

    public boolean validTipoComissionado(String tipo) {

        if (tipo.equals("comissionado")) return true;

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

    public boolean validTipoEmpregado(Empregado e, String tipo) {

        if (tipo.equals("comissionado") || tipo.equals("assalariado") || tipo.equals("horista")) {

            if (e.getTipo().equals(tipo)) {
                return true;
            } else {
                throw new ExceptionEmpregadoNaoEhTipo(tipo);
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

    public boolean validValor(String valor){

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
}
