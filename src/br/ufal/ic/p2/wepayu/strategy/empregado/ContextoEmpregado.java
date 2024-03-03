package br.ufal.ic.p2.wepayu.strategy.empregado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.SystemController;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.utils.Valid;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ContextoEmpregado implements StrategyCriarEmpregado, StrategyRemoverEmpregado {
    private Valid verification;

    public ContextoEmpregado () {
        this.verification = new Valid();
    }

    @Override
    public Empregado executeCriarEmpregado(String nome, String endereco, String tipo,
                                           String salario, EmpregadoController empregadoController) {

        if (this.verification.validNome(nome) && this.verification.validEndereco(endereco)
                && this.verification.validTipoNaoComissionado(tipo) && this.verification.validSalario(salario)) {

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

        if (this.verification.validNome(nome) && this.verification.validEndereco(endereco) &&
                this.verification.validTipoComissionado(tipo) && this.verification.validSalario(salario)
                && this.verification.validComissao(comissao)) {

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

//    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
//
//        int count = 0;
//
//        HashMap<String, Empregado> empregados = this.empregadoController.getEmpregados();
//
//        for (Map.Entry<String, Empregado> entry : empregados.entrySet()) {
//
//            Empregado e = entry.getValue();
//
//            if (nome.contains(e.getNome()))
//                count++;
//
//            if (count == indice)
//                return entry.getKey();
//        }
//
//        ExceptionEmpregado ex = new ExceptionEmpregado();
//
//        ex.msgEmpregadoNaoExistePorNome();
//
//        return null;
//    }

    @Override
    public void executeRemoveEmpregado(String emp, EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {

            try {
                SystemController.pushUndo(empregadoController);
            } catch (Exception e) {
                throw new RuntimeException("Deu errado!");
            }

            File file = new File(emp + ".xml");

            if (file.exists()) {
                System.out.println("Empredado deletado!");
                file.delete();
            }

            empregadoController.removeEmpregado(emp);
        }
    }
}
