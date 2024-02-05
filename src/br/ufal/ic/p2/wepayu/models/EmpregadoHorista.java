package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionErrorMessage;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class EmpregadoHorista extends Empregado {

    private String salarioPorHora;
    private ArrayList<CartaoDePonto> cartao;

    public EmpregadoHorista(String nome, String endereco, String salarioPorHora) {
        super(nome, endereco);
        this.salarioPorHora = salarioPorHora;
        this.cartao = new ArrayList<CartaoDePonto>();
    }

    public String getSalarioPorHora() {
        return salarioPorHora;
    }

    public void addRegistro(String dataString, String horas) throws ExceptionErrorMessage {

        if (Double.parseDouble(horas.replace(",", ".")) <= 0) {
            throw new ExceptionErrorMessage("Horas devem ser positivas.");
        }

        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate dataFormato = LocalDate.parse(dataString, formato);

            this.cartao.add(new CartaoDePonto(dataFormato, Double.parseDouble(horas.replace(",", "."))));
        } catch (DateTimeParseException e) {
            throw new ExceptionErrorMessage("Data invalida.");
        }

    }

    public String getHorasNormaisTrabalhadas(String dataIncial, String dataFinal) throws ExceptionErrorMessage {

        double horasAcumuladas = 0;
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

        if (dateInit.isEqual(dateEnd)) {
            return "0";
        }

        for (CartaoDePonto c : cartao) {
            if (c.getData().isEqual(dateInit) ||
                    (c.getData().isAfter(dateInit) && c.getData().isBefore(dateEnd))) {

                System.out.println(c.getData() + " " + c.getHoras());

                if (c.getHoras() > 8) {
                    horasAcumuladas += 8.0;
                } else {
                    horasAcumuladas += c.getHoras();
                }
            }
        }

        if (horasAcumuladas != (int) horasAcumuladas)
            return Double.toString(horasAcumuladas).replace(".", ",");

        return Integer.toString((int) horasAcumuladas);
    }

    public String getHorasExtrasTrabalhadas(String dataIncial, String dataFinal) {
        double horasAcumuladas = 0;

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");

        LocalDate dateInit = LocalDate.parse(dataIncial, formato);
        LocalDate dateEnd = LocalDate.parse(dataFinal, formato);

        for (CartaoDePonto c : cartao) {
            if (c.getData().isEqual(dateInit) ||
                    (c.getData().isAfter(dateInit) && c.getData().isBefore(dateEnd))) {

                if (c.getHoras() > 8) {
                    horasAcumuladas += (c.getHoras() - 8.0);
                }
            }
        }

        if (horasAcumuladas != (int) horasAcumuladas)
            return Double.toString(horasAcumuladas).replace(".", ",");

        return Integer.toString((int) horasAcumuladas);
    }


    public String getSalario() {
        return salarioPorHora;
    }

    @Override
    public String getTipo() {
        return "horista";
    }
}
