import common.GrammarComponent;
import compiler.MathsCompiler;
import exceptions.VariableUndefinedException;
import lexer.ExpressionLexer;
import parser.productions.StatementProduction;
import parser.syntaxtree.TreeNode;

import java.io.*;

/**
 * Simple program for evaluating maths equations.
 * @author Samuel Wykes.
 */
public class MathsProgram implements Runnable {

    public static void main(String[] args) throws IOException {

        MathsProgram mathsProgram = new MathsProgram();
        mathsProgram.run();

    }


    /**
     * Called when application is ran.
     */
    @Override
    public void run() {

        // remember line number.
        int lineNo = 1;

        // the compiler.
        MathsCompiler compiler = new MathsCompiler();

        // the input stream.
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // repeat until user closes the program.
        while (true) {
            System.out.print("[" + lineNo + "]: ");

            // extract input and attempt to build string.
            try {
                String expression = reader.readLine();

                // if close is typed close program.
                if (expression.equals("close")) {
                    System.out.println("Goobye!");
                    return;
                }

                ExpressionLexer lexer = new ExpressionLexer(new ByteArrayInputStream(expression.getBytes()));

                // attempt to extract AST.
                StatementProduction statementProduction = new StatementProduction();
                TreeNode<GrammarComponent> root = new TreeNode<>(statementProduction);
                boolean success = statementProduction.recognise(lexer, root);

                // check if syntax was correct.
                if (!success) {
                    System.out.println("Invalid Syntax");
                    continue;
                }

                try {
                    compiler.execute(root);
                }
                catch (VariableUndefinedException e) {
                    System.out.println("Variable Not Defined");
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            lineNo++;

        }

    }
}
