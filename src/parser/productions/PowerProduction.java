package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;
import tokens.operators.ExponentToken;

import java.io.IOException;

/**
 * Represents the power production.
 * Power -> Term ^ Factor.
 */
public class PowerProduction extends Production{

    /**
     * Attempt to recognise itself.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @param node The {@link TreeNode<GrammarComponent>} object that the production will be contained in.
     * @return
     * @throws IOException
     */
    @Override
    public boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException {

        // mark initial position.
        int previousPos = lexer.getPosition();

        // attempt to recognise all productions.
        TermProduction termProduction = new TermProduction();
        TreeNode<GrammarComponent> termNode = new TreeNode<>(termProduction);
        ExponentToken exponentToken = new ExponentToken();
        FactorProduction factorProduction = new FactorProduction();
        TreeNode<GrammarComponent> factorNode = new TreeNode<>(factorProduction);

        // if all productions and tokens are recognised then add them to tree.
        if (termProduction.recognise(lexer, termNode) && recogniseToken(lexer, exponentToken) && factorProduction.recognise(lexer, factorNode)) {
            node.addChild(termNode);
            node.addChild(new TreeNode<>(exponentToken));
            node.addChild(factorNode);
            return true;
        }

        // otherwise rewind lexer.
        lexer.setPosition(previousPos);
        return false;

    }
}
