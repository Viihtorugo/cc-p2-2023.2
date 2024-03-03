package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionEmpregado extends Exception {

    public void msgEmpregadoNaoSindicalizado() throws Exception {
        throw new Exception("Empregado nao eh sindicalizado.");
    }

    public void msgNullIndex() throws Exception {
        throw new Exception("Identificacao do empregado nao pode ser nula.");
    }

    public void msgEmpregadoNotExist() throws Exception {
        throw new Exception("Empregado nao existe.");
    }

}
