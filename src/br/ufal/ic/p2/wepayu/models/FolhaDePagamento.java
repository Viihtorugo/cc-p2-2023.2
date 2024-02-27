package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.FolhaDePagamentoController;
import br.ufal.ic.p2.wepayu.utils.EmpregadoUtils;
import br.ufal.ic.p2.wepayu.utils.FolhaDePagamentoUtils;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.io.FileWriter;

public class FolhaDePagamento {
    private LocalDate dataCriacao;
    private String arquivoSaida;



    public FolhaDePagamento(LocalDate dataCriacao, String arquivoSaida) {
        this.dataCriacao = dataCriacao;
        this.arquivoSaida = arquivoSaida;
    }

    public FolhaDePagamento(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setArquivoSaida(String arquivoSaida) {
        this.arquivoSaida = arquivoSaida;
    }

    public void geraFolha(EmpregadoController empregadoController) throws Exception {
        if (this.arquivoSaida.isEmpty()) throw new Exception("Arquivo de saída não especificado");

        double total = 0;

        try (FileWriter escritor = new FileWriter(this.arquivoSaida)) {
            // Cabeçalho
            escritor.write("FOLHA DE PAGAMENTO DO DIA " + dataCriacao.toString() + '\n');
            escritor.write("====================================");
            escritor.write("\n");
            escritor.write("\n");

            // Dados dos empregados
            total += calculaHoristas(escritor, empregadoController);
            escritor.write("\n");

            try {
                total += calculaAssalariados(escritor, empregadoController);
                escritor.write("\n");
            } catch (Exception error) {
                System.out.println(error.getMessage());
            }



            total += calculaComissionados(escritor, empregadoController);
            escritor.write("\n");


            escritor.write("TOTAL FOLHA: " + Utils.convertDoubleToString(total, 2) + "\n");
            escritor.close();
            System.out.println("Arquivo criado com sucesso!");

        }
         catch (Exception e) {
            // TODO;
        }

    }

    public double calculaAssalariados(FileWriter escritor, EmpregadoController empregadoController) throws Exception {
        if (this.arquivoSaida.isEmpty()) throw new Exception("Arquivo de saída não especificado");

        double totalSalarioBruto = 0;
        double totalDescontos = 0;
        double totalSalarioLiquido = 0;


            FolhaDePagamentoUtils.writeEmpregadoHeader(escritor, "ASSALARIADOS");

        HashMap<String, String> empregados = EmpregadoUtils.sortEmpregadosByName(empregadoController.getEmpregadoAssalariado());
            LocalDate dataInicial = dataCriacao.minusDays(dataCriacao.lengthOfMonth() - 1);

            if(dataCriacao.getDayOfMonth() != dataCriacao.lengthOfMonth()) {
                escritor.write("\n");
                String footer = Utils.padRight("TOTAL ASSALARIADOS", 48);
                footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
                footer += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
                footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16) + "\n";
                escritor.write(footer);

                return totalSalarioBruto;
            }

            for (Map.Entry<String, String> entry: empregados.entrySet()){
                EmpregadoAssalariado e = (EmpregadoAssalariado) empregadoController.getEmpregado(entry.getKey());

                String nome = e.getNome();
                double salarioBruto = e.getSalario();
                salarioBruto = ((int)(salarioBruto*100))/100f;
                double descontos = 0;

                MembroSindicalizado ms = e.getSindicalizado();

                if (ms != null) {
                    descontos = dataCriacao.lengthOfMonth() * ms.getTaxaSindical();

                    descontos += ms.getTaxaServicos(dataInicial, dataCriacao);;
                }

                double salarioLiquido = salarioBruto - descontos;

                if (salarioLiquido <= 0) salarioLiquido = 0;

                totalSalarioBruto += salarioBruto;
                totalSalarioLiquido += salarioLiquido;
                totalDescontos += descontos;

                String metodo = e.getMetodoPagamento().getOutputFile() + ", " + e.getEndereco() ;

                FolhaDePagamentoUtils.writeAssalariado(escritor, nome, salarioBruto, descontos, salarioLiquido, metodo);
            }

        escritor.write("\n");

            String footer = Utils.padRight("TOTAL ASSALARIADOS", 48);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16) + "\n";
            escritor.write(footer);







        return totalSalarioBruto;

    }

    public double calculaHoristas(FileWriter escritor, EmpregadoController empregadoController) throws Exception {
        double totalHorasNormais = 0;
        double totalHorasExtras = 0;
        double totalSalarioBruto = 0;
        double totalDescontos = 0;
        double totalSalarioLiquido = 0;



        FolhaDePagamentoUtils.writeEmpregadoHeader(escritor, "HORISTAS");


        if (dataCriacao.getDayOfWeek() != DayOfWeek.FRIDAY) {
            escritor.write("\n");
            String line = Utils.padRight("TOTAL HORISTAS", 36);
            line += Utils.padLeft(Utils.convertDoubleToString(totalHorasNormais), 6);
            line += Utils.padLeft(Utils.convertDoubleToString(totalHorasExtras), 6);
            line += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
            line += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
            line += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16) + "\n";
            escritor.write(line);

            return totalSalarioBruto;
        };

        HashMap<String, String> empregados = EmpregadoUtils.sortEmpregadosByName(empregadoController.getEmpregadoHoristas());
        LocalDate dataInicial = dataCriacao.minusDays(6);

        for (Map.Entry<String, String> entry: empregados.entrySet()) {
            EmpregadoHorista e = (EmpregadoHorista) empregadoController.getEmpregado(entry.getKey());


            String nome = e.getNome();
            double horaNormais = e.getHorasNormaisTrabalhadas(dataInicial, dataCriacao);
            double horaExtras = e.getHorasExtrasTrabalhadas(dataInicial, dataCriacao);
            double salarioBruto = e.getSalarioBruto(dataInicial, dataCriacao);
            salarioBruto = ((int)(salarioBruto*100))/100f;
            double descontos = 0;

            MembroSindicalizado ms = e.getSindicalizado();

            if (ms != null) {
                descontos += 7 * ms.getTaxaSindical();
                descontos += ms.getTaxaServicos(dataInicial, dataCriacao);
                descontos += e.getDescontos();
            }


            double salarioLiquido = salarioBruto - descontos;

            if (salarioLiquido < 0) {
                e.setDescontos(descontos);
                salarioLiquido = 0;
                descontos = 0;
            }

            totalHorasNormais += horaNormais;
            totalHorasExtras += horaExtras;
            totalSalarioBruto += salarioBruto;
            totalDescontos += descontos;
            totalSalarioLiquido += salarioLiquido;

            empregadoController.setValue(entry.getKey(),e);
            FolhaDePagamentoUtils.writeHorista(escritor, nome, horaNormais, horaExtras, salarioBruto, descontos, salarioLiquido, e.getMetodoPagamento().getOutputFile());
        }

        escritor.write("\n");
        String line = Utils.padRight("TOTAL HORISTAS", 36);
        line += Utils.padLeft(Utils.convertDoubleToString(totalHorasNormais), 6);
        line += Utils.padLeft(Utils.convertDoubleToString(totalHorasExtras), 6);
        line += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
        line += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
        line += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16) + "\n";
        escritor.write(line);


        return totalSalarioBruto;


    }

    public double calculaComissionados(FileWriter escritor, EmpregadoController empregadoController) throws Exception {
        if (this.arquivoSaida.isEmpty()) throw new Exception("Arquivo de saída não especificado");

        double totalSalarioFixo = 0;
        double totalVendas = 0;
        double totalComissao = 0;
        double totalSalarioBruto = 0;
        double totalDescontos = 0;
        double totalSalarioLiquido = 0;

        FolhaDePagamentoUtils.writeEmpregadoHeader(escritor, "COMISSIONADOS");



        boolean multiplo =  (ChronoUnit.DAYS.between(LocalDate.of(2005, 1, 1), dataCriacao) + 1) % 14 == 0;

        boolean deveCalcular = dataCriacao.getDayOfWeek() == DayOfWeek.FRIDAY && multiplo;



        if (!deveCalcular) {
            escritor.write("\n");
            String footer = Utils.padRight("TOTAL COMISSIONADOS", 21);

            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioFixo, 2), 9);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalVendas, 2), 9);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalComissao, 2), 9);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
            footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16) + "\n";
            escritor.write(footer);

            return totalSalarioBruto;
        }



        HashMap<String, String> empregados = EmpregadoUtils.sortEmpregadosByName(empregadoController.getEmpregadoComissionado());

        LocalDate dataInicial = dataCriacao.minusDays(13);

        try {


        for (Map.Entry<String, String> entry: empregados.entrySet()) {
            EmpregadoComissionado e = (EmpregadoComissionado) empregadoController.getEmpregado(entry.getKey());

            String nome = e.getNome();

            double salarioFixo = 0;
            double vendas = 0;
            double comissao = 0;
            double salarioBruto = 0;
            double descontos = 0;

            double salarioLiquido = 0;

            salarioFixo = e.getSalario();
            salarioFixo = Math.floor((salarioFixo*12D/52D)*2D * 100)/100F;

            salarioFixo = ((int)(salarioFixo * 100))/100.0f;
            vendas = e.getVendasRealizadas(dataInicial, dataCriacao);
            comissao = e.getTaxaDeComissao();

            comissao = vendas * comissao;

            comissao = Math.floor(comissao*100)/100F;

            salarioBruto = salarioFixo + comissao;

            MembroSindicalizado m = e.getSindicalizado();

            if (m != null)
            {
                double diasDiff = ChronoUnit.DAYS.between(dataInicial, dataCriacao) + 1;
                descontos = m.getTaxaServicos(dataInicial, dataCriacao) + diasDiff * m.getTaxaSindical();
            }

            if (salarioBruto >= descontos) {
                salarioLiquido = salarioBruto - descontos;
            }

            totalSalarioLiquido += salarioLiquido;
            totalComissao += comissao;
            totalSalarioBruto += salarioBruto;
            totalVendas += vendas;
            totalSalarioFixo += salarioFixo;
            totalDescontos += descontos;


            String metodo = e.getMetodoPagamento().getOutputFile() + ", " + e.getEndereco();

            FolhaDePagamentoUtils.writeComissionado(escritor, nome, salarioFixo, vendas, comissao, salarioBruto, descontos, salarioLiquido, metodo);

        }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        escritor.write("\n");
        String footer = Utils.padRight("TOTAL COMISSIONADOS", 21);

        footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioFixo, 2), 9);
        footer += Utils.padLeft(Utils.convertDoubleToString(totalVendas, 2), 9);
        footer += Utils.padLeft(Utils.convertDoubleToString(totalComissao, 2), 9);
        footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioBruto, 2), 14);
        footer += Utils.padLeft(Utils.convertDoubleToString(totalDescontos, 2), 10);
        footer += Utils.padLeft(Utils.convertDoubleToString(totalSalarioLiquido, 2), 16);


        escritor.write(footer);

        escritor.write("\n");

        return totalSalarioBruto;
    }

    public double totalFolha(EmpregadoController empregadoController) throws Exception {

        FolhaDePagamentoController.incrementCountDay();

        double total = 0;

        HashMap<String, Empregado> empregados = empregadoController.getEmpregados();

        for (Map.Entry<String, Empregado> entry : empregados.entrySet()) {
            Empregado e = entry.getValue();

            // Horista e é sexta-feira
            if (e instanceof EmpregadoHorista && dataCriacao.getDayOfWeek() == DayOfWeek.FRIDAY) {
                LocalDate dataInicial = dataCriacao.minusDays(6);

                double salarioBruto = ((EmpregadoHorista) e).getSalarioBruto(dataInicial, dataCriacao);
                salarioBruto = ((int)(salarioBruto*100))/100f;
                total +=  salarioBruto;


            // Comissionado, sexta-feira e multiplo d
            } else if (e instanceof  EmpregadoComissionado )
                     {

                boolean multiplo =  (ChronoUnit.DAYS.between(LocalDate.of(2005, 1, 1), dataCriacao) + 1) % 14 == 0;

                boolean deveCalcular = dataCriacao.getDayOfWeek() == DayOfWeek.FRIDAY && multiplo;

                if (!deveCalcular) continue;

                LocalDate dataInicial = dataCriacao.minusDays(13);

                double salarioBruto = ((EmpregadoComissionado) e).getSalarioBruto(dataInicial, dataCriacao);


                total += salarioBruto ;
            } else if (e instanceof EmpregadoAssalariado &&
                    dataCriacao.getDayOfMonth() == dataCriacao.lengthOfMonth()) {

                double salarioBruto =  e.getSalario();
                salarioBruto = ((int)(salarioBruto*100))/100f;

                total += salarioBruto;
            }

        }

        return total;
    }

}