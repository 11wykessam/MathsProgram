package parser.productions;

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;

import java.io.IOException;

/**
 * Represents the factor production.
 * Factor -> Power | Term.
 */
public class FactorProduction extends Production {

    /**
     * Attempts to recognise itself.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @param node The {@link TreeNode<GrammarComponent>} object that the production will be contained in.
     * @return
     * @throws IOException
     */
    @Override
    public boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException {

        // mark position before attempt to recognise itself.
        int previousPos = lexer.getPosition();

        // firstly attempt to recognise power production.
        PowerProduction powerProduction = new PowerProduction();
        TreeNode<GrammarComponent> powerNode = new TreeNode<>(powerProduction);
        if (powerProduction.recognise(lexer, powerNode)) {
            node.addChild(powerNode);
            return true;
        }

        // attempt to recognise term production.
        TermProduction termProduction = new TermProduction();
        TreeNode<GrammarComponent> termNode = new TreeNode<>(termProduction);
        if (termProduction.recognise(lexer, termNode)) {
            node.addChild(termNode);
            return true;
        }

        // otherwise rewind lexer.
        lexer.setPosition(previousPos);
        return false;

    }
}
