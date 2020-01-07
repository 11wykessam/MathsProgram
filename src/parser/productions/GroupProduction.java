package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;
import tokens.LBracketToken;
import tokens.RBracketToken;

import java.io.IOException;

/**
 * Represents the group production.
 * Group -> ( Sum )
 */
public class GroupProduction extends Production {

    /**
     * Used to recognise itself.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @return
     */
    @Override
    public boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException {

        // mark position before lexing.
        int previousPos = lexer.getPosition();

        // attempt to recognise production.
        LBracketToken lBracketToken = new LBracketToken();
        RBracketToken rBracketToken = new RBracketToken();
        SumProduction sumProduction = new SumProduction();
        TreeNode<GrammarComponent> sumNode = new TreeNode<>(sumProduction);

        if (recogniseToken(lexer, lBracketToken) && sumProduction.recognise(lexer, sumNode) && recogniseToken(lexer, rBracketToken)) {
            // add all children to tree.
            node.addChild(new TreeNode<>(lBracketToken));
            node.addChild(sumNode);
            node.addChild(new TreeNode<>(rBracketToken));
            return true;
        }

        // otherwise rewind and return false.
        lexer.setPosition(previousPos);
        return false;

    }
}
