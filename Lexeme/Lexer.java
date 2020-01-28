package Lexeme;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {
    public Lexer(String input) {
        this.input.append(input);
    }

    public List<Token> getTokens() {
        List<Token> tokens = new ArrayList<>();
        while (input.length() != 0 && !flag) {
            deleteNonPrintable();

            if (findNextToken()) {
                tokens.add(new Token(lTypes, value));
            } else {
                flag = true;
            }
        }
        return tokens;
    }

    private boolean findNextToken() {
        for (LexemeTypes lexeme : LexemeTypes.values()) {
            Matcher matcher = lexeme.getPattern().matcher(input.toString());
            if (matcher.find()) {
                lTypes = lexeme;
                value = input.substring(matcher.start(), matcher.end());
                input.delete(matcher.start(), matcher.end());
                return true;
            }
        }
        return false;
    }

    private void deleteNonPrintable() {
        int pos = 0;
        while (input.charAt(pos) == ' ' || input.charAt(pos) == '\n' || input.charAt(pos) == '\r'
                || input.charAt(pos) == '\t') {
            pos++;
        }
        if (pos > 0) {
            input.delete(0, pos);
        }
    }

    private StringBuilder input = new StringBuilder();
    private LexemeTypes lTypes;
    private String value;
    private boolean flag = false;
}
