package br.ufal.ic.p2.wepayu.strategy.memento;

import java.util.ArrayList;

import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.CartaoDeVenda;

public class VendaMemento {

    public ArrayList<CartaoDeVenda> copyArrayCartaoDeVenda(ArrayList<CartaoDeVenda> origin) {
        ArrayList<CartaoDeVenda> copy = new ArrayList<>();

        for (CartaoDeVenda c : origin) {
            CartaoDeVenda copyCartaoDeVenda = new CartaoDeVenda();

            copyCartaoDeVenda.setData(c.getData());
            copyCartaoDeVenda.setValor(c.getValor());

            copy.add(copyCartaoDeVenda);
        }

        return copy;
    }
}
