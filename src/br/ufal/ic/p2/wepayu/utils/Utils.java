package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionConversao;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static boolean validCriarAgendaDePagamentos(String descricao) throws Exception {

        String[] d = descricao.split(" ");

        if (d[0].equals("semanal")) {
            if (d.length == 2) {

                int value = 0;
                try {
                    value = Integer.parseInt(d[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Valor não é numero inteiro!");
                    return false;
                }

                if (value <= 0 || value >= 8) {
                    ExceptionConversao e = new ExceptionConversao();
                    e.msgDescricaoDeAgendaInvalida();
                    return false;
                }

            } else if (d.length == 3) {
                int v1 = 0, v2 = 0;

                try {
                    v1 = Integer.parseInt(d[1]);
                    v2 = Integer.parseInt(d[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Valor(es) não é(são) numero(s) inteiro(s)!");
                    return false;
                }

                if (v1 <= 0 || v1 >= 53 || v2 <= 0 || v2 >= 8) {
                    ExceptionConversao e = new ExceptionConversao();
                    e.msgDescricaoDeAgendaInvalida();
                    return false;
                }

            } else {
                ExceptionConversao e = new ExceptionConversao();
                e.msgDescricaoDeAgendaInvalida();

                return false;
            }

        } else if (d[0].equals("mensal")) {
            if (d.length == 2) {

                int value = 0;

                try {
                    value = Integer.parseInt(d[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Valor não é numero inteiro!");
                    return false;
                }

                if (value <= 0 || value >= 29) {
                    ExceptionConversao e = new ExceptionConversao();
                    e.msgDescricaoDeAgendaInvalida();
                    return false;
                }


            } else {
                ExceptionConversao e = new ExceptionConversao();
                e.msgDescricaoDeAgendaInvalida();

                return false;
            }

        } else {
            ExceptionConversao e = new ExceptionConversao();
            e.msgDescricaoDeAgendaInvalida();

            return false;
        }

        return true;
    }

    public static void deleteFilesXML() {

        File[] folhas = new File("./").listFiles();

        for (File f : folhas) {
            if (f.getName().endsWith(".xml")) {
                // Excluir o arquivo
                f.delete();
            }
        }

    }

    public static void deleteFolhas() {
        File[] folhas = new File("./").listFiles();

        for (File f : folhas) {
            if (f.getName().endsWith(".txt")) {
                // Excluir o arquivo
                f.delete();
            }
        }
    }

    public static LocalDate validData(String data, String tipo) throws Exception {

        String[] blocos = data.split("/");

        int d = Integer.parseInt(blocos[0]);
        int m = Integer.parseInt(blocos[1]);
        int y = Integer.parseInt(blocos[2]);

        if (m > 12 || m < 1) {
            ExceptionConversao ex = new ExceptionConversao();
            ex.msgDataInvalida(tipo);
            return null;
        }

        YearMonth yearMonth = YearMonth.of(y, m);

        if (d > yearMonth.lengthOfMonth()) {
            ExceptionConversao ex = new ExceptionConversao();
            ex.msgDataInvalida(tipo);
            return null;
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(data, formato);
    }

    public static String convertDoubleToString(double value) {
        if (value != (int) value) return Double.toString(value).replace('.', ',');
        else return Integer.toString((int) value);
    }

    public static String convertDoubleToString(double value, int decimalPlaces) {
        return String.format(("%." + decimalPlaces + "f"), value).replace(".", ",");
    }

    public static double convertStringToDouble(String value) {
        return Double.parseDouble(value.replace(",", "."));
    }

    public static String padLeft(String value, int length) {

        if (value.length() >= length) {
            return value;
        }

        int padLength = length - value.length();

        return " ".repeat(padLength) + value;
    }

    public static String padRight(String value, int length) {

        if (value.length() >= length) {
            return value;
        }

        int padLength = length - value.length();

        return value + " ".repeat(padLength);
    }

    public static String padRight(String value, int length, String padChar) {

        if (value.length() >= length) {
            return value;
        }

        int padLength = length - value.length();

        return value + padChar.repeat(padLength);
    }

    public static LocalDate convertStringToLocalDate(String data) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(data, formato);
    }
}
