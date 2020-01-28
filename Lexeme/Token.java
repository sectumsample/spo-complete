package Lexeme;

public class Token {
    Token(LexemeTypes lexemeTypes, String value) {
        this.lexemeTypes = lexemeTypes;
        this.value = value;
    }

    public LexemeTypes getLexeme() {
        return lexemeTypes;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return lexemeTypes + "'" + value + "'";
    }

    private LexemeTypes lexemeTypes;
    private String value;
}
