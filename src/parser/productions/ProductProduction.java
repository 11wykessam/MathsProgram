package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;
import tokens.operators.DivisionToken;
import tokens.operators.MultiplicationToken;

import java.io.IOException;

/**
 * Represents product production.
 * Product -> Factor ((* factor)|(/ factor))*
 */
public class ProductProduction extends Production {

    /**
     * Attempt to recognise itself.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @param node The {@link TreeNode<GrammarComponent>} object that the production will be contained in.
     * @return
     * @throws IOException
     */
    @Override
    public boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException {

        // mark position.
        int previousPosition = lexer.getPosition();

        // attempt to recognise a single factor.
        FactorProduction firstFactor = new FactorProduction();
        TreeNode<GrammarComponent> firstNode = new TreeNode<>(firstFactor);
        if (firstFactor.recognise(lexer, firstNode)) {
            node.addChild(firstNode);
        }
        // otherwise rewind and return false.
        else {
            lexer.setPosition(previousPosition);
            return false;
        }

        // now attempt to recognise repeated patterns.
        while (true) {
            // mark previous position.
            previousPosition = lexer.getPosition();

            // attempt to recognise * or / followed by factor.
            MultiplicationToken multiplicationToken = new MultiplicationToken();
            DivisionToken divisionToken = new DivisionToken();
            FactorProduction repeatedFactorProduction = new FactorProduction();
            TreeNode<GrammarComponent> repeatedFactorNode = new TreeNode<>(repeatedFactorProduction);

            // first check for multiplication followed by factor.
            if (recogniseToken(lexer, multiplicationToken) && repeatedFactorProduction.recognise(lexer, repeatedFactorNode)) {
                node.addChild(new TreeNode<>(multiplicationToken));
                node.addChild(repeatedFactorNode);
                continue;
            }
            else {
                lexer.setPosition(previousPosition);
            }

            // next check for division followed by factor.
            if (recogniseToken(lexer, divisionToken) && repeatedFactorProduction.recognise(lexer, repeatedFactorNode)) {
                node.addChild(new TreeNode<>(divisionToken));
                node.addChild(repeatedFactorNode);
                continue;
            }
            else {
                lexer.setPosition(previousPosition);
            }

            // if neither productions are recognise break.
            break;
        }

        return true;

    }
}
