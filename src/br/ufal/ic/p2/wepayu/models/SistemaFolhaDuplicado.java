package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.FolhaDePagamentoController;
import br.ufal.ic.p2.wepayu.utils.EmpregadoUtils;
import br.ufal.ic.p2.wepayu.utils.FolhaDePagamento;
import br.ufal.ic.p2.wepayu.utils.Utils;
import util.StringUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class SistemaFolhaDuplicado {
    private HashMap<String, EmpregadoHorista> empregadosHoristas;
    private HashMap<String, EmpregadoComissionado> empregadosComissionados;
    private HashMap<String, EmpregadoAssalariado> empregadosAssalariados;

    private HashMap<String, Double> salarioBrutoHoristas;
    private HashMap<String, Double> salarioBrutoComissionados;
    private HashMap<String, Double> salarioBrutoAssalariados;

    public SistemaFolhaDuplicado(HashMap<String, EmpregadoHorista> empregadosHoristas,
                        HashMap<String, EmpregadoComissionado> empregadosComissionados,
                        HashMap<String, EmpregadoAssalariado> empregadosAssalariados) {

        this.empregadosHoristas = empregadosHoristas;
        this.empregadosComissionados = empregadosComissionados;
        this.empregadosAssalariados = empregadosAssalariados;

        this.salarioBrutoHoristas = new HashMap<String, Double>();
        this.salarioBrutoComissionados = new HashMap<String, Double>();
        this.salarioBrutoAssalariados = new HashMap<String, Double>();
    }

    public double totalFolha(LocalDate data) throws Exception {

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

        for (Map.Entry<String, EmpregadoAssalariado> e : this.empregadosAssalariados.entrySet()) {
            if (data.getDayOfMonth() == data.lengthOfMonth()) {
                EmpregadoAssalariado empregadoAssalariado = e.getValue();
                this.salarioBrutoAssalariados.put(e.getKey(), empregadoAssalariado.getSalario());
            } else {
                this.salarioBrutoAssalariados.put(e.getKey(), 0.0);
            }
        }

        double total = 0;

        for (Map.Entry<String, Double> s : this.salarioBrutoHoristas.entrySet())
            total += s.getValue();

        for (Map.Entry<String, Double> s : this.salarioBrutoComissionados.entrySet())
            total += s.getValue();

        for (Map.Entry<String, Double> s : this.salarioBrutoAssalariados.entrySet())
            total += s.getValue();

        FolhaDePagamentoController.restartCountFriday(data);

        return total;
    }

    public void fileFolha(LocalDate data, String saida, double total) {

        try {

            FileWriter escritor = new FileWriter(saida);

            escritor.write("FOLHA DE PAGAMENTO DO DIA " + data.toString());
            escritor.write("\n====================================\n\n");
            FolhaDePagamento.writeEmpregadoHeader(escritor, "HORISTAS");


            double totalHorasNormais = 0;
            double totalHorasExtras = 0;
            double totalSalarioBruto = 0;
            double totalDescontos = 0;
            double totalSalarioLiquido = 0;

            //inicio cada empregado horista
            if (data.getDayOfWeek() == DayOfWeek.FRIDAY) {

                LocalDate dataInicial = data.minusDays(6);

                HashMap<String, String> sortEmpregados = EmpregadoUtils.sortEmpregadosByName(empregadosHoristas);


                for (Map.Entry<String, String> entry : sortEmpregados.entrySet()) {

                    EmpregadoHorista e = empregadosHoristas.get(entry.getKey());

                    String nome = e.getNome();
                    double horaNormais = e.getHorasNormaisTrabalhadas(dataInicial, data);
                    double horaExtras = e.getHorasExtrasTrabalhadas(dataInicial, data);
                    double salarioBruto = this.salarioBrutoHoristas.get(entry.getKey());
                    double desconto = 0;


                    MembroSindicalizado m = e.getSindicalizado();

                    if (m != null) {
                        desconto = 7 * m.getTaxaSindical();

                        double taxaServicos = m.getTaxaServicos(dataInicial, data);

                        desconto += taxaServicos;
                    }


                    double salarioLiquido = salarioBruto - desconto;

                    totalHorasNormais += horaNormais;
                    totalHorasExtras += horaExtras;
                    totalSalarioBruto += salarioBruto;
                    totalDescontos += desconto;
                    totalSalarioLiquido += salarioLiquido;

                    FolhaDePagamento.writeHorista(escritor, nome, horaNormais, horaExtras, salarioBruto, desconto, salarioLiquido, e.getMetodoPagamento().getMetodoPagamento());

                } //fim for


            }

            escritor.write("\n");

            //TOTAL HORISTAS
            String line = Utils.padRight("TOTAL HORISTAS", 36);
            line += Utils.padLeft(Utils.convertDoubleToString(totalHorasNormais), 6);
            line += Utils.padLeft(Utils.convertDoubleToString(totalHorasExtras), 6);
            line += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
            line += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
            line += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16) + "\n";
            escritor.write(line);

            escritor.write("\n");

            FolhaDePagamento.writeEmpregadoHeader(escritor, "ASSALARIADOS");

            totalHorasNormais = 0;
            totalHorasExtras = 0;
            totalSalarioBruto = 0;
            totalDescontos = 0;
            totalSalarioLiquido = 0;

            if (data.getDayOfMonth() == data.lengthOfMonth()) {

                LocalDate dataInicial = data.minusDays(data.lengthOfMonth() - 1);
                System.out.println(dataInicial);

                HashMap<String, String> sortEmpregadosAssalariados = EmpregadoUtils.sortEmpregadosByName(empregadosAssalariados);

                for (Map.Entry<String, String> entry : sortEmpregadosAssalariados.entrySet()) {
                    EmpregadoAssalariado e = (EmpregadoAssalariado) EmpregadoController.getEmpregado(entry.getKey());

                    String nome = e.getNome();
                    double salarioBruto = e.getSalario();
                    double desconto = 0;
                    MembroSindicalizado m = e.getSindicalizado();

                    if (m != null) {
                        desconto = data.lengthOfMonth() * m.getTaxaSindical();

                        double taxaServicos = m.getTaxaServicos(dataInicial, data);

                        desconto += taxaServicos;
                    }

                    double salarioLiquido = 0;

                    if (salarioBruto >= desconto)
                        salarioLiquido = salarioBruto - desconto;

                    if (salarioLiquido <= 0) salarioLiquido = 0;
                    String metodo = e.getMetodoPagamento().getMetodoPagamento() + ", " + e.getEndereco() ;


                    FolhaDePagamento.writeAssalariado(escritor, nome, salarioBruto, desconto, salarioLiquido, metodo);
                }

                escritor.write("\n");
            }

            escritor.write("\n");

            //TOTAL ASSALARIADO
            String footer = Utils.padRight("TOTAL ASSALARIADOS", 48);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16) + "\n";
            escritor.write(footer);

            escritor.write("\n");

            FolhaDePagamento.writeEmpregadoHeader(escritor, "COMISSIONADOS");

            double totalSalarioFixo = 0;
            double totalVendas = 0;
            double totalComissao = 0;
            totalSalarioBruto = 0;
            totalDescontos = 0;
            totalSalarioLiquido = 0;

            if (FolhaDePagamentoController.pagamentoComissionado()) {

                LocalDate dataInicial = data.minusDays(13);

                HashMap<String, String> sortEmpregadosComissionados = EmpregadoUtils.sortEmpregadosByName(empregadosComissionados);

                for (Map.Entry<String, String> entry : sortEmpregadosComissionados.entrySet()) {
                    EmpregadoComissionado e = (EmpregadoComissionado) EmpregadoController.getEmpregado(entry.getKey());

                    String nome = e.getNome();
                }


            }

            escritor.write("\n");

            //TOTAL COMISSIONADOS
            footer = Utils.padRight("TOTAL COMISSIONADOS", 21);

            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioFixo, 2), 9);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalVendas, 2), 9);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalComissao, 2), 9);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16) + "\n";

            escritor.write(footer);

            escritor.write("\n");

            escritor.write("TOTAL FOLHA: " + Utils.convertDoubleToString(total, 2) + "\n");

            // Fechar o escritor
            escritor.close();
            System.out.println("Arquivo criado com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}