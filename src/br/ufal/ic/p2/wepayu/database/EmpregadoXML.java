package br.ufal.ic.p2.wepayu.database;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmpregadoXML {

    public void save(HashMap <String, Empregado> empregados) {

        for (Map.Entry<String, Empregado> m: empregados.entrySet()) {
            String id = m.getKey();
            Empregado e = m.getValue();

            if (e.getTipo().equals("horista")) {
                EmpregadoHoristaXML xml = new EmpregadoHoristaXML();

                xml.save(id, (EmpregadoHorista) e);
            }

            if (e.getTipo().equals("comissionado")) {
                EmpregadoComissionadoXML xml = new EmpregadoComissionadoXML();

                xml.save(id, (EmpregadoComissionado) e);
            }

            if (e.getTipo().equals("assalariado")) {
                EmpregadoAssalariadoXML xml = new EmpregadoAssalariadoXML();

                xml.save(id, (EmpregadoAssalariado) e);
            }
        }
    }

    public HashMap<String, Empregado> readEmpregados() {

        HashMap<String, Empregado> empregados = new HashMap<>();

        for (int i = 1; i <= 1000; i++) {
            String id = Integer.toString(i);

            Empregado empregado = read(id + ".xml");

            if (empregado != null) {
                empregados.put(id, empregado);
            }

        }

        return empregados;
    }


    public Empregado read(String path) {

        File file = new File(path);

        if (file.exists()) {
            try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)))) {
                // Ler o arquivo XML e criar um objeto Java
                Empregado empregado = (Empregado) decoder.readObject();

                return empregado;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
