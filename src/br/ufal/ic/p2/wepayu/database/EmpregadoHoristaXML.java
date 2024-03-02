package br.ufal.ic.p2.wepayu.database;

import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EmpregadoHoristaXML {
    public void save(String id, EmpregadoHorista empregado) {

        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(id + ".xml")))) {

            // Gravar o objeto Java no arquivo XML
            encoder.writeObject(empregado);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
