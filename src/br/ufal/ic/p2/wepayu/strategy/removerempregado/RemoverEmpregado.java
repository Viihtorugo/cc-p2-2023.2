package br.ufal.ic.p2.wepayu.strategy.removerempregado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.SystemController;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregadoNaoExiste;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionIdentificacaoDoEmpregadoNaoPodeSerNula;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.File;

public class RemoverEmpregado implements StrategyRemoverEmpregado{

    private boolean validEmpregado(String emp, EmpregadoController empregadoController) {

        if (emp.isEmpty())
            throw new ExceptionIdentificacaoDoEmpregadoNaoPodeSerNula();

        if (empregadoController.getEmpregado(emp) == null)
            throw new ExceptionEmpregadoNaoExiste();

        return true;
    }

    @Override
    public void executeRemoveEmpregado(String emp, EmpregadoController empregadoController) {

        if (validEmpregado(emp, empregadoController)) {

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
