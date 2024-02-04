package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exceptions.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.models.Empregado;

import java.util.ArrayList;

public class Facade {

    public ArrayList<Empregado> empregados;

    public void zerarSistema() {
        if (this.empregados == null)
            this.empregados = new ArrayList<Empregado>();
    }

    public String getAtributoEmpregado(String emp, String atributo) throws EmpregadoNaoExisteException {
        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

        Empregado e = empregados.get(id);

        if (e == null) {
            throw new EmpregadoNaoExisteException();
        } else {
            if (atributo.equals("nome")) {
                return e.getNome();
            } else if (atributo.equals("tipo")) {
                return e.getTipo();
            } else if (atributo.equals("salario")) {
                String salario = e.getSalario();
                if (!salario.contains(",")) {
                    salario += ",00";
                }
                return salario;
            } else if (atributo.equals("endereco")) {
                return e.getEndereco();
            } else {
                return "false";
            }
        }

    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) {

        this.empregados.add(new Empregado(nome, endereco, tipo, salario));

        return Integer.toString(this.empregados.size() - 1);
    }
}
