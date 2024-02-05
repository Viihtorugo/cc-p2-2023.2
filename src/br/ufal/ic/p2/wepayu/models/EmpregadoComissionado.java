package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionErrorMessage;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class EmpregadoComissionado extends Empregado {

    private String taxaDeComissao;
    private String salarioMensal;
    private ArrayList<CartaoDeVenda> vendas;

    public EmpregadoComissionado(String nome, String endereco, String salarioMensal, String taxaDeComissao) {
        super(nome, endereco);
        this.taxaDeComissao = taxaDeComissao;
        this.salarioMensal = salarioMensal;
        this.vendas = new ArrayList<CartaoDeVenda>();
    }

    public String getSalarioMensal() {
        return salarioMensal;
    }

    public String getTaxaDeComissao() {
        return taxaDeComissao;
    }

    public void addVenda(String dataString, String valor) throws ExceptionErrorMessage {

        double value = Double.parseDouble(valor.replace(",", "."));

        if (value <= 0) {
            throw new ExceptionErrorMessage("Valor deve ser positivo.");
        }

        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate dataFormato = LocalDate.parse(dataString, formato);

            this.vendas.add(new CartaoDeVenda(dataFormato, value));
        } catch (DateTimeParseException e) {
            throw new ExceptionErrorMessage("Data invalida.");
        }

    }

    public String getVendasRealizadas(String dataIncial, String dataFinal) throws ExceptionErrorMessage {

        double vendasRealizadas = 0;
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
            return "0,00";
        }

        for (CartaoDeVenda c : this.vendas) {
            if (c.getData().isEqual(dateInit) ||
                    (c.getData().isAfter(dateInit) && c.getData().isBefore(dateEnd))) {
                vendasRealizadas += c.getHoras();
            }
        }

        if (vendasRealizadas == 0) {
            return "0,00";
        }

        return Double.toString(vendasRealizadas).replace(".",",") + "0";
    }

    @Override
    public void setSalario (String salario) {
        this.salarioMensal = salario;
    }

    @Override
    public String getSalario() {
        return salarioMensal;
    }

    @Override
    public String getTipo() {
        return "comissionado";
    }
}
