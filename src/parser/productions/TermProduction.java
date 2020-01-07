package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;
import tokens.NumberToken;
import tokens.VariableToken;

import java.io.IOException;

/**
 * Represents the term production.
 * Term -> Group | Variable | Number.
 */
public class TermProduction extends Production {

    /**
     * Attempts to recognise itself.
     * @param lexer The lexer tokens are being received from.
     * @return
     */
    @Override
    public boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException {

        // mark the position before attempting to recognise itself.
        int previousPos = lexer.getPosition();

        // firstly attempt to recognise a variable.
        VariableToken variableToken = VariableToken.getInstance();
        if (recogniseToken(lexer, variableToken)) {
            // create a new tree node and add it to the current.
            TreeNode<GrammarComponent> child = new TreeNode<>(variableToken);
            node.addChild(child);
            return true;
        }
        // next attempt to recognise a number.
        NumberToken numberToken = NumberToken.getInstance();
        if (recogniseToken(lexer, numberToken)) {
            // create a new tree node and add it to the current.
            TreeNode<GrammarComponent> child = new TreeNode<>(numberToken);
            node.addChild(child);
            return true;
        }
        // next attempt to recognise a group production.
        GroupProduction groupProduction = new GroupProduction();
        TreeNode<GrammarComponent> groupNode = new TreeNode<>(groupProduction);
        if (groupProduction.recognise(lexer, groupNode)) {
            node.addChild(groupNode);
            return true;
        }

        // if the production fails to recognise itself then rewind the lexer.
        lexer.setPosition(previousPos);
        return false;

    }

}
