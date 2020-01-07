package lexer;

import common.GrammarComponent;
import parser.productions.*;
import parser.syntaxtree.TreeNode;
import tokens.*;
import tokens.operators.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Class reads from an input and creates a stream of tokens as output.
 */
public class ExpressionLexer {

    // the stream from which input is being taken from.
    private InputStream inputStream;

    // stores all previously read tokens.
    private ArrayList<Token> tokenStream = new ArrayList<>();
    // current index in token stream being read from. (-1) if at end of token stream.
    private int position = -1;

    public ExpressionLexer(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Lexes the next token from either the input stream list or from input stream.
     * @return {@link Token} object.
     */
    public Token lex() throws IOException {
        Token token;
        // if at end of token stream lex directly from input stream.
        if (position == -1) {
            token = next();
            tokenStream.add(token);
        }
        // otherwise read along the token stream.
        else {
            token = tokenStream.get(position);
            position++;
            // if position reaches past the end of the list then reset it to -1.
            if (position == tokenStream.size()) position = -1;
        }
        return token;
    }

    /**
     * Extracts the next token from the input stream.
     * @return {@link Token} object.
     * @throws IOException thrown if there is some problem with reading from input stream.
     */
    private Token next() throws IOException {

        byte currentByte = (byte) inputStream.read();

        // check for end of input.
        if (currentByte == -1) return new EOIToken();
        char current = (char) currentByte;

        // check for all single character tokens.
        switch (current) {
            case '+':
                return new AdditionToken();
            case '-':
                return new SubtractionToken();
            case '*':
                return new MultiplicationToken();
            case '/':
                return new DivisionToken();
            case '^':
                return new ExponentToken();
            case '(':
                return new LBracketToken();
            case ')':
                return new RBracketToken();
            case '=':
                return new EqualsToken();
            case ' ':
                return next();
        }

        // check for numbers.
        if (Character.isDigit(current)) {
            // attempt to extract full number.
            boolean pointEncountered = false;

            StringBuilder full = new StringBuilder(String.valueOf(current));

            // mark and read ahead.
            inputStream.mark(100);
            current = (char) inputStream.read();

            while (Character.isDigit(current) || (current == '.' && !pointEncountered)) {
                if (current == '.') pointEncountered = true;
                full.append(current);

                // mark the previous character before reading so that program can step back if necessary.
                inputStream.mark(100);
                current = (char) inputStream.read();
            }

            // rewind stream.
            inputStream.reset();

            // create token.
            return new NumberToken(Double.valueOf(full.toString()));
        }

        // check for variables.
        if (Character.isLetter(current)) {

            // attempt to extract full variable name.
            StringBuilder full = new StringBuilder(String.valueOf(current));

            // mark and read ahead.
            inputStream.mark(100);
            current = (char) inputStream.read();

            while (Character.isDigit(current) || Character.isLetter(current)) {
                full.append(current);

                // mark previous point in stream and advance stream.
                inputStream.mark(100);
                current = (char) inputStream.read();
            }
            // rewind stream.
            inputStream.reset();

            // create token.
            return new VariableToken(full.toString());
        }

        // return null if no suitable token is found or end of input is reached.
        return null;
    }

    /**
     * Getter for position.
     * @return Current position in token stream.
     */
    public int getPosition() {
        // if position is -1 return the last index in the list.
        if (position == -1) return tokenStream.size();
        else return position;
    }

    /**
     * Setter for position.
     * @param position Position in token stream.
     */
    public void setPosition(int position) {
        // check if position is valid.
        this.position = position;
    }
}
