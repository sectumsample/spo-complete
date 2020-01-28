package Parsing;

import Structures.HashSet;
import Structures.LinkedList;
import Lexeme.LexemeTypes;
import Lexeme.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Parser {
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        curToken = 0;
        curF = "main";
        rpnMap.put(curF, new ArrayList<>());
        varTable.put(curF, new HashMap<>());
    }

    public boolean lang() {
        boolean lang = false;
        while (this.tokens.size() != curToken) {
            if (!expr()) {
                System.err.println("Expr error");
                System.exit(5);
            } else {
                lang = true;
            }
        }
        //System.out.println(reversePolishNotation);
        return lang;
    }

    private boolean expr() {
        boolean expr = false;
        if (declare_stmt() ||
                init() ||
                assign() ||
                while_expr() ||
                if_expr() ||
                for_expr() ||
                set_stmt() ||
                print() ||
                function() ||
                functionExecute()
                || thread() ||
                threadJoin() ||
                mutLock() ||
                mutUnlock()) {
            expr = true;
        }
        return expr;
    }

    //!!!!

    private boolean mutexLock() {
        boolean thread = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.get(curF).get(getLastTokenValue()) instanceof Semaphore) {
            String threadVar = getLastTokenValue();
            if (getTokenLexeme() == LexemeTypes.DOT) {
                if (getTokenLexeme() == LexemeTypes.LOCK) {
                    if (getTokenLexeme() == LexemeTypes.L_RB) {
                        if (getTokenLexeme() == LexemeTypes.VAR && rpnMap.containsKey("block_" + getLastTokenValue())) {
                            String funcName = getLastTokenValue();
                            if (getTokenLexeme() == LexemeTypes.R_RB) {
                                if (getTokenLexeme() == LexemeTypes.SEMI) {
                                    rpnMap.get(curF).add("thread_" + funcName);
                                    rpnMap.get(curF).add(threadVar);
                                    rpnMap.get(curF).add("lock");
                                    thread = true;
                                }
                            } else {
                                curToken--;
                                if (getTokenLexeme() == LexemeTypes.L_RB) {
                                    if (params(funcName)) {
                                        if (getTokenLexeme() == LexemeTypes.R_RB) {
                                            if (getTokenLexeme() == LexemeTypes.R_RB) {
                                                if (getTokenLexeme() == LexemeTypes.SEMI) {
                                                    rpnMap.get(curF).add("thread_" + funcName);
                                                    rpnMap.get(curF).add(threadVar);
                                                    rpnMap.get(curF).add("lock");
                                                    thread = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        curToken = thread ? curToken : oldPos;
        return thread;
    }

    private boolean mutexUnlock() {
        boolean thread = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.get(curF).get(getLastTokenValue()) instanceof Semaphore) {
            String threadVar = getLastTokenValue();
            if (getTokenLexeme() == LexemeTypes.DOT) {
                if (getTokenLexeme() == LexemeTypes.UNLOCK) {
                    if (getTokenLexeme() == LexemeTypes.L_RB) {
                        if (getTokenLexeme() == LexemeTypes.VAR && rpnMap.containsKey("block_" + getLastTokenValue())) {
                            String funcName = getLastTokenValue();
                            if (getTokenLexeme() == LexemeTypes.R_RB) {
                                if (getTokenLexeme() == LexemeTypes.SEMI) {
                                    rpnMap.get(curF).add("thread_" + funcName);
                                    rpnMap.get(curF).add(threadVar);
                                    rpnMap.get(curF).add("unlock");
                                    thread = true;
                                }
                            } else {
                                curToken--;
                                if (getTokenLexeme() == LexemeTypes.L_RB) {
                                    if (params(funcName)) {
                                        if (getTokenLexeme() == LexemeTypes.R_RB) {
                                            if (getTokenLexeme() == LexemeTypes.R_RB) {
                                                if (getTokenLexeme() == LexemeTypes.SEMI) {
                                                    rpnMap.get(curF).add("thread_" + funcName);
                                                    rpnMap.get(curF).add(threadVar);
                                                    rpnMap.get(curF).add("unlock");
                                                    thread = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        curToken = thread ? curToken : oldPos;
        return thread;
    }

    private boolean thread() {
        boolean thread = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.get(curF).get(getLastTokenValue()) instanceof Thread) {
            String threadVar = getLastTokenValue();
            if (getTokenLexeme() == LexemeTypes.DOT) {
                if (getTokenLexeme() == LexemeTypes.RUN) {
                    if (getTokenLexeme() == LexemeTypes.L_RB) {
                        if (getTokenLexeme() == LexemeTypes.VAR && rpnMap.containsKey("block_" + getLastTokenValue())) {
                            String funcName = getLastTokenValue();
                            if (getTokenLexeme() == LexemeTypes.R_RB) {
                                if (getTokenLexeme() == LexemeTypes.SEMI) {
                                    rpnMap.get(curF).add("thread_" + funcName);
                                    rpnMap.get(curF).add(threadVar);
                                    rpnMap.get(curF).add("run");
                                    thread = true;
                                }
                            } else {
                                curToken--;
                                if (getTokenLexeme() == LexemeTypes.L_RB) {
                                    if (params(funcName)) {
                                        if (getTokenLexeme() == LexemeTypes.R_RB) {
                                            if (getTokenLexeme() == LexemeTypes.R_RB) {
                                                if (getTokenLexeme() == LexemeTypes.SEMI) {
                                                    rpnMap.get(curF).add("thread_" + funcName);
                                                    rpnMap.get(curF).add(threadVar);
                                                    rpnMap.get(curF).add("run");
                                                    thread = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        curToken = thread ? curToken : oldPos;
        return thread;
    }

    private boolean threadJoin() {
        boolean thread = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.get(curF).get(getLastTokenValue()) instanceof Thread) {
            String threadVar = getLastTokenValue();
            if (getTokenLexeme() == LexemeTypes.DOT) {
                if (getTokenLexeme() == LexemeTypes.JOIN) {
                    if (getTokenLexeme() == LexemeTypes.L_RB) {
                        if (getTokenLexeme() == LexemeTypes.R_RB) {
                            if (getTokenLexeme() == LexemeTypes.SEMI) {
                                rpnMap.get(curF).add(threadVar);
                                rpnMap.get(curF).add("join");
                                thread = true;
                            }
                        }
                    }
                }
            }
        }

        curToken = thread ? curToken : oldPos;
        return thread;
    }

    private boolean mutLock() {
        boolean thread = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.get(curF).get(getLastTokenValue()) instanceof Semaphore) {
            //System.out.println("block_" + getLastTokenValue());
            String threadVar = getLastTokenValue();
            if (getTokenLexeme() == LexemeTypes.DOT) {
                if (getTokenLexeme() == LexemeTypes.LOCK) {
                    if (getTokenLexeme() == LexemeTypes.L_RB) {
                        if (getTokenLexeme() == LexemeTypes.R_RB) {
                            if (getTokenLexeme() == LexemeTypes.SEMI) {
                                rpnMap.get(curF).add(threadVar);
                                rpnMap.get(curF).add("lock");
                                thread = true;
                            }
                        }
                    }
                }
            }
        }

        curToken = thread ? curToken : oldPos;
        return thread;
    }

    private boolean mutUnlock() {
        boolean thread = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.get(curF).get(getLastTokenValue()) instanceof Semaphore) {
            String threadVar = getLastTokenValue();
            if (getTokenLexeme() == LexemeTypes.DOT) {
                if (getTokenLexeme() == LexemeTypes.UNLOCK) {
                    if (getTokenLexeme() == LexemeTypes.L_RB) {
                        if (getTokenLexeme() == LexemeTypes.R_RB) {
                            if (getTokenLexeme() == LexemeTypes.SEMI) {
                                rpnMap.get(curF).add(threadVar);
                                rpnMap.get(curF).add("unlock");
                                thread = true;
                            }
                        }
                    }
                }
            }
        }

        curToken = thread ? curToken : oldPos;
        return thread;
    }

    private boolean function() {
        boolean func = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.FUNCTION) {
            if (getTokenLexeme() == LexemeTypes.TYPE) {
                if (getTokenLexeme() == LexemeTypes.VAR) {
                    String funcName = "block_" + getLastTokenValue();
                    varTable.put(funcName, new HashMap<>());
                    if (getTokenLexeme() == LexemeTypes.L_RB) {
                        if (paramsInit(funcName)) {
                            if (getTokenLexeme() == LexemeTypes.R_RB) {
                                if (getTokenLexeme() == LexemeTypes.L_FB) {
                                    prevF = curF;
                                    curF = funcName;
                                    rpnMap.put(funcName, new ArrayList<>());
                                    while (expr()) {
                                    }
                                    if (getTokenLexeme() == LexemeTypes.RETURN) {
                                        varTable.get(curF).put(getLastTokenValue(), null);
                                        if (getTokenLexeme() == LexemeTypes.VAR) {
                                            rpnMap.get(curF).add(getLastTokenValue());
                                            rpnMap.get(curF).add("return");
                                            if (getTokenLexeme() == LexemeTypes.SEMI) {
                                                if (getTokenLexeme() == LexemeTypes.R_FB) {
                                                    curF = prevF;
                                                    rpnMap.get(curF).add(funcName);
                                                    func = true;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (getTokenLexeme() == LexemeTypes.R_FB) {
                                        curF = prevF;
                                        rpnMap.get(curF).add(funcName);
                                        func = true;
                                    }
                                }
                            }
                        }
                    }
                    if (!func) {
                        rpnMap.remove(funcName);
                    }
                }
            } else {
                curToken--;
                if (getTokenLexeme() == LexemeTypes.VAR) {
                    String funcName = "block_" + getLastTokenValue();
                    varTable.put(funcName, new HashMap<>());
                    if (getTokenLexeme() == LexemeTypes.L_RB) {
                        if (paramsInit(funcName)) {
                            if (getTokenLexeme() == LexemeTypes.R_RB) {
                                if (getTokenLexeme() == LexemeTypes.L_FB) {
                                    prevF = curF;
                                    curF = funcName;
                                    rpnMap.put(funcName, new ArrayList<>());
                                    while (expr()) {
                                    }
                                    if (getTokenLexeme() == LexemeTypes.RETURN) {
                                        varTable.get(curF).put(getLastTokenValue(), null);
                                        if (getTokenLexeme() == LexemeTypes.VAR) {
                                            rpnMap.get(curF).add(getLastTokenValue());
                                            rpnMap.get(curF).add("return");
                                            if (getTokenLexeme() == LexemeTypes.SEMI) {
                                                if (getTokenLexeme() == LexemeTypes.R_FB) {
                                                    curF = prevF;
                                                    //reversePolishNotation.get(currentFunction).add(funcName);
                                                    func = true;
                                                }
                                            }
                                        } else {
                                            curToken--;
                                            if (getTokenLexeme() == LexemeTypes.SEMI) {
                                                if (getTokenLexeme() == LexemeTypes.R_FB) {
                                                    curF = prevF;
                                                    //reversePolishNotation.get(currentFunction).add(funcName);
                                                    func = true;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (getTokenLexeme() == LexemeTypes.R_FB) {
                                        curF = prevF;
                                        //reversePolishNotation.get(currentFunction).add(funcName);
                                        func = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        curToken = func ? curToken : oldPos;
        return func;
    }

    private boolean paramsInit(String funcName) {
        boolean param = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.TYPE) {
            if (getTokenLexeme() == LexemeTypes.VAR) {
                varTable.get(funcName).put(getLastTokenValue(), null);
                if (getTokenLexeme() == LexemeTypes.COM) {
                    if (paramsInit(funcName)) {
                        param = true;
                    }
                } else {
                    curToken--;
                    param = true;
                }
            }
        } else {
            curToken--;
            if (getTokenLexeme() == LexemeTypes.R_RB) {
                curToken--;
                param = true;
            }
        }

        curToken = param ? curToken : oldPos;
        return param;
    }

    private boolean init() {
        boolean init = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.TYPE) {
            if (assign_op()) {
                if (getTokenLexeme() == LexemeTypes.SEMI) {
                    init = true;
                }
            }
        }
        curToken = init ? curToken : oldPos;
        return init;
    }

    private boolean assign() {
        boolean assign = false;
        int oldPos = curToken;
        if (assign_op()) {
            if (getTokenLexeme() == LexemeTypes.SEMI) {
                assign = true;
            }
        }
        curToken = assign ? curToken : oldPos;
        return assign;
    }

    private boolean declare_stmt() {
        boolean declare = false;
        String var, type;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.TYPE) {
            type = getLastTokenValue();
            if (getTokenLexeme() == LexemeTypes.VAR && !varTable.get(curF).containsKey(getLastTokenValue())) {
                var = getLastTokenValue();
                if (getTokenLexeme() == LexemeTypes.SEMI) {
                    declare = true;
                    switch (type) {
                        case "Mutex":
                            varTable.get(curF).put(var, new Semaphore(1));
                            break;
                        case "HashSet":
                            varTable.get(curF).put(var, new HashSet(3));
                            break;
                        case "LinkedList":
                            varTable.get(curF).put(var, new LinkedList());
                            break;
                        case "Thread":
                            varTable.get(curF).put(var, new Thread());
                            break;
                        default:
                            varTable.get(curF).put(var, null);
                            break;
                    }
                }
            }
        }
        curToken = declare ? curToken : oldPos;
        return declare;
    }

    private boolean set_stmt() {
        boolean set = false;
        int oldPos = curToken;
        String col_op;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.get(curF).containsKey(getLastTokenValue())) {
            rpnMap.get(curF).add(getLastTokenValue());
            if (getTokenLexeme() == LexemeTypes.DOT) {
                if (getTokenLexeme() == LexemeTypes.COL_OP) {
                    col_op = getLastTokenValue();
                    if (bkt_value()) {
                        if (getTokenLexeme() == LexemeTypes.SEMI) {
                            set = true;
                            rpnMap.get(curF).add(col_op);
                        } else {
                            rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
                        }
                    } else {
                        rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
                    }
                } else {
                    rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
                }
            } else {
                rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
            }
        }
        curToken = set ? curToken : oldPos;
        return set;
    }

    private boolean if_expr() {
        boolean ifExpr = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.IF) {
            if (condition_stmt()) {
                if (if_body()) {
                    ifExpr = true;
                    rpnMap.get(curF).set(p0, String.valueOf(rpnMap.get(curF).size() - 1));
                }
            }
        }
        curToken = ifExpr ? curToken : oldPos;
        return ifExpr;
    }

    private boolean if_body() {
        boolean if_body = false;
        int oldPos = curToken;

        if (getTokenLexeme() == LexemeTypes.L_FB) {
            //noinspection StatementWithEmptyBody
            while (figure_bkt()) {
            }

            if (getTokenLexeme() == LexemeTypes.R_FB) {
                if_body = true;
            }
        }
        curToken = if_body ? curToken : oldPos;
        return if_body;
    }

    private boolean print() {
        boolean print = false;
        int old_position = curToken;

        if (getTokenLexeme() == LexemeTypes.PRINT) {
            if (getTokenLexeme() == LexemeTypes.SEMI) {
                rpnMap.get(curF).add("print");
                print = true;
            } else {
                curToken--;
                if (getTokenLexeme() == LexemeTypes.VAR) {
                    rpnMap.get(curF).add(getLastTokenValue());
                    if (getTokenLexeme() == LexemeTypes.SEMI) {
                        rpnMap.get(curF).add("print");
                        print = true;
                    } else {
                        rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
                    }
                } else {
                    curToken--;
                    if (getTokenLexeme() == LexemeTypes.STRING) {
                        rpnMap.get(curF).add(getLastTokenValue());
                        if (getTokenLexeme() == LexemeTypes.SEMI) {
                            rpnMap.get(curF).add("print");
                            print = true;
                        } else {
                            rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
                        }
                    } else {
                        rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
                    }
                }
            }
        }

        curToken = print ? curToken : old_position;
        return print;
    }

    private boolean while_expr() {
        boolean whileExpr = false;
        int oldPos = curToken;

        if (getTokenLexeme() == LexemeTypes.WHILE) {
            if (condition_stmt()) {
                if (while_body()) {
                    whileExpr = true;
                    rpnMap.get(curF).set(p0, String.valueOf(rpnMap.get(curF).size() + 1));
                    rpnMap.get(curF).add(String.valueOf(p1));
                    rpnMap.get(curF).add("!");
                }
            }
        }
        curToken = whileExpr ? curToken : oldPos;
        return whileExpr;
    }

    private boolean while_body() {
        boolean while_body = false;
        int oldPos = curToken;

        if (getTokenLexeme() == LexemeTypes.L_FB) {
            //noinspection StatementWithEmptyBody
            while (expr()) { //
            }
            if (getTokenLexeme() == LexemeTypes.R_FB) {
                while_body = true;
            }
        }
        curToken = while_body ? curToken : oldPos;
        return while_body;
    }

    private boolean for_expr() {
        boolean for_loop = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.FOR) {
            if (for_stmt()) {
                if (for_body()) {
                    for_loop = true;
                    rpnMap.get(curF).set(p0, String.valueOf(rpnMap.get(curF).size() + 1));
                    rpnMap.get(curF).add(String.valueOf(p1));
                    rpnMap.get(curF).add("!");
                }
            }
        }
        curToken = for_loop ? curToken : oldPos;
        return for_loop;
    }

    private boolean for_body() {
        boolean for_body = false;
        int oldPos = curToken;

        if (getTokenLexeme() == LexemeTypes.L_FB) {
            //noinspection StatementWithEmptyBody
            while (figure_bkt()) {
            }
            if (getTokenLexeme() == LexemeTypes.R_FB) {
                for_body = true;
            }
        }
        curToken = for_body ? curToken : oldPos;
        return for_body;
    }

    private boolean for_stmt() {
        boolean for_expr = false;
        int oldPos = curToken;

        if (getTokenLexeme() == LexemeTypes.L_RB) {
            if (start_stmt()) {
                if (if_log_expr()) {
                    if (assign_op()) {
                        if (getTokenLexeme() == LexemeTypes.R_RB) {
                            for_expr = true;
                        }
                    }
                }
            }
        }
        curToken = for_expr ? curToken : oldPos;
        return for_expr;
    }

    private boolean condition_stmt() {
        int oldPos = curToken;
        boolean condition = false;
        if (getTokenLexeme() == LexemeTypes.L_RB) {
            if (log_stmt()) {//log_stmt
                if (getTokenLexeme() == LexemeTypes.R_RB) {
                    condition = true;
                }
            }
        }
        curToken = condition ? curToken : oldPos;
        return condition;
    }

    private boolean start_stmt() {
        boolean start_expr = false;

        if (init() || assign()) {
            start_expr = true;
        }
        return start_expr;
    }

    private boolean log_stmt() {
        boolean log_expr = false;
        int oldPos = curToken;
        p1 = rpnMap.get(curF).size();
        if (stmt()) {
            if (getTokenLexeme() == LexemeTypes.COMP_OP) {
                String log_op = getLastTokenValue();
                if (stmt()) {
                    log_expr = true;
                    rpnMap.get(curF).add(log_op);
                    p0 = rpnMap.get(curF).size();
                    rpnMap.get(curF).add("" + p0);
                    rpnMap.get(curF).add("!F");
                }
            } else {
                rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
                rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
            }
        }

        curToken = log_expr ? curToken : oldPos;


        if (getTokenLexeme() == LexemeTypes.VAR) {
            rpnMap.get(curF).add(getLastTokenValue());
            if (getTokenLexeme() == LexemeTypes.DOT) {
                if (getTokenLexeme() == LexemeTypes.COL_OP && getLastTokenValue().equals("contains")) {
                    String col_op = getLastTokenValue();
                    if (bkt_value()) {
                        log_expr = true;
                        rpnMap.get(curF).add(col_op);
                        p0 = rpnMap.get(curF).size();
                        rpnMap.get(curF).add("" + p0);
                        rpnMap.get(curF).add("!F");
                    }
                }
            } else {
                rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
            }
        } else {
            curToken--;
        }

        curToken = log_expr ? curToken : oldPos;
        return log_expr;
    }

    private boolean if_log_expr() {
        boolean if_log_expr = false;
        int oldPos = curToken;
        if (assign_op() || stmt()) {
            if (getTokenLexeme() == LexemeTypes.COMP_OP) {
                String if_log_op = getLastTokenValue();
                if (assign_op() || stmt()) {
                    if (getTokenLexeme() == LexemeTypes.SEMI) {
                        if_log_expr = true;
                        rpnMap.get(curF).add(if_log_op);
                        int p2 = rpnMap.get(curF).size();
                        rpnMap.get(curF).add(p2 + "");
                        rpnMap.get(curF).add("!F");
                    }
                }
            }
        }
        curToken = if_log_expr ? curToken : oldPos;
        return if_log_expr;
    }

    private boolean assign_op() {
        boolean assign_op = false;
        int oldPos = curToken;
        boolean add = false;
        String var;

        if (getTokenLexeme() == LexemeTypes.VAR) {
            var = getLastTokenValue();
            add = rpnMap.get(curF).add(var);
            if (getTokenLexeme() == LexemeTypes.ASSIGN_OP) {
                stack.push(getLastTokenValue());
                if (stmt()) {
                    assign_op = true;
                    varTable.get(curF).put(var, null);
                } else if (functionExecute()) {
                    assign_op = true;
                }
            }
        }
        if (add && !assign_op) {
            rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
        }
        if (assign_op) {
            while (!stack.empty()) {
                rpnMap.get(curF).add(stack.pop());
            }
        }
        curToken = assign_op ? curToken : oldPos;
        return assign_op;
    }

    private boolean functionExecute() {
        boolean func = false;
        int oldPos = curToken;
        String funcName;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.containsKey("block_" + getLastTokenValue())) {
            funcName = getLastTokenValue();
            if (getTokenLexeme() == LexemeTypes.L_RB) {
                if (params(funcName)) {
                    if (getTokenLexeme() == LexemeTypes.R_RB) {
                        if (getTokenLexeme() == LexemeTypes.SEMI) {
                            rpnMap.get(curF).add(funcName);
                            func = true;
                        }
                    }
                } else if (getTokenLexeme() == LexemeTypes.R_RB) {
                    if (getTokenLexeme() == LexemeTypes.SEMI) {
                        rpnMap.get(curF).add(funcName);
                        func = true;
                    }
                }
            }
        }

        curToken = func ? curToken : oldPos;
        return func;
    }

    private boolean params(String funcName) {
        boolean param = false;
        int oldPos = curToken;
        if (getTokenLexeme() == LexemeTypes.VAR && varTable.get(curF).containsKey(getLastTokenValue())) {
            rpnMap.get(curF).add("arg_" + getLastTokenValue());
            if (getTokenLexeme() == LexemeTypes.COM) {
                if (params(curF)) {
                    param = true;
                }
            } else {
                curToken--;
                param = true;
            }
        } else {
            curToken--;
            if (getTokenLexeme() == LexemeTypes.R_RB && varTable.get("block_" + funcName).size() == 1) {
                curToken--;
                param = true;
            }
        }

        curToken = param ? curToken : oldPos;
        return param;
    }

    private boolean stmt() {
        boolean value = false;

        if (value()) {
            //noinspection StatementWithEmptyBody
            while (opValue()) {
            }
            value = true;
        }
        return value;
    }

    private boolean opValue() {
        boolean opVal = false;
        int oldPos = curToken;

        if (getTokenLexeme() == LexemeTypes.OP) {
            String op = getLastTokenValue();
            while (getPriority(op) <= getPriority(stack.peek())) {
                rpnMap.get(curF).add(stack.pop());
            }
            stack.push(op);
            if (value()) {
                opVal = true;
            }
        }
        curToken = opVal ? curToken : oldPos;
        return opVal;
    }

    private boolean value() {
        if (getTokenLexeme() == LexemeTypes.VAR) {
            rpnMap.get(curF).add(getLastTokenValue());
            if (!varTable.get(curF).containsKey(getLastTokenValue())) {
                rpnMap.get(curF).remove(rpnMap.get(curF).size() - 1);
                curToken--;
                return false;
            }
            if (varTable.get(curF).get(getLastTokenValue()) instanceof LinkedList
                    || varTable.get(curF).get(getLastTokenValue()) instanceof HashSet
                || varTable.get(curF).get(getLastTokenValue()) instanceof Semaphore
            ) {
                if (getTokenLexeme() == LexemeTypes.DOT) {
                    if (getTokenLexeme() == LexemeTypes.COL_OP && (getLastTokenValue().equals("get")
                            || getLastTokenValue().equals("size"))) {
                        String col_op = getLastTokenValue();
                        if (bkt_value()) {
                            rpnMap.get(curF).add(col_op);
                            return true;
                        }
                    }
                }

            } else {
                return true;
            }

        } else {
            curToken--;
        }

        if (getTokenLexeme() == LexemeTypes.DIGIT) {
            rpnMap.get(curF).add(getLastTokenValue());
            return true;
        } else {
            curToken--;
        }

        String last = getLastTokenValue();
        if (getTokenLexeme() == LexemeTypes.R_RB && last.equals("(")) {
            curToken--;
            return true;
        } else {
            curToken--;
        }

        return bkt_value();
    }

    private boolean bkt_value() {
        boolean bkt = false;
        int oldPos = curToken;

        if (getTokenLexeme() == LexemeTypes.L_RB) {
            stack.push(getLastTokenValue());
            if (stmt()) {
                if (getTokenLexeme() == LexemeTypes.R_RB) {
                    while (!stack.peek().equals("(")) {
                        rpnMap.get(curF).add(stack.pop());
                    }
                    stack.pop();
                    bkt = true;
                }
            }
        }
        curToken = bkt ? curToken : oldPos;
        return bkt;
    }


    private boolean figure_bkt() {
        boolean bkt = false;
        if (init() || assign() || set_stmt()) {
            bkt = true;
        }
        return bkt;
    }


    private LexemeTypes getTokenLexeme() {
        try {
            return tokens.get(curToken++).getLexeme();
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("Error: Lexeme expected");
            System.exit(2);
        }
        return null;
    }


    private String getLastTokenValue() {
        return tokens.get(curToken - 1).getValue();
    }

    private int getPriority(String str) {
        switch (str) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "^":
            case "/":
            case "%":
                return 2;
            case "=":
            case "(":
                return 0;
            default:
                System.err.println("Error: in " + str);
                System.exit(5);
                return 0;
        }
    }

    private List<Token> tokens;
    private Stack<String> stack = new Stack<>();
    public HashMap<String, Map<String, Object>> varTable = new HashMap<>();
    public HashMap<String, List<String>> rpnMap = new HashMap<>();
    private String curF;
    private String prevF;
    private int curToken;
    private int p0;
    private int p1;
}
