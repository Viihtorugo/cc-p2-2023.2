package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionErrorMessage;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Facade {

    public void zerarSistema() {
        EmpregadoController.empregados = new HashMap<String, Empregado>();
    }

    public String getAtributoEmpregado(String emp, String atributo) throws ExceptionErrorMessage {

        if (emp.equals(""))
            throw new ExceptionErrorMessage("Identificacao do empregado nao pode ser nula.");

        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e == null)
            throw new ExceptionErrorMessage("Empregado nao existe.");

        switch (atributo) {
            case "nome" -> {
                return e.getNome();
            }
            case "tipo" -> {
                return e.getTipo();
            }
            case "salario" -> {
                String salario = e.getSalario();
                if (salario.contains(",")) {
                    return salario;
                } else {
                    return salario + ",00";
                }
            }
            case "endereco" -> {
                return e.getEndereco();
            }
            case "comissao" -> {

                if (e instanceof EmpregadoComissionado)
                    return ((EmpregadoComissionado) e).getTaxaDeComissao();
            }
            case "sindicalizado" -> {
                return "false";
            }
            default -> throw new ExceptionErrorMessage("Atributo nao existe.");
        }

        return "false";
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

        if (tipo.equals("assalariado")) {
            return EmpregadoController.setEmpregado(new EmpregadoAssalariado(nome, endereco, salario));
        } else if (tipo.equals("horista")) {
            return EmpregadoController.setEmpregado(new EmpregadoHorista(nome, endereco, salario));
        }

        return "0";
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

        return EmpregadoController.setEmpregado(new EmpregadoComissionado(nome, endereco, salario, comissao));
    }

    public String getEmpregadoPorNome(String nome, int indice) throws ExceptionErrorMessage {

        int count = 0;

        for (Map.Entry<String, Empregado> entry : EmpregadoController.empregados.entrySet()) {

            Empregado e = entry.getValue();

            if (nome.contains(e.getNome()))
                count++;

            if (count == indice)
                return entry.getKey();
        }

        throw new ExceptionErrorMessage("Nao ha empregado com esse nome.");
    }

    public void encerrarSistema() {
    }

    //termina o file us1

    public void removerEmpregado(String emp) throws ExceptionErrorMessage {

        if (emp.equals(""))
            throw new ExceptionErrorMessage("Identificacao do empregado nao pode ser nula.");

        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e == null)
            throw new ExceptionErrorMessage("Empregado nao existe.");

        EmpregadoController.empregados.remove(emp);
    }

    public String getHorasNormaisTrabalhadas (String emp, String dataIncial, String dataFinal) throws ExceptionErrorMessage {
        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e instanceof EmpregadoHorista) {
            return ((EmpregadoHorista) e).getHorasNormaisTrabalhadas(dataIncial, dataFinal);
        }

        throw new ExceptionErrorMessage("Empregado nao eh horista.");
    }

    public String getHorasExtrasTrabalhadas (String emp, String dataIncial, String dataFinal) throws ExceptionErrorMessage {
        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e instanceof EmpregadoHorista) {
            return ((EmpregadoHorista) e).getHorasExtrasTrabalhadas(dataIncial, dataFinal);
        }

        throw new ExceptionErrorMessage("Empregado nao eh horista.");
    }

    public void lancaCartao(String emp, String data, String horas) throws ExceptionErrorMessage {
        Empregado e = EmpregadoController.getEmpregado(emp);

        if (emp.equals(""))
            throw new ExceptionErrorMessage("Identificacao do empregado nao pode ser nula.");

        if (e == null)
            throw new ExceptionErrorMessage("Empregado nao existe.");

        if (e instanceof EmpregadoHorista) {
            ((EmpregadoHorista) e).addRegistro(data, horas);
        } else {
            throw new ExceptionErrorMessage("Empregado nao eh horista.");
        }
    }
}
