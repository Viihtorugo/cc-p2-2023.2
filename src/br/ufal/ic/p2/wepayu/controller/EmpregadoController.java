package br.ufal.ic.p2.wepayu.controller;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.MembroSindicalizado;

import java.util.HashMap;
import java.util.Map;

public class EmpregadoController {
    public static HashMap<String, Empregado> empregados;
    public static int size = 0;

    public static String setEmpregado(Empregado e) {
        size++;
        empregados.put(Integer.toString(size),e);
        return Integer.toString(size);
    }

    public static Empregado getEmpregado(String index) {
        return empregados.get(index);
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

}
