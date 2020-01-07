package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;
import tokens.EOIToken;
import tokens.VariableToken;
import tokens.operators.EqualsToken;

import java.io.IOException;

/**
 * Represents assignment production.
 * Assignment -> Variable = Sum End.
 */
public class AssignmentProduction extends Production {

    /**
     * Attempts to recognise itself.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @param node The {@link TreeNode<GrammarComponent>} object that the production will be contained in.
     * @return
     * @throws IOException
     */
    @Override
    public boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException {

        // mark position before lexing.
        int previousPos = lexer.getPosition();

        // attempt to recognise production.
        VariableToken variableToken = VariableToken.getInstance();
        EqualsToken equalsToken = new EqualsToken();
        SumProduction sumProduction = new SumProduction();
        TreeNode<GrammarComponent> sumNode = new TreeNode<>(sumProduction);
        EOIToken eoiToken = new EOIToken();

        if (recogniseToken(lexer, variableToken) && recogniseToken(lexer, equalsToken) && sumProduction.recognise(lexer, sumNode) && recogniseToken(lexer, eoiToken)) {

            // add all to tree.
            node.addChild(new TreeNode<>(variableToken));
            node.addChild(new TreeNode<>(equalsToken));
            node.addChild(sumNode);
            node.addChild(new TreeNode<>(eoiToken));

            return true;

        }

        // otherwise reset lexer and return false.
        lexer.setPosition(previousPos);
        return false;

    }
}
