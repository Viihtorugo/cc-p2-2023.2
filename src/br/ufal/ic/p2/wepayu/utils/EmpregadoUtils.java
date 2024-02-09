package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;


import java.util.*;

public class EmpregadoUtils {

    public static <T extends Empregado> HashMap<String, String> sortEmpregadosByName(HashMap<String, T> empregados){
        HashMap<String, String> sortedEmpregados = new HashMap<>();

        for (Map.Entry<String, T> entry : empregados.entrySet()) {
            sortedEmpregados.put(entry.getKey(), entry.getValue().getNome());
        }

        List<Map.Entry<String, String>> entryList = new ArrayList<>(sortedEmpregados.entrySet());

        entryList.sort(Map.Entry.comparingByValue());

        HashMap<String, String> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;


    }
}
