package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.controller.FolhaDePagamentoController;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;

public class SistemaFolha {
    private HashMap<String, EmpregadoHorista> empregadosHoristas;
    private HashMap<String, EmpregadoComissionado> empregadosComissionados;
    private HashMap<String, EmpregadoAssalariado> empregadosAssalariados;

    private HashMap<String, Double> salarioBrutoHoristas;
    private HashMap<String, Double> salarioBrutoComissionados;
    private HashMap<String, Double> salarioBrutoAssalariados;

    public SistemaFolha(HashMap<String, EmpregadoHorista> empregadosHoristas,
                        HashMap<String, EmpregadoComissionado> empregadosComissionados,
                        HashMap<String, EmpregadoAssalariado> empregadosAssalariados) {

        this.empregadosHoristas = empregadosHoristas;
        this.empregadosComissionados = empregadosComissionados;
        this.empregadosAssalariados = empregadosAssalariados;

        this.salarioBrutoHoristas = new HashMap<String, Double>();
        this.salarioBrutoComissionados = new HashMap<String, Double>();
        this.salarioBrutoAssalariados = new HashMap<String, Double>();
    }

    public void rodaFolha(LocalDate data) throws Exception {

        for (Map.Entry<String, EmpregadoHorista> e : this.empregadosHoristas.entrySet()) {

            EmpregadoHorista empregadoHorista = e.getValue();

            if (data.getDayOfWeek() != DayOfWeek.FRIDAY) {
                this.salarioBrutoHoristas.put(e.getKey(), 0.0);
            } else {
                LocalDate dataInicial = data.minusDays(6);

                double horasNormaisAcumuladas = empregadoHorista.getHorasNormaisTrabalhadas(dataInicial, data);
                double horasExtrasAcumuladas = empregadoHorista.getHorasExtrasTrabalhadas(dataInicial, data);

                double total = horasNormaisAcumuladas * empregadoHorista.getSalario();
                total += (1.5 * horasExtrasAcumuladas * empregadoHorista.getSalario());

                this.salarioBrutoHoristas.put(e.getKey(), total);
            }
        }


        FolhaDePagamentoController.incrementCountFriday(data);

        for (Map.Entry<String, EmpregadoComissionado> e : this.empregadosComissionados.entrySet()) {
            EmpregadoComissionado empregadoComissionado = e.getValue();

            if (FolhaDePagamentoController.pagamentoComissionado()) {

                LocalDate dataInicial = data.minusDays(13);

                double salarioFixo = ((empregadoComissionado.getSalario() * 12.0) / 52.0) * 2.0;
                double comissao = empregadoComissionado.getVendasRealizadas(dataInicial, data) * empregadoComissionado.getTaxaDeComissao();

                String salarioFixoString = Double.toString(salarioFixo);
                String comissaoString = Double.toString(comissao);

                salarioFixo = Utils.convertDoubleToStringFormattPagamento(salarioFixoString);
                comissao = Utils.convertDoubleToStringFormattPagamento(comissaoString);

                salarioBrutoComissionados.put(e.getKey(), salarioFixo + comissao);
            } else {
                salarioBrutoComissionados.put(e.getKey(), 0.0);
            }
        }

        FolhaDePagamentoController.restartCountFriday(data);

        for (Map.Entry<String, EmpregadoAssalariado> e : this.empregadosAssalariados.entrySet()) {
            if (data.getDayOfMonth() == data.lengthOfMonth()) {
                EmpregadoAssalariado empregadoAssalariado = e.getValue();
                this.salarioBrutoAssalariados.put(e.getKey(), empregadoAssalariado.getSalario());
            } else {
                this.salarioBrutoAssalariados.put(e.getKey(), 0.0);
            }
        }


        if (data.getDayOfWeek() == DayOfWeek.FRIDAY || data.getDayOfMonth() == data.lengthOfMonth()) {
            String dataString = data.toString();

            try {
                FileWriter escritor = new FileWriter("folha-" + dataString + ".txt");

                escritor.write("FOLHA DE PAGAMENTO DO DIA " + dataString);

                // Fechar o escritor
                escritor.close();

                System.out.println("Arquivo criado com sucesso!");

            } catch (IOException e) {
                System.out.println("Erro ao criar o arquivo: " + e.getMessage());
            }
        }


    }

    public double totalFolhaSalarioBruto() {
        double total = 0;

        for (Map.Entry<String, Double> s : this.salarioBrutoHoristas.entrySet())
            total += s.getValue();

        for (Map.Entry<String, Double> s : this.salarioBrutoComissionados.entrySet())
            total += s.getValue();

        for (Map.Entry<String, Double> s : this.salarioBrutoAssalariados.entrySet())
            total += s.getValue();

        return total;
    }

}