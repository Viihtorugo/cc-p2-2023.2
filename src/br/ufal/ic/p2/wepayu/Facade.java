package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exceptions.EmpregadoNaoExisteException;

public class Facade {
    public void zerarSistema() {};

    public void getAtributoEmpregado(String emp, String atributo) throws EmpregadoNaoExisteException {
        throw new EmpregadoNaoExisteException();
    }

}
