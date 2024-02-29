package br.ufal.ic.p2.wepayu.database;

import br.ufal.ic.p2.wepayu.controller.FolhaDePagamentoController;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class FolhaDePagamentoXML {
    public void saveFolha(FolhaDePagamentoController folhaDePagamentoController) {

        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("folha-de-pagamento.xml")))) {
            // Gravar o objeto Java no arquivo XML
            encoder.writeObject(folhaDePagamentoController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FolhaDePagamentoController readFolha() {

        String path = "folha-de-pagamento.xml";

        File file = new File(path);

        if (file.exists()) {
            try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)))) {
                // Ler o arquivo XML e criar um objeto Java
                FolhaDePagamentoController folha = (FolhaDePagamentoController) decoder.readObject();

                return folha;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
