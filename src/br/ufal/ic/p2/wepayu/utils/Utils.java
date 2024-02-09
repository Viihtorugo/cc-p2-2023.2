package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.controller.EmpregadoController;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionConversao;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionEmpregado;
import br.ufal.ic.p2.wepayu.exceptions.ExceptionMetodosDePagamento;
import br.ufal.ic.p2.wepayu.models.*;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Utils {

    public static Map<String, String> sortHashMap(HashMap<String, String> hashMap) {

        List<Map.Entry<String, String>> entryList = new ArrayList<>(hashMap.entrySet());

        entryList.sort(Map.Entry.comparingByValue());

        Map<String, String> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    
    public static void initSystem () {
        EmpregadoController.empregados = new HashMap<>();
        EmpregadoController.key = 0;
    }
    public static void deleteFilesXML () {

        for (int i = 1; i <= 1000; i++) {
            File file = new File( i + ".xml" );

            if (file.exists()) {
                file.delete();
            } else {
                return;
            }
        }
    }

    public static void deleteFolhas () {
        File[] folhas = new File("./").listFiles();

        for (File f : folhas) {
            if (f.getName().endsWith(".txt")) {
                // Excluir o arquivo
                f.delete();
            }
        }
    }

    public static boolean validNome (String nome) throws Exception {

        if (nome.isEmpty()) {
            ExceptionEmpregado e = new ExceptionEmpregado();
            e.msgNomeNulo();
            return false;
        }

        return true;
    }

    public static boolean validEndereco (String endereco) throws Exception {

        if (endereco.isEmpty()) {
            ExceptionEmpregado e = new ExceptionEmpregado();
            e.msgEnderecoNulo();
            return false;
        }

        return true;
    }
    public static boolean sindicalizarEmpregado(String idSindicato) throws Exception {

        boolean flag = true;

        for (Map.Entry<String, Empregado> entry : EmpregadoController.empregados.entrySet()) {
            MembroSindicalizado m = entry.getValue().getSindicalizado();

            if (m != null)
                if (m.getIdMembro().equals(idSindicato))
                    flag = false;
        }

        if (!flag) {
            ExceptionEmpregado ex = new ExceptionEmpregado();
            ex.msgIndentificaoSindicatoDuplicada();
        }

        return flag;
    }

    public static double validTaxaSindical(String taxaSindical) throws Exception {

        if (taxaSindical.isEmpty()) {
            ExceptionEmpregado e = new ExceptionEmpregado();
            e.msgTaxaSindicalNula();

            return -1;
        }

        if (!taxaSindical.matches("[0-9,-]+")) {
            ExceptionConversao e = new ExceptionConversao();
            e.msgTaxaSindicalDeveSerNumerica();
            return -1.0;
        }

        double taxaSindicalNumber = Double.parseDouble(taxaSindical.replace(",", "."));

        if (taxaSindicalNumber <= 0.0) {
            ExceptionConversao e = new ExceptionConversao();
            e.msgTaxaSindicalNaoNegativa();
        }

        return taxaSindicalNumber;
    }

    public static boolean validIdSindical(String idSindicato) throws Exception {

        if (idSindicato.isEmpty()) {
            ExceptionEmpregado e = new ExceptionEmpregado();
            e.msgIdSindicalNula();
            return true;
        }

        return false;
    }

    public static double validValor(String valor) throws Exception {

        if (!valor.matches("[0-9,-]+")) {
            ExceptionConversao e = new ExceptionConversao();
            e.msgValorDeveSerNumerico();
            return -1.0;
        }

        double valorFormato = Double.parseDouble(valor.replace(",", "."));

        if (valorFormato <= 0) {
            ExceptionConversao e = new ExceptionConversao();
            e.msgValorDeveSerPostivo();
        }

        return valorFormato;
    }

    public static String validMembroSindicalizadoPeloID(String membro) throws Exception {

        ExceptionEmpregado e = new ExceptionEmpregado();

        if (membro.isEmpty()) {
            e.msgIdNaoPodeSerNulo();
            return null;
        }

        String id = EmpregadoController.getEmpregadoPorIdSindical(membro);

        if (id == null) {
            e.msgMembroNaoExiste();
        }

        return id;
    }

    public static MembroSindicalizado validMembroSindicalizado(Empregado e) throws Exception {
        MembroSindicalizado m = e.getSindicalizado();

        if (m == null) {
            ExceptionEmpregado ex = new ExceptionEmpregado();
            ex.msgEmpregadoNaoSindicalizado();
        }

        return m;
    }

    public static Empregado validEmpregado(String emp) throws Exception {
        ExceptionEmpregado ex = new ExceptionEmpregado();

        if (emp.isEmpty())
            ex.msgNullIndex();

        Empregado e = EmpregadoController.getEmpregado(emp);

        if (e != null)
            return e;

        ex.msgEmpregadoNotExist();

        return null;
    }

    public static boolean validMetodoPagamento(String valor) throws Exception {
        if (valor.equals("correios") || valor.equals("emMaos") || valor.equals("banco"))
            return true;

        ExceptionEmpregado e = new ExceptionEmpregado();
        e.msgMetodoPagInvalido();

        return false;
    }

    public static boolean validSindicalizado(String valor) throws Exception {
        if (valor.equals("true"))
            return true;

        if (valor.equals("false"))
            return false;

        ExceptionConversao e = new ExceptionConversao();
        e.msgValorTrueOuFalse();

        return false;
    }

    public static boolean validAlterarTipo(Empregado e, String tipo) throws Exception {

        ExceptionEmpregado ex = new ExceptionEmpregado();

        if (!(tipo.equals("comissionado") || tipo.equals("assalariado") || tipo.equals("horista"))) {
            ex.msgTipoInvalido();
            return true;
        }

        return e.getTipo().equals(tipo);
    }

    public static boolean validTipoEmpregado(Empregado e, String tipo) throws Exception {

        if (e.getTipo().equals(tipo)) {
            return true;
        }

        ExceptionEmpregado ex = new ExceptionEmpregado();

        if (!(tipo.equals("comissionado") || tipo.equals("assalariado") || tipo.equals("horista"))) {
            ex.msgTipoInvalido();
            return false;
        }

        ex.msgEmpregadoNaoTipo(tipo);

        return false;
    }

    //4 variaveis
    public static boolean validCriarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        ExceptionEmpregado e = new ExceptionEmpregado();

        if (nome.isEmpty()) {
            e.msgNomeNulo();
            return false;
        }

        if (endereco.isEmpty()) {
            e.msgEnderecoNulo();
            return false;
        }

        if (!(tipo.equals("comissionado") || tipo.equals("assalariado") || tipo.equals("horista"))) {
            e.msgTipoInvalido();
            return false;
        }

        if (salario.isEmpty()) {
            e.msgSalarioNulo();
            return false;
        }

        return true;
    }

    // 5 variaveis
    public static boolean validCriarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {

        if (!validCriarEmpregado(nome, endereco, tipo, salario))
            return false;

        ExceptionEmpregado e = new ExceptionEmpregado();

        if (comissao.isEmpty()) {
            e.msgComissaoNula();
            return false;
        }

        return true;
    }

    public static double validSalario(String salario) throws Exception {

        if (salario.isEmpty()) {
            ExceptionEmpregado ex = new ExceptionEmpregado();
            ex.msgSalarioNulo();
            return  -1;
        }

        ExceptionConversao e = new ExceptionConversao();

        if (!salario.matches("[0-9,-]+")) {
            e.msgSalarioDeveSerNumerico();
            return -1.0;
        }

        if (salario.contains("-")) {
            e.msgSalarioNaoNegativo();
            return -1.0;
        }

        return Double.parseDouble(salario.replace(",", "."));
    }

    public static double validComissao(String comissao) throws Exception {

        if (comissao.isEmpty()) {
            ExceptionEmpregado ex = new ExceptionEmpregado();
            ex.msgComissaoNula();
            return -1;
        }

        ExceptionConversao e = new ExceptionConversao();

        if (!comissao.matches("[0-9,-]+")) {
            e.msgComissaoDeveSerNumerica();
            return -1.0;
        }

        if (comissao.contains("-")) {
            e.msgComissaoNaoNegativa();
            return -1.0;
        }

        return Double.parseDouble(comissao.replace(",", "."));
    }

    public static boolean validTipoComissionado(String tipo) throws Exception {

        if (tipo.equals("comissionado")) {
            return true;
        }

        ExceptionEmpregado e = new ExceptionEmpregado();
        e.msgTipoNaoAplicavel();

        return false;
    }

    public static boolean validTipoNotComissionado(String tipo) throws Exception {
        ExceptionEmpregado e = new ExceptionEmpregado();

        if (tipo.equals("comissionado")) {
            e.msgTipoNaoAplicavel();
            return false;
        }

        return true;
    }

    public static boolean validBanco(String banco, String agencia, String contaCorrente) throws Exception {
        ExceptionEmpregado e = new ExceptionEmpregado();

        if (banco.isEmpty()) {
            e.msgBancoNulo();
            return false;
        }

        if (agencia.isEmpty()) {
            e.msgAgenciaNulo();
            return false;
        }

        if (contaCorrente.isEmpty()) {
            e.msgContaCorrenteNulo();
            return false;
        }

        return true;
    }

    public static boolean validGetAtributo(String emp, String atributo) throws Exception {
        ExceptionEmpregado ex = new ExceptionEmpregado();

        if (emp.isEmpty()) {
            ex.msgNullIndex();
            return false;
        }

        if (atributo.isEmpty()) {
            ex.msgAtributoNotExit();
            return false;
        }

        switch (atributo) {
            case "nome", "tipo", "salario", "endereco",
                    "comissao", "metodoPagamento", "banco",
                    "agencia", "contaCorrente", "sindicalizado",
                    "idSindicato", "taxaSindical" -> {
                return true;
            }
            default -> {
                ex.msgAtributoNotExit();
                return false;
            }
        }
    }

    public static boolean validSindicato(MembroSindicalizado m) throws Exception {
        ExceptionEmpregado ex = new ExceptionEmpregado();

        if (m != null)
            return true;

        ex.msgEmpregadoNotSindicalizado();
        return false;
    }

    public static double validHoras(String horas) throws Exception {
        ExceptionConversao e = new ExceptionConversao();

        if (horas.isEmpty()) {
            e.msgHorasNula();
            return -1;
        }

        if (!horas.matches("[0-9,-]+")) {
            e.msgHorasDeveSerNumerica();
            return -1;
        }

        double horasFormato = Double.parseDouble(horas.replace(",", "."));

        if (horasFormato <= 0) {
            e.msgHorasDeveSerPositiva();

            return -1;
        }

        return horasFormato;
    }

    public static String invertFormatData(String data) {
        String[] s = data.split("/");

        return s[2] + "-" + s[1] + "-" + s[0];
    }

    public static LocalDate validData(String data, String tipo) throws Exception {

        String[] blocos = data.split("/");

        int d = Integer.parseInt(blocos[0]);
        int m = Integer.parseInt(blocos[1]);
        int y = Integer.parseInt(blocos[2]);

        if (m > 12 || m < 1) {
            ExceptionConversao ex = new ExceptionConversao();
            ex.msgDataInvalida(tipo);
            return null;
        }

        YearMonth yearMonth = YearMonth.of(y, m);

        if (d > yearMonth.lengthOfMonth()) {
            ExceptionConversao ex = new ExceptionConversao();
            ex.msgDataInvalida(tipo);
            return null;
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(data, formato);
    }

    public static boolean empregadoIsNotComissionado(Empregado e) throws Exception {
        ExceptionEmpregado ex = new ExceptionEmpregado();

        if (e.getTipo().equals("comissionado"))
            return true;

        ex.msgEmpregadoNotComissionado();

        return false;
    }

    public static boolean metodoPagamentoIsBanco(MetodoPagamento m) throws Exception {
        ExceptionMetodosDePagamento e = new ExceptionMetodosDePagamento();

        if (m instanceof Banco)
            return true;

        e.msgNaoRecebeEmBanco();
        return false;
    }

    public static LocalDate isValidDateString(String date) throws Exception {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new Exception("Data inválida.");
        }
    }

    public static String convertDoubleToString(double value) {
        if (value != (int) value) return Double.toString(value).replace('.', ',');
        else return Integer.toString((int) value);
    }

    public static String convertDoubleToString(double value, int decimalPlaces) {
        return String.format(("%." + decimalPlaces + "f"), value).replace(".", ",");
    }

    public static String convertDoubleToStringByFolha(double value) {
        System.out.println("test");
        return null;
    }

    public static double convertDoubleToStringFormattPagamento (String value) {

        int posicaoSeparador = value.indexOf(".");

        if (posicaoSeparador + 3 < value.length()) {
            value = value.substring(0, posicaoSeparador + 3);
        }

        return Double.parseDouble(value.replace(",", "."));
    }

    public static String padLeft(String value, int length) {

        if (value.length() >= length) {
            return value;
        }

        int padLength = length - value.length();

        return " ".repeat(padLength) + value;
    }

    public static String padLeft(String value, int length, String padChar) {

        if (value.length() >= length) {
            return value;
        }

        int padLength = length - value.length();

        return padChar.repeat(padLength) + value;
    }


    public static String padRight(String value, int length) {

        if (value.length() >= length) {
            return value;
        }

        int padLength = length - value.length();

        return value + " ".repeat(padLength);
    }

    public static String padRight(String value, int length, String padChar) {

        if (value.length() >= length) {
            return value;
        }

        int padLength = length - value.length();

        System.out.println(padLength);

        return value + padChar.repeat(padLength);
    }
}
