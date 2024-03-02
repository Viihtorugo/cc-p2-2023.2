package br.ufal.ic.p2.wepayu.strategy.criarempregado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.SystemController;
import br.ufal.ic.p2.wepayu.exceptions.*;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.utils.Utils;

public class CriarEmpregado implements StrategyCriarEmpregado {

    private boolean validNome(String nome) {
        if (nome.isEmpty()) throw new ExceptionNomeNaoPodeSerNulo();

        return true;
    }

    public boolean validEndereco(String endereco) {

        if (endereco.isEmpty()) throw new ExceptionEnderecoNaoPodeSerNulo();

        return true;
    }

    private boolean validTipoNaoComissionado(String tipo) {

        if (tipo.equals("assalariado") || tipo.equals("horista")) return true;

        if (tipo.equals("comissionado")) throw new ExceptionTipoNaoAplicavel();

        throw new ExceptionTipoInvalido();
    }

    private boolean validTipoComissionado(String tipo) {

        if (tipo.equals("comissionado")) return true;

        if (tipo.equals("assalariado") || tipo.equals("horista"))
            throw new ExceptionTipoNaoAplicavel();

        throw new ExceptionTipoInvalido();
    }

    private boolean validSalario(String salario) {

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

    @Override
    public Empregado executeCriarEmpregado(String nome, String endereco, String tipo,
                                           String salario, EmpregadoController empregadoController) {

        if (validNome(nome) && validEndereco(endereco)
                && validTipoNaoComissionado(tipo) && validSalario(salario)) {

            try {
                SystemController.pushUndo(empregadoController);
            } catch (Exception e) {
                throw new RuntimeException("Deu errado!");
            }

            double salarioFormatado = Utils.convertStringToDouble(salario);

            if (tipo.equals("assalariado")) {
                return new EmpregadoAssalariado(nome, endereco, "mensal $", salarioFormatado);
            } else if (tipo.equals("horista")) {
                return new EmpregadoHorista(nome, endereco, "semanal 5", salarioFormatado);
            }
        }

        return null;
    }

    @Override
    public Empregado executeCriarEmpregado(String nome, String endereco, String tipo,
                                           String salario, String comissao,
                                           EmpregadoController empregadoController) {

        if (validNome(nome) && validEndereco(endereco) && validTipoComissionado(tipo)
                && validSalario(salario) && validComissao(comissao)) {

            try {
                SystemController.pushUndo(empregadoController);
            } catch (Exception e) {
                throw new RuntimeException("Deu errado!");
            }

            double salarioFormatado = Utils.convertStringToDouble(salario);
            double comissaoFormatado = Utils.convertStringToDouble(comissao);

            return new EmpregadoComissionado(nome, endereco, "semanal 5 2", salarioFormatado, comissaoFormatado);
        }

        return null;
    }
}
