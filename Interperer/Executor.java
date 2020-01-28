package Interperer;

import Parsing.Parser;
import Structures.HashSet;
import Structures.LinkedList;
import Lexeme.Lexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Executor {
    public void start(Parser parser) {
        this.rnpMap = parser.rpnMap;
        this.varTableMap = parser.varTable;
        curF = "main";
        execute(rnpMap.get(curF), varTableMap.get(curF), new Stack<>());
    }

    void execute(List<String> reversePolishNotation, Map<String, Object> tableOfVar, Stack<String> stack) {
        Object first, second, result;

        for (int i = 0; i <= reversePolishNotation.size() - 1; i++) {
            //System.out.println(reversePolishNotation.get(i));
            switch (reversePolishNotation.get(i)) {
                case "run":
                    String threadName = stack.peek();
                    first = tableOfVar.get(stack.pop());
                    if (first instanceof Thread) {
                        String funcName = stack.pop().replace("thread_", "block_");
                        if (rnpMap.containsKey(funcName)) {
                            if (stack.peek().contains("arg_")) {
                                varTableMap.get(funcName).put("arg",
                                        varTableMap.get(curF).get(stack.pop().replace("arg_", "")));
                            }
                            Runnable task = () -> {
                                execute(rnpMap.get(funcName), varTableMap.get(funcName), new Stack<>());
                            };
                            first = new Thread(task);
                            tableOfVar.replace(threadName, first);
                            ((Thread) first).start();
                        }
                    } else {
                        System.out.println(first + " is not thread");
                    }
                    break;

                case "join":
                    first = getValue(tableOfVar, stack);
                    if (first instanceof Thread) {
                        try {
                            ((Thread) first).join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "add":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    if (second instanceof HashSet) {
                        ((HashSet) second).add(first);
                    } else {
                        ((LinkedList) second).add(first);
                    }
                    break;

                case "remove":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    if (second instanceof HashSet) {
                        ((HashSet) second).remove(first);
                    } else {
                        ((LinkedList) second).remove(first);
                    }
                    break;

                case "lock":
                    first = getValue(tableOfVar, stack);
                    if (first instanceof Semaphore) {
                        try {
                            ((Semaphore) first).acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "unlock":
                    first = getValue(tableOfVar, stack);
                    if (first instanceof Semaphore) {
                        ((Semaphore) first).release();
                    }
                    break;

                case "get":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    stack.push("" + (((LinkedList) second).get((Integer) first)));
                    break;

                case "contains":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    boolean b;
                    if (second instanceof LinkedList) {
                        b = ((LinkedList) second).contains(first) != -1;
                        stack.push(String.valueOf(b));
                    } else {
                        b = ((HashSet) second).contains(first);
                        stack.push(String.valueOf(b));
                    }
                    break;


                case "size":
                    first = getValue(tableOfVar, stack);
                    stack.push("" + ((HashSet) first).size());
                    break;

                case "print":
                    if (stack.empty()) {
                        System.out.println(tableOfVar);
                    } else if (tableOfVar.containsKey(stack.peek())) {
                        System.out.println(stack.peek() + " = " + tableOfVar.get(stack.pop()));
                    } else {
                        String s = stack.pop();
                        System.out.println(s.substring(1, s.length()-1));
                    }
                    break;

                case "=":
                    first = getValue(tableOfVar, stack);
                    tableOfVar.put(stack.pop(), first);
                    break;

                case "+":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    result = (Integer) first + (Integer) second;
                    stack.push(String.valueOf(result));
                    break;

                case "-":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    result = (Integer) second - (Integer) first;
                    stack.push(String.valueOf(result));
                    break;

                case "/":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    result = (Double) second / (Double) first;
                    stack.push(String.valueOf(result));
                    break;

                case "%":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    result = (Double) second % (Double) first;
                    stack.push(String.valueOf(result));
                    break;

                case "^":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    result = (int) Math.pow((Integer) first, (Integer) second);
                    stack.push(String.valueOf(result));
                    break;

                case "*":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    result = (Integer) first * (Integer) second;
                    stack.push(String.valueOf(result));
                    break;

                case "!=":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    boolean b1 = first.equals(second);
                    stack.push(String.valueOf(b1));
                    break;

                case "==":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    b1 = first == second;
                    stack.push(String.valueOf(b1));
                    break;

                case "<":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    b1 = (Integer) second < (Integer) first;
                    stack.push(String.valueOf(b1));
                    break;

                case ">":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    b1 = (Integer) second > (Integer) first;
                    stack.push(String.valueOf(b1));
                    break;

                case "<=":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    b1 = (Integer) second <= (Integer) first;
                    stack.push(String.valueOf(b1));
                    break;

                case ">=":
                    first = getValue(tableOfVar, stack);
                    second = getValue(tableOfVar, stack);
                    b1 = (Integer) second >= (Integer) first;
                    stack.push(String.valueOf(b1));
                    break;

                case "!F":
                    first = getValue(tableOfVar, stack);
                    boolean c = stack.pop().equals("true");
                    i = c ? i : (Integer) first;
                    break;

                case "!":
                    i = (Integer) getValue(tableOfVar, stack) - 1;
                    break;

                case "return":
                    tableOfVar.put("return", tableOfVar.get(stack.pop()));
                    break;

                default:
                    if (!reversePolishNotation.get(i).contains("block_") && !rnpMap.containsKey("block_" + reversePolishNotation.get(i))) {
                        stack.push(String.valueOf(reversePolishNotation.get(i)));
                    }
                    if (rnpMap.containsKey("block_" + reversePolishNotation.get(i))) {
                        if (stack.peek().contains("arg_")) {
                            varTableMap.get("block_" + reversePolishNotation.get(i)).put("arg",
                                    varTableMap.get(curF).get(stack.pop().replace("arg_", "")));
                        }
                        execute(rnpMap.get("block_" + reversePolishNotation.get(i)), varTableMap.get("block_" + reversePolishNotation.get(i)), new Stack<>());
                    }
                    break;
            }
        }
        System.out.println();
    }

    private Object getValue(Map<String, Object> tableOfVar, Stack<String> stack) {
        String value = stack.peek();
        if (varTableMap.containsKey("block_" + value)) {
            varTableMap.get("block_" + stack.pop()).put("arg",
                    varTableMap.get(curF).get(stack.pop()));
            execute(rnpMap.get("block_" + value), varTableMap.get("block_" + value), new Stack<>());
            return varTableMap.get("block_" + value).get("return");
        }
        Lexer lexer = new Lexer(stack.peek());
        switch (lexer.getTokens().get(0).getLexeme()) {
            case VAR:
                return tableOfVar.get(stack.pop());
            case DIGIT:
                return Integer.valueOf(stack.pop());
            default:
                System.err.println();
                System.exit(50);
        }
        return -1;
    }

    HashMap<String, Map<String, Object>> varTableMap = new HashMap<>();
    HashMap<String, List<String>> rnpMap = new HashMap<>();
    String curF;
}
