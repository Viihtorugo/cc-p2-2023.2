package br.ufal.ic.p2.wepayu.controller;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.TaxaServico;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Banco;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Correios;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.EmMaos;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.CartaoDeVenda;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.CartaoDePonto;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmpregadoController {
    private HashMap<String, Empregado> empregados;
    private int key;

    public EmpregadoController() {
        this.empregados = new HashMap<>();
        this.key = 0;
    }

    public HashMap<String, Empregado> getEmpregados() {
        return this.empregados;
    }
    public void setEmpregados(HashMap<String, Empregado> empregados) {

        for (Map.Entry<String, Empregado> entry : this.empregados.entrySet()) {
            String key = entry.getKey();

            int newKey = Integer.parseInt(key);

            if (newKey > this.key)
                setKey(newKey);
        }

        this.empregados = empregados;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String setEmpregado(Empregado e) {
        if (e != null) {
            this.key++;
            String id = Integer.toString(key);
            this.empregados.put(id, e);
            return id;
        }

        return null;
    }

    public void removeEmpregado(String key) {
        this.empregados.remove(key);
    }

    public Empregado getEmpregado(String key) {
        return empregados.get(key);
    }

    public int getNumeroDeEmpregados() {
        if (this.empregados == null)
            return 0;

        return this.empregados.size();
    }

    public HashMap<String, EmpregadoHorista> getEmpregadoHoristas() {

        HashMap<String, EmpregadoHorista> empregadoHoristas = new HashMap<String, EmpregadoHorista>();

        for (Map.Entry<String, Empregado> e : empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("horista")) {
                empregadoHoristas.put(e.getKey(), (EmpregadoHorista) empregado);
            }
        }

        return empregadoHoristas;
    }

    public HashMap<String, EmpregadoComissionado> getEmpregadoComissionado() {

        HashMap<String, EmpregadoComissionado> empregadoComissionado = new HashMap<String, EmpregadoComissionado>();

        for (Map.Entry<String, Empregado> e : empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("comissionado")) {
                empregadoComissionado.put(e.getKey(), (EmpregadoComissionado) empregado);
            }
        }

        return empregadoComissionado;
    }

    public HashMap<String, EmpregadoAssalariado> getEmpregadoAssalariado() {

        HashMap<String, EmpregadoAssalariado> empregadoAssalariado = new HashMap<String, EmpregadoAssalariado>();

        for (Map.Entry<String, Empregado> e : empregados.entrySet()) {

            Empregado empregado = e.getValue();

            if (empregado.getTipo().equals("assalariado")) {
                empregadoAssalariado.put(e.getKey(), (EmpregadoAssalariado) empregado);
            }
        }

        return empregadoAssalariado;
    }

    public String getEmpregadoPorIdSindical(String idSindical) {

        for (Map.Entry<String, Empregado> entry : this.empregados.entrySet()) {
            Empregado e = entry.getValue();

            MembroSindicalizado sindicalizado = e.getSindicalizado();

            if (sindicalizado != null) {
                if (idSindical.equals(sindicalizado.getIdMembro()))
                    return entry.getKey();
            }
        }

        return null;
    }

    public void setValue(String key, Empregado e) {
        empregados.replace(key, e);
    }

    private MembroSindicalizado copyMembroSindicalizado(MembroSindicalizado origin) {

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

    private MetodoPagamento copyMetodoPagamento(MetodoPagamento origin) {

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

    private ArrayList<CartaoDeVenda> copyArrayCartaoDeVenda (ArrayList<CartaoDeVenda> origin) {
        ArrayList<CartaoDeVenda> copy = new ArrayList<>();

        for (CartaoDeVenda c : origin) {
            CartaoDeVenda copyCartaoDeVenda = new CartaoDeVenda();

            copyCartaoDeVenda.setData(c.getData());
            copyCartaoDeVenda.setValor(c.getValor());

            copy.add(copyCartaoDeVenda);
        }

        return  copy;
    }

    private ArrayList<CartaoDePonto> copyArrayCartaoDePonto (ArrayList<CartaoDePonto> origin) {
        ArrayList<CartaoDePonto> copy = new ArrayList<>();

        for (CartaoDePonto c : origin) {
            CartaoDePonto copyCartaoDePonto = new CartaoDePonto();

            copyCartaoDePonto.setData(c.getData());
            copyCartaoDePonto.setHoras(c.getHoras());

            copy.add(copyCartaoDePonto);
        }

        return  copy;
    }

    private Empregado setAtributosEmpregado(Empregado copy, Empregado origin) {
        copy.setNome(origin.getNome());
        copy.setEndereco(origin.getEndereco());
        copy.setSalario(origin.getSalario());

        MembroSindicalizado mCopy = copyMembroSindicalizado(origin.getSindicalizado());

        copy.setSindicalizado(mCopy);

        MetodoPagamento pCopy = copyMetodoPagamento(origin.getMetodoPagamento());

        copy.setMetodoPagamento(pCopy);

        return copy;
    }

    public HashMap<String, Empregado> copyHashEmpregados() {

        if (this.empregados == null)
            return null;

        HashMap<String, Empregado> hash = new HashMap<String, Empregado>();

        for (Map.Entry<String, Empregado> entry : this.empregados.entrySet()) {
            String id = entry.getKey();

            if (entry.getValue().getTipo().equals("comissionado")) {
                EmpregadoComissionado origin = (EmpregadoComissionado) entry.getValue();
                EmpregadoComissionado copy = new EmpregadoComissionado();

                copy = (EmpregadoComissionado) setAtributosEmpregado(copy, origin);

                //Comissionado
                copy.setTaxaDeComissao(origin.getTaxaDeComissao());

                ArrayList<CartaoDeVenda> cCopy = copyArrayCartaoDeVenda(origin.getVendas());
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

                //Horista

                ArrayList<CartaoDePonto> cCopy = copyArrayCartaoDePonto(origin.getCartao());
                copy.setCartao(cCopy);

                hash.put(id, copy);
            }

        }

        return hash;
    }

}
