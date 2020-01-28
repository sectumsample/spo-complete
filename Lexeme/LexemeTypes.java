package Lexeme;

import java.util.regex.Pattern;

public enum LexemeTypes {
    COL_OP("(add|get|remove|size|contains)"),
    RUN("run"),
    JOIN("join"),
    LOCK("lock"),
    OP("^(\\-|\\+|\\*|\\/)"),
    COMP_OP("(==|!=|<=|>=|<|>)"),
    LOG_OP("(\\&\\&|\\|\\|)"),
    ASSIGN_OP("="),
    DOT("\\."),
    UNLOCK("unlock"),
    L_RB("\\("),
    R_RB("\\)"),
    L_FB("\\{"),
    RETURN("return"),
    R_FB("\\}"),
    COM(","),
    SEMI(";"),
    IF("if"),
    WHILE("while"),
    THEN("then"),
    ELSE("else"),
    FOR("for"),
    PRINT("print"),
    TYPE("(int|LinkedList|HashSet|Thread|void|Mutex)"),
    STRING("'([^']*?)'"),
    FUNCTION("def"),
    VAR("([a-zA-Z]|_)+\\w*"),
    DIGIT("(0|[1-9][0-9]*)");

    private Pattern pattern;

    LexemeTypes(String str) {
        this.pattern = Pattern.compile("^" + str);
    }

    public Pattern getPattern() {
        return pattern;
    }

}
