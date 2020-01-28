import Interperer.Executor;
import Lexeme.Lexer;
import Lexeme.Token;
import Parsing.Optimization;
import Parsing.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File("program.txt");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[(int) file.length()];
        try {
            fis.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String input = new String(data);

        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.getTokens();

        Parser parser = new Parser(tokens);

        if (parser.lang()) {
            //Optimization optimization = new Optimization(parser.varTable, parser.rpnMap);
            //optimization.execute();

            Executor executor = new Executor();
            System.out.println("Results:");
            executor.start(parser);
        }
    }
}
