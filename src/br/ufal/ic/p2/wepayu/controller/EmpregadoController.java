package br.ufal.ic.p2.wepayu.controller;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.strategy.memento.EmpregadoMemento;

import java.util.HashMap;
import java.util.Map;

public class EmpregadoController {
    private HashMap<String, Empregado> empregados;
    private int key;

    public EmpregadoController() {
        this.empregados = new HashMap<>();
        this.key = 0;
    }

    public HashMap<String, Empregado> getEmpregados() {
        return this.empregados;
    }

    public void setEmpregados(HashMap<String, Empregado> empregados) {

        for (Map.Entry<String, Empregado> entry : this.empregados.entrySet()) {
            String key = entry.getKey();

            int newKey = Integer.parseInt(key);

            if (newKey > this.key)
                setKey(newKey);
        }

        this.empregados = empregados;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String setEmpregado(Empregado e) {
        if (e != null) {
            this.key++;
            String id = Integer.toString(key);
            this.empregados.put(id, e);
            return id;
        }

        return null;
    }

    public void removeEmpregado(String key) {
        this.empregados.remove(key);
    }

    public Empregado getEmpregado(String key) {
        return empregados.get(key);
    }

    public int getNumeroDeEmpregados() {
        if (this.empregados == null)
            return 0;

        return this.empregados.size();
    }

    public HashMap<String, EmpregadoHorista> getEmpregadoHoristas() {

        HashMap<String, EmpregadoHorista> empregadoHoristas = new HashMap<String, EmpregadoHorista>();

        for (Map.Entry<String, Empregado> e : empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("horista")) {
                empregadoHoristas.put(e.getKey(), (EmpregadoHorista) empregado);
            }
        }

        return empregadoHoristas;
    }

    public HashMap<String, EmpregadoComissionado> getEmpregadoComissionado() {

        HashMap<String, EmpregadoComissionado> empregadoComissionado = new HashMap<String, EmpregadoComissionado>();

        for (Map.Entry<String, Empregado> e : empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("comissionado")) {
                empregadoComissionado.put(e.getKey(), (EmpregadoComissionado) empregado);
            }
        }

        return empregadoComissionado;
    }

    public HashMap<String, EmpregadoAssalariado> getEmpregadoAssalariado() {

        HashMap<String, EmpregadoAssalariado> empregadoAssalariado = new HashMap<String, EmpregadoAssalariado>();

        for (Map.Entry<String, Empregado> e : empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("assalariado")) {
                empregadoAssalariado.put(e.getKey(), (EmpregadoAssalariado) empregado);
            }
        }

        return empregadoAssalariado;
    }

    public String getEmpregadoPorIdSindical(String idSindical) {

        for (Map.Entry<String, Empregado> entry : this.empregados.entrySet()) {
            Empregado e = entry.getValue();

            MembroSindicalizado sindicalizado = e.getSindicalizado();

            if (sindicalizado != null) {
                if (idSindical.equals(sindicalizado.getIdMembro()))
                    return entry.getKey();
            }
        }

        return null;
    }

    public void setValue(String key, Empregado e) {
        empregados.replace(key, e);
    }

    public HashMap<String, Empregado> copyHashEmpregados() {
        EmpregadoMemento empregadoMemento = new EmpregadoMemento();
        return empregadoMemento.copyHashEmpregados(empregados);
    }
}
