package br.ufal.ic.p2.wepayu.strategy.empregadohorista;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.SystemController;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.utils.Valid;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.LocalDate;

public class ContextoEmpregadoHorista implements StrategyEmpregadoHorista {
    private Valid verification;
    public ContextoEmpregadoHorista() {
        this.verification = new Valid();
    }

    @Override
    public String getHorasTrabalhadas(String emp, String dataInicial, String dataFinal,
                                      EmpregadoController empregadoController, boolean normais) {

        if (this.verification.validEmpregado(emp, empregadoController)) {
            Empregado empregado = empregadoController.getEmpregado(emp);

            if (this.verification.validTipoEmpregado(empregado, "horista")) {
                if (this.verification.validData(dataFinal, " final ")) {
                    if (this.verification.validData(dataInicial, " inicial ")) {

                        LocalDate dataInicialFormato = Utils.convertStringToLocalDate(dataInicial);
                        LocalDate dataFinalFormato = Utils.convertStringToLocalDate(dataFinal);

                        double horasTrabalhadas;

                        if (normais) {
                            horasTrabalhadas = ((EmpregadoHorista) empregado).getHorasNormaisTrabalhadas(
                                                dataInicialFormato, dataFinalFormato);
                        } else {
                            horasTrabalhadas = ((EmpregadoHorista) empregado).getHorasExtrasTrabalhadas(
                                    dataInicialFormato, dataFinalFormato);
                        }

                        return Utils.convertDoubleToString(horasTrabalhadas);
                    }
                }
            }
        }

        if (normais) {
            return "0";
        } else {
            return "0,00";
        }
    }

    @Override
    public void executeLancaCartao(String emp, String data, String horas,
                            EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {

            Empregado empregado = empregadoController.getEmpregado(emp);

            if (this.verification.validTipoEmpregado(empregado, "horista")) {
                if (this.verification.validData(data, " ")) {
                    if (this.verification.validHoras(horas)) {

                        double horasFormato = Utils.convertStringToDouble(horas);

                        try {
                            SystemController.pushUndo(empregadoController);
                        } catch (Exception e) {
                            throw new RuntimeException("Deu errado!");
                        }

                        ((EmpregadoHorista) empregado).addRegistro(data, horasFormato);
                    }
                }

            }
        }


    }


}