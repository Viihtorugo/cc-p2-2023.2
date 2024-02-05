package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionErrorMessage;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.controller.EmpregadoController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Facade {

    public void zerarSistema() {
        EmpregadoController.empregados = new HashMap<String, Empregado>();
    }

    public void encerrarSistema() {
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

    //termina o file us1

    public void removerEmpregado(String emp) throws ExceptionErrorMessage {

        if (emp.equals(""))
            throw new ExceptionErrorMessage("Identificacao do empregado nao pode ser nula.");

        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e == null)
            throw new ExceptionErrorMessage("Empregado nao existe.");

        EmpregadoController.empregados.remove(emp);
    }

    public String getHorasNormaisTrabalhadas(String emp, String dataIncial, String dataFinal) throws ExceptionErrorMessage {
        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e instanceof EmpregadoHorista) {
            return ((EmpregadoHorista) e).getHorasNormaisTrabalhadas(dataIncial, dataFinal);
        }

        throw new ExceptionErrorMessage("Empregado nao eh horista.");
    }

    public String getHorasExtrasTrabalhadas(String emp, String dataIncial, String dataFinal) throws ExceptionErrorMessage {
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

    public String getVendasRealizadas(String emp, String dataIncial, String dataFinal) throws ExceptionErrorMessage {
        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e instanceof EmpregadoComissionado) {
            return ((EmpregadoComissionado) e).getVendasRealizadas(dataIncial, dataFinal);
        }

        throw new ExceptionErrorMessage("Empregado nao eh comissionado.");
    }

    public void lancaVenda(String emp, String data, String horas) throws ExceptionErrorMessage {
        Empregado e = EmpregadoController.getEmpregado(emp);

        if (emp.equals(""))
            throw new ExceptionErrorMessage("Identificacao do empregado nao pode ser nula.");

        if (e == null)
            throw new ExceptionErrorMessage("Empregado nao existe.");

        if (e instanceof EmpregadoComissionado) {
            ((EmpregadoComissionado) e).addVenda(data, horas);
        } else {
            throw new ExceptionErrorMessage("Empregado nao eh comissionado.");
        }
    }

    public void alteraEmpregado(String emp, String atributo, String valor) {
        if (atributo.equals("nome")) {
            EmpregadoController.getEmpregado(emp).setNome(valor);
        } else if (atributo.equals("endereco")) {
            EmpregadoController.getEmpregado(emp).setEndereco(valor);
        } else if (atributo.equals("salario")) {
            EmpregadoController.getEmpregado(emp).setSalario(valor);
        } else if (atributo.equals("sindicalizado")) {
            if (valor.equals("false")) {
                EmpregadoController.empregados.get(emp).setSindicalizado(null);
            }
        }
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws ExceptionErrorMessage {
        if (atributo.equals("sindicalizado") && valor.equals("true")) {
            boolean v = true;

            for (Map.Entry<String, Empregado> entry : EmpregadoController.empregados.entrySet()) {
                MembroSindicalizado m = entry.getValue().getSindicalizado();

                if (m != null)
                    if (m.getIdMembro().equals(idSindicato))
                        v = false;
            }

            if (v == false)
                throw new ExceptionErrorMessage("Ha outro empregado com esta identificacao de sindicato");

            EmpregadoController.empregados.get(emp).setSindicalizado(new MembroSindicalizado(idSindicato, Double.parseDouble(taxaSindical.replace(",", "."))));
        }
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws ExceptionErrorMessage {

        double value = Double.parseDouble(valor.replace(",", "."));

        if (value <= 0)
            throw new ExceptionErrorMessage("Valor deve ser positivo.");

        if (membro.equals(""))
            throw new ExceptionErrorMessage("Identificacao do membro nao pode ser nula.");

        String id = EmpregadoController.getEmpregadoPorIdSindical(membro);

        if (id == null)
            throw new ExceptionErrorMessage("Membro nao existe.");

        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate date = LocalDate.parse(data, formato);
            EmpregadoController.getEmpregado(id).addTaxaServico(new TaxaServico(date, value));
        } catch (DateTimeParseException e) {
            throw new ExceptionErrorMessage("Data invalida.");
        }

    }

    public String getTaxasServico(String emp, String dataIncial, String dataFinal) throws ExceptionErrorMessage {

        LocalDate dateInit;
        LocalDate dateEnd;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");

        int d = 0, m = 0, y, i = 0;

        for (String s : dataFinal.split("/")) {
            if (i == 0) {
                d = Integer.parseInt(s);
                i++;
            } else if (i == 1) {
                m = Integer.parseInt(s);
                i++;
            } else {
                y = Integer.parseInt(s);
            }
        }

        if (m == 2 && d > 29) {
            throw new ExceptionErrorMessage("Data final invalida.");
        }

        dateEnd = LocalDate.parse(dataFinal, formato);

        try {
            dateInit = LocalDate.parse(dataIncial, formato);
        } catch (DateTimeParseException e) {
            throw new ExceptionErrorMessage("Data inicial invalida.");
        }

        if (dateInit.isAfter(dateEnd)) {
            throw new ExceptionErrorMessage("Data inicial nao pode ser posterior aa data final.");
        }

        if (dateInit.isEqual(dateEnd))
            return "0,00";

        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e == null)
            return "0,00";

        MembroSindicalizado memSind = e.getSindicalizado();

        if (memSind == null)
            throw new ExceptionErrorMessage("Empregado nao eh sindicalizado.");

        double countTaxas = 0;

        ArrayList<TaxaServico> taxas = memSind.getTaxaServicos();

        if (taxas == null)
            return "0,00";

        for (TaxaServico t : taxas) {
            if (t.getData().isEqual(dateInit) ||
                    (t.getData().isAfter(dateInit) && t.getData().isBefore(dateEnd))) {
                countTaxas += t.getValor();
            }
        }

        return String.format("%.2f", countTaxas).replace(".", ",");
    }
}
