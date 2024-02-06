package br.ufal.ic.p2.wepayu.controller;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.MembroSindicalizado;

import java.util.HashMap;
import java.util.Map;

public class EmpregadoController {
    public static HashMap<String, Empregado> empregados;
    public static int key = 0;

    public static String setEmpregado(Empregado e) {
        key++;
        empregados.put(Integer.toString(key),e);
        return Integer.toString(key);
    }

    public static Empregado getEmpregado(String key) {
        return empregados.get(key);
    }

    public static String getEmpregadoPorIdSindical (String idSindical) {

        for (Map.Entry<String, Empregado> entry : EmpregadoController.empregados.entrySet()) {
            Empregado e = entry.getValue();

            MembroSindicalizado sindicalizado = e.getSindicalizado();

            if (sindicalizado != null) {
                if (idSindical.equals(sindicalizado.getIdMembro()))
                    return entry.getKey();
            }
        }

        return null;
    }

    public static void setValue(String key, Empregado e) {
        empregados.replace(key, e);
    }


}
