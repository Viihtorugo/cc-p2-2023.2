package br.ufal.ic.p2.wepayu.controller;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionSystem;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

import java.util.HashMap;
import java.util.Stack;

public class SystemController {
    private static Stack<HashMap<String, Empregado>> undo;
    private static Stack<HashMap<String, Empregado>> redo;

    private static boolean systemOn;

    public static void setSystemOn (boolean systemOn) {
        SystemController.systemOn = systemOn;
    }

    public static void systemStart() {
        systemOn = true;
        undo = new Stack<>();
        redo = new Stack<>();
    }

    public static void pushUndo(EmpregadoController empregadoController) throws Exception {

        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return;
        }

        HashMap<String, Empregado> novaHash = empregadoController.copyHashEmpregados();

        undo.push(novaHash);
    }

    //apenas a lista!
    public static void pushUndo(HashMap<String, Empregado> empregados) throws Exception {

        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return;
        }

        undo.push(empregados);
    }

    public static void popUndoErro() throws Exception {

        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return;
        }

        if (undo.empty()) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoHaComandoDesfazer();
            return;
        }

        undo.pop();
    }

    public static HashMap<String, Empregado> popUndo() throws Exception {
        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return null;
        }

        if (undo.empty()) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoHaComandoDesfazer();
            return  null;
        }

        HashMap<String, Empregado> e = undo.peek();

        if (undo.size() > 1)  SystemController.pushRedo(e);

        undo.pop();

        return e;
    }

    public static void pushRedo(HashMap<String, Empregado> empregados) throws Exception {
        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return;
        }

        redo.push(empregados);
    }

    public static HashMap<String, Empregado> popRedo() throws Exception {
        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return null;
        }

        if (redo.empty()) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoHaComandoFazer();
            return  null;
        }

        HashMap<String, Empregado> empregados = redo.peek();

        SystemController.pushUndo(empregados);
        redo.pop();

        return empregados;
    }
}
