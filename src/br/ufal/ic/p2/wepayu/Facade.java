package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionErrorMessage;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.utils.Utils;

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

        if (emp.isEmpty())
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

                throw new ExceptionErrorMessage("Empregado nao eh comissionado.");
            }

            case "metodoPagamento" -> {
                return e.getMetodoPagamento().getMetodoPagamento();
            }

            case "banco", "agencia", "contaCorrente" -> {
                MetodoPagamento metodoPagamento = e.getMetodoPagamento();

                if (!(metodoPagamento instanceof  Banco)) {
                    throw new ExceptionErrorMessage("Empregado nao recebe em banco.");
                }

                if (atributo.equals("banco")) return ((Banco) metodoPagamento).getBanco();
                if (atributo.equals("agencia")) return ((Banco) metodoPagamento).getAgencia();

                return ((Banco) metodoPagamento).getContaCorrente();
            }

            case "sindicalizado" -> {
                if (e.getSindicalizado() == null) return "false";
                else return "true";
            }

            case "idSindicato", "taxaSindical" -> {
                MembroSindicalizado ms = e.getSindicalizado();

                if (ms == null) throw new ExceptionErrorMessage("Empregado nao eh sindicalizado.");

                if (atributo.equals("idSindicato")) return ms.getIdMembro();

                return Utils.convertDoubleToString(e.getSindicalizado().getTaxaSindical(), 2);
            }

            default -> throw new ExceptionErrorMessage("Atributo nao existe.");
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

        if (emp.isEmpty())
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

    public void alteraEmpregado(String emp, String atributo, String valor) throws ExceptionErrorMessage {
        if(emp.isEmpty()) throw new ExceptionErrorMessage("Identificacao do empregado nao pode ser nula.");

        Empregado e = EmpregadoController.getEmpregado(emp);

        if(e == null) throw new ExceptionErrorMessage("Empregado nao existe.");

        switch (atributo) {
            case "nome" -> {
                if(valor.isEmpty()) throw new ExceptionErrorMessage("Nome nao pode ser nulo.");

                e.setNome(valor);
            }
            case "endereco" -> {
                if(valor.isEmpty()) throw new ExceptionErrorMessage("Endereco nao pode ser nulo.");

                e.setEndereco(valor);
            }
            case "salario" -> {
                if (valor.isEmpty()) throw new ExceptionErrorMessage("Salario nao pode ser nulo.");

                try {
                    double salario = Double.parseDouble(valor.replace(',', '.'));

                    if(salario <= 0.0) throw new ExceptionErrorMessage("Salario deve ser nao-negativo.");

                } catch (NumberFormatException ex) {
                    throw new ExceptionErrorMessage("Salario deve ser numerico.");
                }

                e.setSalario(valor);
            }
            case "sindicalizado" -> {
                if(!valor.equals("false") && !valor.equals("true")) throw new ExceptionErrorMessage("Valor deve ser true ou false.");

                if (valor.equals("false")) {
                    e.setSindicalizado(null);
                }
            }
            case "comissao" -> {

                if(valor.isEmpty()) throw new ExceptionErrorMessage("Comissao nao pode ser nula.");

                try {
                    double comissao = Double.parseDouble(valor.replace(',', '.'));

                    if(comissao <= 0.0) throw new ExceptionErrorMessage("Comissao deve ser nao-negativa.");

                } catch (NumberFormatException ex) {
                    throw new ExceptionErrorMessage("Comissao deve ser numerica.");
                }

                if (!(e instanceof EmpregadoComissionado))
                    throw new ExceptionErrorMessage("Empregado nao eh comissionado.");

                ((EmpregadoComissionado) e).setTaxaDeComissao(valor);
            }
            case "tipo" -> {
                String nome = e.getNome();
                String endereco = e.getEndereco();

                if(valor.equals("horista"))
                    EmpregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, "0"));

                else if (valor.equals("assalariado"))
                    EmpregadoController.setValue(emp, new EmpregadoAssalariado(nome, endereco, "0"));
                else if(valor.equals("comissionado")) EmpregadoController.setValue(emp, new EmpregadoComissionado(nome, endereco, "0", "0"));

                else throw new ExceptionErrorMessage("Tipo invalido.");
            }

            case "metodoPagamento" -> {
                if (valor.equals("correios")) e.setMetodoPagamento(new Correios());

                else if(valor.equals("emMaos")) e.setMetodoPagamento(new EmMaos());

                else throw new ExceptionErrorMessage("Metodo de pagamento invalido.");

            }
            default -> throw new ExceptionErrorMessage("Atributo nao existe.");
        }
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String sal) throws ExceptionErrorMessage {
        Empregado e = EmpregadoController.getEmpregado(emp);



        String nome = e.getNome();
        String endereco = e.getEndereco();

        if (valor.equals("comissionado"))
            EmpregadoController.setValue(emp, new EmpregadoComissionado(nome, endereco, "0", sal));
        else if(valor.equals("horista"))
            EmpregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, sal));
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws ExceptionErrorMessage {
        if(idSindicato.isEmpty()) throw new ExceptionErrorMessage("Identificacao do sindicato nao pode ser nula.");

        if(taxaSindical.isEmpty()) throw new ExceptionErrorMessage("Taxa sindical nao pode ser nula.");

        try {
            double taxaSindicalNumber = Double.parseDouble(taxaSindical.replace(",", "."));

            if(taxaSindicalNumber <= 0.0) throw new ExceptionErrorMessage("Taxa sindical deve ser nao-negativa.");

        } catch (NumberFormatException ex) {
            throw new ExceptionErrorMessage("Taxa sindical deve ser numerica.");
        }

        if (atributo.equals("sindicalizado") && valor.equals("true")) {
            boolean flag = true;



            for (Map.Entry<String, Empregado> entry : EmpregadoController.empregados.entrySet()) {
                MembroSindicalizado m = entry.getValue().getSindicalizado();

                if (m != null)
                    if (m.getIdMembro().equals(idSindicato))
                        flag = false;
            }

            if (!flag)
                throw new ExceptionErrorMessage("Ha outro empregado com esta identificacao de sindicato");

            Empregado e = EmpregadoController.getEmpregado(emp);
            e.setSindicalizado(new MembroSindicalizado(idSindicato, Double.parseDouble(taxaSindical.replace(",", "."))));
        }
    }

    public void alteraEmpregado(String emp, String atributo, String tipo, String banco, String agencia, String contaCorrente) throws ExceptionErrorMessage {
        Empregado e = EmpregadoController.getEmpregado(emp);

        if (!tipo.equals("banco")) throw new ExceptionErrorMessage("Não implementado");

        if(banco.isEmpty()) throw new ExceptionErrorMessage("Banco nao pode ser nulo.");
        if(agencia.isEmpty()) throw new ExceptionErrorMessage("Agencia nao pode ser nulo.");
        if(contaCorrente.isEmpty()) throw new ExceptionErrorMessage("Conta corrente nao pode ser nulo.");


        e.setMetodoPagamento(new Banco(banco, agencia, contaCorrente));
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
