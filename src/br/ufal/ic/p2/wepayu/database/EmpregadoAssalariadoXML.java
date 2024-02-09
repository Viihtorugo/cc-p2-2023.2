package br.ufal.ic.p2.wepayu.database;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class EmpregadoAssalariadoXML {

    public void save(String id, EmpregadoAssalariado empregado) {

        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream( id + ".xml")))) {

            // Gravar o objeto Java no arquivo XML
            encoder.writeObject(empregado);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
