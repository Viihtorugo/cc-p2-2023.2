package br.ufal.ic.p2.wepayu.strategy.empregadocomissionado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.SystemController;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.utils.Utils;
import br.ufal.ic.p2.wepayu.utils.Valid;

import java.time.LocalDate;

public class ContextoEmpregadoComissionado implements StrategyEmpregadoComissionado {

    private Valid verification;

    public ContextoEmpregadoComissionado() {
        this.verification = new Valid();
    }

    @Override
    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal,
                                      EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {

            Empregado empregado = empregadoController.getEmpregado(emp);

            if (this.verification.validTipoEmpregado(empregado, "comissionado")) {
                if (this.verification.validData(dataInicial, " inicial ")) {
                    if (this.verification.validData(dataFinal, " final ")) {
                        LocalDate dataInicialFormato = Utils.convertStringToLocalDate(dataInicial);
                        LocalDate dataFinalFormato = Utils.convertStringToLocalDate(dataFinal);

                        double vendas = ((EmpregadoComissionado) empregado).getVendasRealizadas(dataInicialFormato, dataFinalFormato);

                        return Utils.convertDoubleToString(vendas, 2);
                    }
                }
            }

        }

        return "0,00";
    }

    @Override
    public void executeLancaVenda(String emp, String data, String valor, EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {

            Empregado empregado = empregadoController.getEmpregado(emp);

            if (this.verification.validTipoEmpregado(empregado, "comissionado")) {
                if (this.verification.validValor(valor)) {
                    if (this.verification.validData(data, " ")) {

                        double valorFormato = Utils.convertStringToDouble(valor);

                        try {
                            SystemController.pushUndo(empregadoController);
                        } catch (Exception e) {
                            System.out.println("Deu Errado!");
                        }

                        ((EmpregadoComissionado) empregado).addVenda(data, valorFormato);
                    }
                }
            }
        }
    }

}