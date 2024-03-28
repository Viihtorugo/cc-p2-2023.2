package br.ufal.ic.p2.wepayu.strategy.memento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.TaxaServico;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.CartaoDeVenda;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.CartaoDePonto;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;

public class EmpregadoMemento {

    public MembroSindicalizado copyMembroSindicalizado(MembroSindicalizado origin) {

        MembroSindicalizado copy = null;

        if (origin != null) {
            copy = new MembroSindicalizado();
            copy.setIdMembro(origin.getIdMembro());
            copy.setTaxaSindical(origin.getTaxaSindical());

            ArrayList<TaxaServico> copyArrayTaxaServico = new ArrayList<>();

            ArrayList<TaxaServico> originArrayTaxaServico = origin.getTaxaServicos();

            for (TaxaServico t : originArrayTaxaServico) {
                TaxaServico copyTaxaServico = new TaxaServico();

                copyTaxaServico.setData(t.getData());
                copyTaxaServico.setValor(t.getValor());

                copyArrayTaxaServico.add(copyTaxaServico);
            }

            copy.setTaxaServicos(copyArrayTaxaServico);
        }

        return copy;
    }

    public Empregado setAtributosEmpregado(Empregado copy, Empregado origin) {
        PagamentoMemento pgMemento = new PagamentoMemento();
        copy.setNome(origin.getNome());
        copy.setEndereco(origin.getEndereco());
        copy.setSalario(origin.getSalario());

        MembroSindicalizado mCopy = copyMembroSindicalizado(origin.getSindicalizado());

        copy.setSindicalizado(mCopy);

        MetodoPagamento pCopy = pgMemento.copyMetodoPagamento(origin.getMetodoPagamento());

        copy.setMetodoPagamento(pCopy);

        return copy;
    }

    public HashMap<String, Empregado> copyHashEmpregados(HashMap<String, Empregado> empregados) {

        if (empregados == null)
            return null;

        VendaMemento vendaMemento = new VendaMemento();
        CartaoPontoMemento cardPontoMemento = new CartaoPontoMemento();

        HashMap<String, Empregado> hash = new HashMap<String, Empregado>();

        for (Map.Entry<String, Empregado> entry : empregados.entrySet()) {
            String id = entry.getKey();

            if (entry.getValue().getTipo().equals("comissionado")) {
                EmpregadoComissionado origin = (EmpregadoComissionado) entry.getValue();
                EmpregadoComissionado copy = new EmpregadoComissionado();

                copy = (EmpregadoComissionado) setAtributosEmpregado(copy, origin);

                // Comissionado
                copy.setTaxaDeComissao(origin.getTaxaDeComissao());

                ArrayList<CartaoDeVenda> cCopy = vendaMemento.copyArrayCartaoDeVenda(origin.getVendas());
                copy.setVendas(cCopy);

                hash.put(id, copy);
            }

            if (entry.getValue().getTipo().equals("assalariado")) {
                EmpregadoAssalariado origin = (EmpregadoAssalariado) entry.getValue();
                EmpregadoAssalariado copy = new EmpregadoAssalariado();

                copy = (EmpregadoAssalariado) setAtributosEmpregado(copy, origin);

                hash.put(id, copy);
            }

            if (entry.getValue().getTipo().equals("horista")) {
                EmpregadoHorista origin = (EmpregadoHorista) entry.getValue();
                EmpregadoHorista copy = new EmpregadoHorista();

                copy = (EmpregadoHorista) setAtributosEmpregado(copy, origin);

                // Horista

                ArrayList<CartaoDePonto> cCopy = cardPontoMemento.copyArrayCartaoDePonto(origin.getCartao());
                copy.setCartao(cCopy);

                hash.put(id, copy);
            }

        }

        return hash;
    }

}
