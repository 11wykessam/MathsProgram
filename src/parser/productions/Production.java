package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;
import tokens.NumberToken;
import tokens.Token;
import tokens.VariableToken;

import java.io.IOException;

/**
 * Class used to represent productions in a grammar.
 */
public abstract class Production extends GrammarComponent {

    /**
     * Attempts to recognise itself from the lexer's tokens.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @param node The {@link TreeNode<GrammarComponent>} object that the production will be contained in.
     * @return Boolean representing whether the production recognises itself.
     */
    public abstract boolean recognise(ExpressionLexer lexer, TreeNode<GrammarComponent> node) throws IOException;

    /**
     * Attempts to recognise a given token.
     * @param lexer The {@link ExpressionLexer} tokens are being received from.
     * @param token The {@link Token} object attempting to recognise itself.
     * @return
     */
    public boolean recogniseToken(ExpressionLexer lexer, Token token) throws IOException {

        // mark previous position.
        int previousPosition = lexer.getPosition();

        // extract next token.
        Token next = lexer.lex();
        // check if token recognises itself.
        if (next.getClass().equals(token.getClass())) {
            // if variable or number extract values.
            if (next instanceof NumberToken) ((NumberToken) token).setValue(((NumberToken) next).getValue());
            else if (next instanceof VariableToken) ((VariableToken) token).setName(((VariableToken) next).getName());
            return true;
        }
        // if not rewind lexer.
        else {
            lexer.setPosition(previousPosition);
            return false;
        }

    }

}
