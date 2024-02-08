package br.ufal.ic.p2.wepayu.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class FolhaDePagamentoController {
    private static int countFriday = 1;

    public static void incrementCountFriday(LocalDate data) {

        if (data.getDayOfWeek() == DayOfWeek.FRIDAY) {
            countFriday++;
        }
    }

    public static void restartCountFriday(LocalDate data) {
        if (countFriday % 3 == 0 && data.getDayOfWeek() == DayOfWeek.FRIDAY) {
            countFriday = 1;
        }
    }

    public static boolean pagamentoComissionado() {
        if (countFriday % 3 == 0) {
            return true;
        }

        return false;
    }
}
