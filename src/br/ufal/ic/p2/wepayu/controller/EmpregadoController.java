package br.ufal.ic.p2.wepayu.controller;
import br.ufal.ic.p2.wepayu.models.*;

import java.util.HashMap;
import java.util.Map;

public class EmpregadoController {
    public static HashMap<String, Empregado> empregados;
    public static int key;

    public static String setEmpregado(Empregado e) {
        key++;
        empregados.put(Integer.toString(key),e);
        return Integer.toString(key);
    }

    public static Empregado getEmpregado(String key) {
        return empregados.get(key);
    }

    public static HashMap<String, EmpregadoHorista> getEmpregadoHoristas() {

        HashMap <String, EmpregadoHorista> empregadoHoristas = new HashMap<String,EmpregadoHorista>();

        for (Map.Entry<String, Empregado> e: empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("horista")) {
                empregadoHoristas.put(e.getKey(), (EmpregadoHorista) empregado);
            }
        }

        return empregadoHoristas;
    }

    public static HashMap<String, EmpregadoComissionado> getEmpregadoComissionado() {

        HashMap <String, EmpregadoComissionado> empregadoHoristas = new HashMap<String,EmpregadoComissionado>();

        for (Map.Entry<String, Empregado> e: empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("comissionado")) {
                empregadoHoristas.put(e.getKey(), (EmpregadoComissionado) empregado);
            }
        }

        return empregadoHoristas;
    }

    public static HashMap<String, EmpregadoAssalariado> getEmpregadoAssalariado() {

        HashMap <String, EmpregadoAssalariado> empregadoHoristas = new HashMap<String,EmpregadoAssalariado>();

        for (Map.Entry<String, Empregado> e: empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("assalariado")) {
                empregadoHoristas.put(e.getKey(), (EmpregadoAssalariado) empregado);
            }
        }

        return empregadoHoristas;
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
