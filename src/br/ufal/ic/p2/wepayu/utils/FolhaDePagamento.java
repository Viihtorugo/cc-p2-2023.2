package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;

import java.io.FileWriter;

public class FolhaDePagamento {
    public static void writeEmpregadoHeader(FileWriter writter, String type) {
        try {

            writter.write("=".repeat(127) + "\n");
            writter.write(Utils.padRight(String.format("===================== %s ", type), 127, "=") + "\n");
            writter.write("=".repeat(127) + "\n");
            if (type.equals("HORISTAS")) {
                writter.write("Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo\n");
                writter.write("==================================== ===== ===== ============= ========= =============== ======================================" + "\n");
            }

            if (type.equals("ASSALARIADOS")) {
                writter.write("Nome                                             Salario Bruto Descontos Salario Liquido Metodo\n");
                writter.write("================================================ ============= ========= =============== ======================================" + "\n");
            }

            if (type.equals("COMISSIONADOS")) {
                writter.write("Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo\n");
                writter.write("===================== ======== ======== ======== ============= ========= =============== ======================================\n");
            }

        } catch (Exception e) {
            System.out.println("Erro ao criar arquivo");
        }

    }

    public static void writeHorista(FileWriter writter, String nome, double horas, double extras, double bruto, double descontos, double liquido, String metodo) {
        String line = Utils.padRight(nome, 36);

        line += Utils.padLeft(Utils.convertDoubleToString(horas), 6);
        line += Utils.padLeft(Utils.convertDoubleToString(extras), 6);
        line += Utils.padLeft(Utils.convertDoubleToString(bruto, 2), 14);
        line += Utils.padLeft(Utils.convertDoubleToString(descontos, 2), 10);
        line += Utils.padLeft(Utils.convertDoubleToString(liquido, 2), 16);
        line += " " + metodo + "\n";

        try {
            writter.write(line);
        } catch (Exception e) {
            System.out.println("Erro durante a escrita do funcionário");
        }

    }

    public static void writeAssalariado(FileWriter writter, String nome, double bruto, double descontos, double liquido, String metodo ) {
        String line = Utils.padRight(nome, 48);


        line += Utils.padLeft(Utils.convertDoubleToString(bruto, 2), 14);
        line += Utils.padLeft(Utils.convertDoubleToString(descontos, 2), 10);
        line += Utils.padLeft(Utils.convertDoubleToString(liquido, 2), 16);
        line += " " + metodo + "\n";

        try {
            writter.write(line);
        } catch (Exception e) {
            System.out.println("Erro durante a escrita do funcionário");
        }

    }


}
