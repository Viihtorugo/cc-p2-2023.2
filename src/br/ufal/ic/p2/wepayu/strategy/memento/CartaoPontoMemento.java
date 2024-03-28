package br.ufal.ic.p2.wepayu.strategy.memento;

import java.util.ArrayList;

import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.CartaoDePonto;

public class CartaoPontoMemento {

    public ArrayList<CartaoDePonto> copyArrayCartaoDePonto(ArrayList<CartaoDePonto> origin) {
        ArrayList<CartaoDePonto> copy = new ArrayList<>();

        for (CartaoDePonto c : origin) {
            CartaoDePonto copyCartaoDePonto = new CartaoDePonto();

            copyCartaoDePonto.setData(c.getData());
            copyCartaoDePonto.setHoras(c.getHoras());

            copy.add(copyCartaoDePonto);
        }

        return copy;
    }
}
