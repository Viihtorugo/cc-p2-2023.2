package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;

import java.time.LocalDate;
import java.util.ArrayList;

public class EmpregadoHorista extends Empregado {

    private double salarioPorHora;
    private ArrayList<CartaoDePonto> cartao;

    public EmpregadoHorista(String nome, String endereco, double salarioPorHora) {
        super(nome, endereco);
        this.salarioPorHora = salarioPorHora;
        this.cartao = new ArrayList<CartaoDePonto>();
    }

    public boolean checkDateCartaoDePonto (LocalDate date) {
        for (int i = 0; i < this.cartao.size(); i++)
           if (date.isEqual(this.cartao.get(i).getData()))
               return true;

        return false;
    }

    public void removeCartaoDePonto (int id) {
        this.cartao.remove(id);
    }

    public void addRegistro(LocalDate data, double horas) {
        this.cartao.add(new CartaoDePonto(data, horas));
    }

    public double getHorasNormaisTrabalhadas(LocalDate dataInicial, LocalDate dataFinal) throws Exception {

        double horasAcumuladas = 0;

        if (dataInicial.isEqual(dataFinal))
            return horasAcumuladas;

        if (dataInicial.isAfter(dataFinal)) {
            ExceptionEmpregado e = new ExceptionEmpregado();
            e.msgDataInicialPosteriorDataFinal();

            return horasAcumuladas;
        }

        for (CartaoDePonto c : cartao) {
            if (c.getData().isEqual(dataInicial) ||
                    (c.getData().isAfter(dataInicial) && c.getData().isBefore(dataFinal))) {

                if (c.getHoras() > 8) {
                    horasAcumuladas += 8.0;
                } else {
                    horasAcumuladas += c.getHoras();
                }
            }
        }

        return horasAcumuladas;
    }

    public double getHorasExtrasTrabalhadas(LocalDate dataInicial, LocalDate dataFinal) throws Exception {
        double horasAcumuladas = 0;

        if (dataInicial.isAfter(dataFinal)) {
            ExceptionEmpregado e = new ExceptionEmpregado();
            e.msgDataInicialPosteriorDataFinal();

            return horasAcumuladas;
        }

        for (CartaoDePonto c : cartao) {
            if (c.getData().isEqual(dataInicial) ||
                    (c.getData().isAfter(dataInicial) && c.getData().isBefore(dataFinal))) {

                if (c.getHoras() > 8) {
                    horasAcumuladas += (c.getHoras() - 8.0);
                }
            }
        }

        return horasAcumuladas;
    }

    @Override
    public void setSalario (double salario) {
        this.salarioPorHora = salario;
    }

    @Override
    public double getSalario() {
        return this.salarioPorHora;
    }

    @Override
    public String getTipo() {
        return "horista";
    }
}
