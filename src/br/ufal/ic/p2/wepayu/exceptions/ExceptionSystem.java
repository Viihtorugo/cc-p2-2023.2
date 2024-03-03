package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionSystem extends Exception{
    public void msgNaoHaComandoDesfazer () throws Exception{
        throw new Exception("Nao ha comando a desfazer.");
    }

    public void msgNaoHaComandoFazer () throws Exception{
        throw new Exception("Nao ha comando a fazer.");
    }

    public void msgNaoPodeDarComandos() throws Exception {
        throw new Exception("Nao pode dar comandos depois de encerrarSistema.");
    }

}
