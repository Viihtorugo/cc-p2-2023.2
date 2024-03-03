package br.ufal.ic.p2.wepayu.exceptions;

public class ExceptionEmpregado extends Exception {

    public void msgEmpregadoNaoSindicalizado() throws Exception {
        throw new Exception("Empregado nao eh sindicalizado.");
    }

    public void msgDataInicialPosteriorDataFinal() throws Exception {
        throw new Exception("Data inicial nao pode ser posterior aa data final.");
    }

    public void msgIdNaoPodeSerNulo() throws Exception {
        throw new Exception("Identificacao do membro nao pode ser nula.");
    }

    public void msgMembroNaoExiste() throws Exception {
        throw new Exception("Membro nao existe.");
    }

    public void msgNullIndex() throws Exception {
        throw new Exception("Identificacao do empregado nao pode ser nula.");
    }

    public void msgEmpregadoNotExist() throws Exception {
        throw new Exception("Empregado nao existe.");
    }

}
