package br.ufal.ic.p2.wepayu.controller;
import br.ufal.ic.p2.wepayu.models.Empregado;
import java.util.HashMap;

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
}
