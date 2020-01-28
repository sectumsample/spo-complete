package Parsing;

import java.util.*;

public class Optimization {
    public Optimization(HashMap<String, Map<String, Object>> varTable, HashMap<String, List<String>> rpnSet) {
        this.varTable = varTable;
        this.rpnSet = rpnSet;
        this.rpnOpt = new ArrayList<>();
        this.linear = new ArrayList<>();
    }

    public void execute() {

        for (String funcName : rpnSet.keySet()) {
            curF = funcName;
            rpnList = new ArrayList<>(rpnSet.get(funcName));
            rpnCopy = new ArrayList<>(rpnList);
            rpnOpt = new ArrayList<>();
            System.out.println("\n" + funcName);
            System.out.println(rpnList);
            while (findLinear()) {
                optimise();
            }
            if (rpnOpt.size() < rpnCopy.size()) {
                System.out.println("Result:\n" + rpnOpt);
                rpnSet.replace(funcName, rpnOpt);
            } else {
                System.out.println("Result:\n" + rpnCopy);
            }
        }
    }

    private void optimise() {
        String tmp;
        String[] trd;
        int result;

        for (int i = 0; i < linear.size(); i++) {

            tmp = linear.get(i).replaceAll("[\\[|\\]]", "");
            trd = tmp.split(", ");

            if (trd[2].matches("^([-+*/])")) {
                if (trd[0].matches("([a-zA-Z]|_)") && varTable.get(curF).get(trd[0]) != null) {
                    trd[0] = String.valueOf(varTable.get(curF).get(trd[0]));
                }
                if (trd[1].matches("([a-zA-Z]|_)") && varTable.get(curF).get(trd[1]) != null) {
                    trd[1] = String.valueOf(varTable.get(curF).get(trd[1]));
                }
                if (isNumeric(trd[0]) && isNumeric(trd[0])) {
                    switch (trd[2]) {
                        case "+":
                            result = Integer.parseInt(trd[0]) + Integer.parseInt(trd[1]);
                            linear.set(i, "C(" + result + ", 0)");
                            break;
                        case "-":
                            result = Integer.parseInt(trd[0]) - Integer.parseInt(trd[1]);
                            linear.set(i, "C(" + result + ", 0)");
                            break;
                        case "*":
                            result = Integer.parseInt(trd[0]) * Integer.parseInt(trd[1]);
                            linear.set(i, "C(" + result + ", 0)");
                            break;
                        case "/":
                            result = Integer.parseInt(trd[0]) / Integer.parseInt(trd[1]);
                            linear.set(i, "C(" + result + ", 0)");
                            break;

                    }
                }
            }

            if (trd[0].matches("([a-zA-Z]|_)+\\w") && !trd[1].matches("([a-zA-Z]|_|\\^*)+\\w") && trd[2].equals("=")) {
                if (!varTable.containsKey(trd[0])) {
                    varTable.get(curF).put(trd[0], Integer.parseInt(trd[1]));
                } else {
                    varTable.get(curF).replace(trd[0], Integer.parseInt(trd[1]));
                }
            }

            if (trd[1].contains("^") && linear.get(Integer.parseInt(trd[1].replace("^", "")) - 1).contains("C(")) {
                tmp = linear.get(Integer.parseInt(trd[1].replace("^", "")) - 1).replaceAll("[C(|)]", "");
                trd[1] = tmp.split(", ")[0];
                linear.set(i, Arrays.toString(trd));
                if (!varTable.get(curF).containsKey(trd[0])) {
                    varTable.get(curF).put(trd[0], Integer.parseInt(trd[1]));
                } else {
                    varTable.get(curF).replace(trd[0], Integer.parseInt(trd[1]));
                }
            }
            System.out.println(linear);
        }

        for (String s : linear) {
            if (!s.contains("C(")) {
                rpnOpt.addAll(Arrays.asList(s.replaceAll("[\\[|\\]]", "").split(", ")));
            }
        }
        if (rpnOpt.contains("~")) {
            rpnOpt.set(rpnOpt.indexOf("~"), rpnOpt.size() + "");
        }
        //System.out.println(tableOfVariables);
        linear.clear();
    }

    private boolean findLinear() {
        int i = 0;

        while (rpnList.size() > 0) {
            if (rpnList.get(i).matches("(==|!=|<=|>=|<|>|print|(block_+\\w*)|(thread_+\\w*))|'([^']*?)'")) {
                if (linear.size() > 0) {
                    return true;
                }

                if (rpnList.get(i).matches("'([^']*?)'") && rpnList.get(i + 1).equals("print")) {
                    rpnOpt.addAll(rpnList.subList(0, i + 2));
                    rpnList = rpnList.subList(i + 2, rpnList.size());
                    i = 0;
                } else if (!rpnList.get(i).equals("print") && !rpnList.get(i).contains("block_")) {
                    rpnOpt.addAll(rpnList.subList(0, i + 3));
                    rpnList = rpnList.subList(i + 3, rpnList.size());
                    i = 0;
                } else {
                    rpnOpt.add(rpnList.get(i));
                    rpnList = rpnList.subList(i + 1, rpnList.size());
                    i = 0;
                }
            } else if (rpnList.get(i).matches("^([-+*/=])")) {
                if (i - 2 < 0) {/*если [b, = ]*/
                    linear.add(String.valueOf(rpnList.subList(0, i + 1)));
                    rpnList.subList(0, i + 1).clear();
                    i = 0;
                } else {
                    linear.add(String.valueOf(rpnList.subList(i - 2, i + 1)));
                    rpnList.subList(i - 2, i + 1).clear();
                    i = 0;
                    if (rpnList.size() > 1 && rpnList.get(1).equals("=")) {
                        rpnList.add(1, "^" + linear.size());
                    }
                }
            } else if (rpnList.get(i).equals("!") && Integer.parseInt(rpnList.get(i - 1)) < Collections.indexOfSubList(rpnCopy, rpnList)) {
                rpnOpt.addAll(rpnCopy.subList(Integer.parseInt(rpnList.get(i - 1)) + 5, Collections.indexOfSubList(rpnCopy, rpnList) + 2));
                rpnList.subList(0, i + 1).clear();
                i = 0;
                linear.clear();
            } else if (rpnOpt.size() > 2 && rpnOpt.get(rpnOpt.size() - 1).equals("!F")) {
                rpnOpt.set(rpnOpt.size() - 2, "~");
                i++;
            } else if (varTable.containsKey("block_" + rpnList.get(i))) {
                if (linear.size() > 0) {
                    return true;
                } else {
                    rpnOpt.addAll(rpnList.subList(0, i + 2));
                    rpnList.subList(0, i + 2).clear();
                    i = 0;
                }
            } else if (rpnList.get(i).equals("return")) {
                if (linear.size() > 0) {
                    return true;
                } else {
                    rpnOpt.addAll(rpnList.subList(0, i + 1));
                    rpnList.subList(0, i + 1).clear();
                    i = 0;
                }
            } else {
                i++;
            }
        }
        return linear.size() != 0;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private HashMap<String, Map<String, Object>> varTable;
    private List<String> rpnList;
    private HashMap<String, List<String>> rpnSet;
    private List<String> rpnOpt;
    private List<String> linear;
    private List<String> rpnCopy;
    private String curF;
}
