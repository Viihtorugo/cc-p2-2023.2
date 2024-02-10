package br.ufal.ic.p2.wepayu.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class FolhaDePagamentoController {
    private static int countDay = 1;

    public static void incrementCountDay() {
            countDay++;
    }


    public static boolean ehDiaDoPagamentoComissionado() {

        System.out.println(countDay);

        if (countDay % 15 == 0) return true;

        return false;
    }
}
