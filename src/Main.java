import antlr.IMPGrammarLexer;
import antlr.IMPGrammarParser;
import antlr.MyVisitor;
import ast.ASTPrinter;
import ast.Interpretor;
import exception.DivideByZero;
import exception.UnassignedVar;
import lexer.Parser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.*;

public class Main {

    private static final String nameInput = "input";
    private static final String nameOutput = "arbore";
    private static final String nameOutputInterpretor = "output";
    private static final String nameOutputBonus = "arbore-b";

    public static void main(String[] args) throws IOException, DivideByZero, UnassignedVar {

        runBonus();

        Parser parser = new Parser(new BufferedReader(new FileReader(nameInput)));
        parser.yylex();

        parser.yyclose();

        ASTPrinter printer = new ASTPrinter(nameOutput);
        parser.getProgram().accept(printer);
        printer.close();

        BufferedWriter interpretorWriter = new BufferedWriter(new FileWriter(nameOutputInterpretor));
        if (parser.getExceptions().size() != 0) {
            interpretorWriter.write(parser.getExceptions().get(0).getMessage() + "\n");
            interpretorWriter.close();
        } else {

            Interpretor interpretor = new Interpretor(parser.getVariable(), interpretorWriter);
            try {
                parser.getProgram().accept(interpretor);

            } catch (DivideByZero divideByZero) {
                interpretor.getBufferedWriter().write(divideByZero.getMessage() + "\n");

            } catch (UnassignedVar unassignedVar) {
                interpretor.getBufferedWriter().write(unassignedVar.getMessage() + "\n");

            }finally {
                interpretor.close();
            }
        }
    }

    private static void runBonus() throws IOException {
        CharStream charStream = CharStreams.fromFileName(nameInput);
        IMPGrammarLexer lexer = new IMPGrammarLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        IMPGrammarParser parser = new IMPGrammarParser(commonTokenStream);

        ParserRuleContext tree =  parser.program();
        MyVisitor visitor = new MyVisitor(nameOutputBonus);
        tree.accept(visitor);
        visitor.close();
    }

}
