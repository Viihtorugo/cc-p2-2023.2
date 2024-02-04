package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionErrorMessage;
import br.ufal.ic.p2.wepayu.models.Empregado;

import java.util.ArrayList;
import java.util.Objects;

public class Facade {

    public ArrayList<Empregado> empregados;

    public void zerarSistema() {
        if (this.empregados == null)
            this.empregados = new ArrayList<Empregado>();
    }

    public String getAtributoEmpregado(String emp, String atributo) throws ExceptionErrorMessage {

        if (emp.equals(""))
            throw new ExceptionErrorMessage("Identificacao do empregado nao pode ser nula.");

        if (!emp.matches("\\d+"))
            throw new ExceptionErrorMessage("Empregado nao existe.");

        Empregado e = empregados.get(Integer.parseInt(emp));

        if (atributo.equals("nome")) {
            return e.getNome();
        } else if (atributo.equals("tipo")) {
            return e.getTipo();
        } else if (atributo.equals("salario")) {
            String salario = e.getSalario();
            if (salario.contains(",")) {
                return salario;
            } else {
                return  salario + ",00";
            }
        } else if (atributo.equals("endereco")) {
            return e.getEndereco();
        } else if (atributo.equals("comissao")) {
            return e.getComissao();
        } else if(atributo.equals("sindicalizado")) {
            return "false";
        } else {
            throw new ExceptionErrorMessage("Atributo nao existe.");
        }

    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws ExceptionErrorMessage {

        if (nome.equals(""))
            throw new ExceptionErrorMessage("Nome nao pode ser nulo.");

        if (endereco.equals(""))
            throw new ExceptionErrorMessage("Endereco nao pode ser nulo.");

        if (tipo.equals("abc"))
            throw new ExceptionErrorMessage("Tipo invalido.");

        if (tipo.equals("comissionado"))
            throw new ExceptionErrorMessage("Tipo nao aplicavel.");

        if (salario.equals(""))
            throw new ExceptionErrorMessage("Salario nao pode ser nulo.");

        if (!salario.matches("[0-9,-]+"))
            throw new ExceptionErrorMessage("Salario deve ser numerico.");

        if (salario.contains("-"))
            throw new ExceptionErrorMessage("Salario deve ser nao-negativo.");

        this.empregados.add(new Empregado(nome, endereco, tipo, salario));

        return Integer.toString(this.empregados.size() - 1);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws ExceptionErrorMessage {

        if (nome.equals(""))
            throw new ExceptionErrorMessage("Nome nao pode ser nulo.");

        if (endereco.equals(""))
            throw new ExceptionErrorMessage("Endereco nao pode ser nulo.");

        if (tipo.equals("abc"))
            throw new ExceptionErrorMessage("Tipo invalido.");

        if (tipo.equals("horista") || tipo.equals("assalariado"))
            throw new ExceptionErrorMessage("Tipo nao aplicavel.");

        if (salario.equals(""))
            throw new ExceptionErrorMessage("Salario nao pode ser nulo.");

        if (!salario.matches("[0-9,-]+"))
            throw new ExceptionErrorMessage("Salario deve ser numerico.");

        if (salario.contains("-"))
            throw new ExceptionErrorMessage("Salario deve ser nao-negativo.");

        if (comissao.equals(""))
            throw new ExceptionErrorMessage("Comissao nao pode ser nula.");

        if (!comissao.matches("[0-9,-]+"))
            throw new ExceptionErrorMessage("Comissao deve ser numerica.");

        if (comissao.contains("-"))
            throw new ExceptionErrorMessage("Comissao deve ser nao-negativa.");


        this.empregados.add(new Empregado(nome, endereco, tipo, salario, comissao));

        return Integer.toString(this.empregados.size() - 1);
    }

    public void encerrarSistema() {}
}
