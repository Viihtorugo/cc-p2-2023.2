package br.ufal.ic.p2.wepayu.strategy.empregado;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.controller.FolhaDePagamentoController;
import br.ufal.ic.p2.wepayu.controller.SystemController;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionNaoHaEmpregadoComEsseNome;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionTipoInvalido;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.MembroSindicalizado;
import br.ufal.ic.p2.wepayu.models.empregado.membrosindicalizado.TaxaServico;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Banco;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.Correios;
import br.ufal.ic.p2.wepayu.models.empregado.metodopagamento.tiposdemetodopagamento.EmMaos;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadocomissionado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.tiposdeempregados.empregadohorista.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.utils.Valid;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ContextoEmpregado implements StrategyEmpregado {
    private Valid verification;

    public ContextoEmpregado() {
        this.verification = new Valid();
    }

    @Override
    public Empregado executeCriarEmpregado(String nome, String endereco, String tipo,
                                           String salario, EmpregadoController empregadoController) {

        if (this.verification.validNome(nome) && this.verification.validEndereco(endereco)
                && this.verification.validTipoNaoComissionado(tipo) && this.verification.validSalario(salario)) {

            double salarioFormatado = Utils.convertStringToDouble(salario);

            if (tipo.equals("assalariado")) {
                return new EmpregadoAssalariado(nome, endereco, "mensal $", salarioFormatado);
            } else if (tipo.equals("horista")) {
                return new EmpregadoHorista(nome, endereco, "semanal 5", salarioFormatado);
            }
        }

        return null;
    }

    @Override
    public Empregado executeCriarEmpregado(String nome, String endereco, String tipo,
                                           String salario, String comissao,
                                           EmpregadoController empregadoController) {

        if (this.verification.validNome(nome) && this.verification.validEndereco(endereco) &&
                this.verification.validTipoComissionado(tipo) && this.verification.validSalario(salario)
                && this.verification.validComissao(comissao)) {


            double salarioFormatado = Utils.convertStringToDouble(salario);
            double comissaoFormatado = Utils.convertStringToDouble(comissao);

            return new EmpregadoComissionado(nome, endereco, "semanal 2 5", salarioFormatado, comissaoFormatado);
        }

        return null;
    }

    public String getEmpregadoPorNome(String nome, int indice, EmpregadoController empregadoController) {

        int count = 0;

        HashMap<String, Empregado> empregados = empregadoController.getEmpregados();

        for (Map.Entry<String, Empregado> entry : empregados.entrySet()) {

            Empregado e = entry.getValue();

            if (nome.contains(e.getNome()))
                count++;

            if (count == indice)
                return entry.getKey();
        }

        throw new ExceptionNaoHaEmpregadoComEsseNome();
    }

    @Override
    public void executeRemoveEmpregado(String emp, EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {

            File file = new File(emp + ".xml");

            if (file.exists()) {
                System.out.println("Empredado deletado!");
                file.delete();
            }

            empregadoController.removeEmpregado(emp);
        }
    }

    public void executeLancaTaxaServico(String membro, String data, String valor,
                                        EmpregadoController empregadoController) {

        if (this.verification.validMembroSindicalizadoPeloID(membro, empregadoController)) {
            if (this.verification.validValor(valor)) {
                if (this.verification.validData(data, " ")) {

                    String id = empregadoController.getEmpregadoPorIdSindical(membro);

                    double valorFormato = Utils.convertStringToDouble(valor);

                    empregadoController.getEmpregado(id).addTaxaServico(new TaxaServico(data, valorFormato));
                }
            }
        }
    }

    public String getTaxasServico(String emp, String dataInicial, String dataFinal,
                                  EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {
            Empregado empregado = empregadoController.getEmpregado(emp);

            if (this.verification.validMembroSindicalizado(empregado)) {
                MembroSindicalizado membroSindicalizado = empregado.getSindicalizado();

                if (this.verification.validData(dataInicial, " inicial ")) {
                    if (this.verification.validData(dataFinal, " final ")) {


                        LocalDate dataInicialFormato = Utils.convertStringToLocalDate(dataInicial);
                        LocalDate dataFinalFormato = Utils.convertStringToLocalDate(dataFinal);


                        double taxa = membroSindicalizado.getTaxaServicos(dataInicialFormato, dataFinalFormato);

                        return Utils.convertDoubleToString(taxa, 2);
                    }
                }
            }
        }

        return "0,00";
    }



    //3 variaveis
    @Override
    public void executeAlteraEmpregado(String emp, String atributo, String valor,
                                       EmpregadoController empregadoController,
                                       FolhaDePagamentoController folhaDePagamentoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {
            if (this.verification.validGetAtributo(atributo)) {
                Empregado empregado = empregadoController.getEmpregado(emp);

                switch (atributo) {
                    case "nome" -> {
                        if (this.verification.validNome(valor))
                            empregado.setNome(valor);
                    }
                    case "endereco" -> {
                        if (this.verification.validEndereco(valor))
                            empregado.setEndereco(valor);
                    }
                    case "agendaPagamento" -> {

                        if (folhaDePagamentoController.alterarAgendaDePagamentoDoEmpregado(valor)) {
                            empregado.setAgendaDePagamento(valor);
                        }
                    }
                    case "salario" -> {
                        if (this.verification.validSalario(valor)) {
                            empregado.setSalario(Utils.convertStringToDouble(valor));
                        }
                    }
                    case "sindicalizado" -> {
                        if (this.verification.validSindicalizado(valor)) {
                            if (valor.equals("false"))
                                empregado.setSindicalizado(null);
                        }
                    }
                    case "comissao" -> {

                        if (this.verification.validTipoEmpregado(empregado, "comissionado")) {
                            if (this.verification.validComissao(valor)) {
                                double comissao = Utils.convertStringToDouble(valor);

                                ((EmpregadoComissionado) empregado).setTaxaDeComissao(comissao);
                            }
                        }
                    }
                    case "tipo" -> {

                        if (this.verification.validAlterarTipoEmpregado(empregado, valor)) {

                            String nome = empregado.getNome();
                            String endereco = empregado.getEndereco();
                            double salario = empregado.getSalario();

                            switch (valor) {
                                case "horista" ->
                                        empregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, "semanal 5", salario));
                                case "assalariado" ->
                                        empregadoController.setValue(emp, new EmpregadoAssalariado(nome, endereco, "mensal $", salario));
                                case "comissionado" ->
                                        empregadoController.setValue(emp, new EmpregadoComissionado(nome, endereco, "semanal 2 5", salario, 0));
                                default -> throw new ExceptionTipoInvalido();
                            }
                        }
                    }

                    case "metodoPagamento" -> {

                        if (this.verification.validMetodoPagamento(valor)) {
                            if (valor.equals("correios")) empregado.setMetodoPagamento(new Correios());
                            else if (valor.equals("emMaos")) empregado.setMetodoPagamento(new EmMaos());
                        }

                    }
                }
            }
        }
    }

    // 4 variaveis
    @Override
    public void executeAlteraEmpregado(String emp, String atributo, String valor,
                                       String sal, EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {

            Empregado empregado = empregadoController.getEmpregado(emp);

            if (this.verification.validGetAtributo(atributo)) {

                String nome = empregado.getNome();
                String endereco = empregado.getEndereco();

                if (valor.equals("comissionado")) {
                    double salario = empregado.getSalario();

                    if (this.verification.validComissao(sal)) {
                        double comissao = Utils.convertStringToDouble(sal);

                        empregadoController.setValue(emp, new EmpregadoComissionado(nome, endereco, "semanal 2 5", salario, comissao));
                    }

                } else if (valor.equals("horista")) {
                    if (this.verification.validSalario(sal)) {
                        double novoSalario = Utils.convertStringToDouble(sal);

                        empregadoController.setValue(emp, new EmpregadoHorista(nome, endereco, "semanal 5", novoSalario));
                    }
                }
            }
        }
    }

    // 5 variaveis
    @Override
    public void executeAlteraEmpregado(String emp, String atributo, String valor,
                                       String idSindicato, String taxaSindical,
                                       EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {
            if (this.verification.validIdSindical(idSindicato)) {
                if (this.verification.validTaxaSindical(taxaSindical)) {

                    double taxaSindicalNumber = Utils.convertStringToDouble(taxaSindical);

                    if (atributo.equals("sindicalizado") && valor.equals("true")) {

                        if (this.verification.sindicalizarEmpregado(idSindicato, empregadoController)) {

                            Empregado empregado = empregadoController.getEmpregado(emp);

                            empregado.setSindicalizado(new MembroSindicalizado(idSindicato, taxaSindicalNumber));
                        }
                    }
                }
            }
        }
    }

    // 6 variaveis
    @Override
    public void executeAlteraEmpregado(String emp, String atributo, String tipo,
                                       String banco, String agencia, String contaCorrente,
                                       EmpregadoController empregadoController) {

        if (this.verification.validEmpregado(emp, empregadoController)) {
            if (this.verification.validGetAtributo(atributo)) {
                Empregado empregado = empregadoController.getEmpregado(emp);

                if (this.verification.validBanco(banco, agencia, contaCorrente))
                    empregado.setMetodoPagamento(new Banco(banco, agencia, contaCorrente));
            }
        }
    }

}
