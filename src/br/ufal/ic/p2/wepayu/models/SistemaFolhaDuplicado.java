package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.FolhaDePagamentoController;
import br.ufal.ic.p2.wepayu.utils.EmpregadoUtils;
import br.ufal.ic.p2.wepayu.utils.FolhaDePagamentoUtils;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class SistemaFolhaDuplicado {
    private HashMap<String, EmpregadoHorista> empregadosHoristas;
    private HashMap<String, EmpregadoComissionado> empregadosComissionados;
    private HashMap<String, EmpregadoAssalariado> empregadosAssalariados;

    private HashMap<String, Double> salarioBrutoHoristas;
    private HashMap<String, Double> salarioBrutoComissionados;
    private HashMap<String, Double> salarioBrutoAssalariados;

    public SistemaFolhaDuplicado(HashMap<String, EmpregadoHorista> empregadosHoristas,
                        HashMap<String, EmpregadoComissionado> empregadosComissionados,
                        HashMap<String, EmpregadoAssalariado> empregadosAssalariados) {

        this.empregadosHoristas = empregadosHoristas;
        this.empregadosComissionados = empregadosComissionados;
        this.empregadosAssalariados = empregadosAssalariados;

        this.salarioBrutoHoristas = new HashMap<String, Double>();
        this.salarioBrutoComissionados = new HashMap<String, Double>();
        this.salarioBrutoAssalariados = new HashMap<String, Double>();
    }



}