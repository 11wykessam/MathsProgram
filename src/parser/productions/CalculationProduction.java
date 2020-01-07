package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;
import tokens.EOIToken;

import java.io.IOException;

/**
 * Represents calculation production.
 * Calculation -> Sum End.
 */
public class CalculationProduction extends Production {

    /**
     * Attempts to recognise itself.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @param node The {@link TreeNode<GrammarComponent>} object that the production will be contained in.
     * @return
     * @throws IOException
     */
    @Override
    public boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException {

        // mark previous position.
        int previousPos = lexer.getPosition();

        // attempt to recognise itself.
        SumProduction sumProduction = new SumProduction();
        TreeNode<GrammarComponent> sumNode = new TreeNode<>(sumProduction);
        EOIToken eoiToken = new EOIToken();

        if (sumProduction.recognise(lexer, sumNode) && recogniseToken(lexer, eoiToken)) {

            node.addChild(sumNode);
            node.addChild(new TreeNode<>(eoiToken));
            return true;

        }

        // otherwise rewind and return false.
        lexer.setPosition(previousPos);
        return false;

    }
}
