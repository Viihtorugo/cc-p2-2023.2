package br.ufal.ic.p2.wepayu.strategy.getatributo;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.exceptions.*;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Banco;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.utils.Valid;
import br.ufal.ic.p2.wepayu.utils.Utils;

public class GetAtributo implements StrategyGetAtributo {

    private Valid verification;

    public GetAtributo() {
        this.verification = new Valid();
    }

    private String getAtributoEmpregado(Empregado emp, String atributo){

        switch (atributo) {

            case "nome" -> {
                return emp.getNome();
            }
            case "agendaPagamento" -> {
                return emp.getAgendaDePagamento();
            }
            case "tipo" -> {
                return emp.getTipo();
            }
            case "salario" -> {
                return Utils.convertDoubleToString(emp.getSalario(), 2);
            }
            case "endereco" -> {
                return emp.getEndereco();
            }
            case "comissao" -> {

                if (emp.getTipo().equals("comissionado"))
                    return Utils.convertDoubleToString(((EmpregadoComissionado) emp).getTaxaDeComissao(), 2);

                return null;
            }

            case "metodoPagamento" -> {

                MetodoPagamento metodoPagamento = emp.getMetodoPagamento();

                if (metodoPagamento != null)
                    return metodoPagamento.getMetodoPagamento();

                return null;
            }

            case "banco", "agencia", "contaCorrente" -> {
                MetodoPagamento metodoPagamento = emp.getMetodoPagamento();

                if (!metodoPagamento.getMetodoPagamento().equals("banco"))
                    return null;

                if (atributo.equals("banco")) return ((Banco) metodoPagamento).getBanco();
                if (atributo.equals("agencia")) return ((Banco) metodoPagamento).getAgencia();

                return ((Banco) metodoPagamento).getContaCorrente();
            }

            case "sindicalizado" -> {
                if (emp.getSindicalizado() == null)
                    return "false";

                return "true";
            }

            case "idSindicato", "taxaSindical" -> {
                MembroSindicalizado membroSindicalizado = emp.getSindicalizado();

                if (membroSindicalizado == null)
                    throw new ExceptionEmpregadoNaoEhSindicalizado();

                if (atributo.equals("idSindicato"))
                    return membroSindicalizado.getIdMembro();

                return Utils.convertDoubleToString(emp.getSindicalizado().getTaxaSindical(), 2);

            }

        }

        return null;
    }

    @Override
    public String executeGetAtributo(String emp, String atributo, EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {
            if (this.verification.validGetAtributo(atributo)) {

                Empregado e = empregadoController.getEmpregado(emp);

                return getAtributoEmpregado(e, atributo);
            }
        }

        return null;
    }
}
