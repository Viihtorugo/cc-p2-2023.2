package br.ufal.ic.p2.wepayu.strategy.memento;

import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Banco;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Correios;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.EmMaos;

public class PagamentoMemento {

    public MetodoPagamento copyMetodoPagamento(MetodoPagamento origin) {

        if (origin.getMetodoPagamento().equals("emMaos"))
            return new EmMaos();

        if (origin.getMetodoPagamento().equals("correios"))
            return new Correios();

        if (origin.getMetodoPagamento().equals("banco")) {
            Banco copy = new Banco();

            copy.setBanco(((Banco) origin).getBanco());
            copy.setAgencia(((Banco) origin).getAgencia());
            copy.setContaCorrente(((Banco) origin).getContaCorrente());

            return copy;
        }

        return null;
    }

}
