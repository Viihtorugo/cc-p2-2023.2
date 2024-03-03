package br.ufal.ic.p2.wepayu.controller;

import br.ufal.ic.p2.wepayu.exceptions.ExceptionSystem;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

import java.util.HashMap;
import java.util.Stack;

public class SystemController {
    private static Stack<HashMap<String, Empregado>> undo;
    private static Stack<HashMap<String, Empregado>> redo;

    private static boolean systemOn = true;

    public static void pushUndo(EmpregadoController e) throws Exception {

        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return;
        }

        HashMap<String, Empregado> novaHash = e.novaHashEmpregados();

        if (undo == null)
            undo = new Stack<>();

        undo.push(novaHash);
    }

    //apenas a lista!
    public static void pushUndo(HashMap<String, Empregado> e) throws Exception {

        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return;
        }

        if (undo == null)
            undo = new Stack<>();

        undo.push(e);
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

    public static void pushRedo(HashMap<String, Empregado> e) throws Exception {
        if (!systemOn) {
            ExceptionSystem exceptionSystem = new ExceptionSystem();
            exceptionSystem.msgNaoPodeDarComandos();
            return;
        }

        if (redo == null) redo = new Stack<>();

        redo.push(e);
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

        HashMap<String, Empregado> e = redo.peek();

        SystemController.pushUndo(e);
        redo.pop();

        return e;
    }
}
