package br.ufal.ic.p2.wepayu.controller;

import br.ufal.ic.p2.wepayu.database.EmpregadoXML;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.utils.Utils;

public class FacadeController {

    public FacadeController () {
        EmpregadoXML xml = new EmpregadoXML();
        EmpregadoController.empregados = xml.readEmpregados();
    }
    public void cleanSystem() {
        Utils.initSystem();
        Utils.deleteFilesXML();
        Utils.deleteFolhas();
        System.out.println("-> Sistema zerado");
    }

    public void shutDownSystem () {
        System.out.println("-> Sistema encerrado");

        EmpregadoXML xml = new EmpregadoXML();
        xml.save(EmpregadoController.empregados);
    }

    //4 variaveis
    public String createEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        if (!Utils.validCriarEmpregado(nome, endereco, tipo, salario))
            return null;

        if (!Utils.validTipoNotComissionado(tipo))
            return null;

        double salarioFormato = Utils.validSalario(salario);

        if (salarioFormato <= 0)
            return null;

        if (tipo.equals("assalariado")) {
            return EmpregadoController.setEmpregado(new EmpregadoAssalariado(nome, endereco, salarioFormato));
        } else if (tipo.equals("horista")) {
            return EmpregadoController.setEmpregado(new EmpregadoHorista(nome, endereco, salarioFormato));
        }

        return null;
    }
}
