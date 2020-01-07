package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;

import java.io.IOException;

/**
 * Represents statement production.
 * StatementProduction -> Assignment | Calculation.
 */
public class StatementProduction extends Production{

    /**
     * Attempts to recognise itself.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @param node The {@link TreeNode<GrammarComponent>} object that the production will be contained in.
     * @return
     * @throws IOException
     */
    @Override
    public boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException {

        // mark position in lexer.
        int previousPos = lexer.getPosition();

        // attempt to recognise an assignment.
        AssignmentProduction assignmentProduction = new AssignmentProduction();
        TreeNode<GrammarComponent> assignmentNode = new TreeNode<>(assignmentProduction);
        if (assignmentProduction.recognise(lexer, assignmentNode)) {
            node.addChild(assignmentNode);
            return true;
        }
        else {
            lexer.setPosition(previousPos);
        }

        // attempt to recognise a calculation.
        CalculationProduction calculationProduction = new CalculationProduction();
        TreeNode<GrammarComponent> calculationNode = new TreeNode<>(calculationProduction);
        if (calculationProduction.recognise(lexer, calculationNode)) {
            node.addChild(calculationNode);
            return true;
        }

        else {
            lexer.setPosition(previousPos);
            return false;
        }


    }
}
