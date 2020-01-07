package parser.productions;

import common.GrammarComponent;
import lexer.ExpressionLexer;
import parser.syntaxtree.TreeNode;
import tokens.operators.AdditionToken;
import tokens.operators.SubtractionToken;

import java.io.IOException;

/**
 * Represents the sum production.
 * Sum -> Product ((+ product)|(- product))*
 */
public class SumProduction extends Production {

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
        int previousPos = lexer.getPosition();

        // attempt to recognise a single product.
        ProductProduction firstProduct = new ProductProduction();
        TreeNode<GrammarComponent> firstNode = new TreeNode<>(firstProduct);
        if (firstProduct.recognise(lexer, firstNode)) {
            node.addChild(firstNode);
        }
        // otherwise rewind and return false.
        else {
            lexer.setPosition(previousPos);
            return false;
        }

        // now attempt to recognise repeated patterns.
        while (true) {
            // mark previous position.
            previousPos = lexer.getPosition();

            // attempt to recognise + or - followed by factor.
            AdditionToken additionToken = new AdditionToken();
            SubtractionToken subtractionToken = new SubtractionToken();
            ProductProduction repeatedProductProduction = new ProductProduction();
            TreeNode<GrammarComponent> repeatedProductNode = new TreeNode<>(repeatedProductProduction);

            // first check for addition followed by a product.
            if (recogniseToken(lexer, additionToken) && repeatedProductProduction.recognise(lexer, repeatedProductNode)) {
                node.addChild(new TreeNode<>(additionToken));
                node.addChild(repeatedProductNode);
                continue;
            }
            else {
                lexer.setPosition(previousPos);
            }

            // next check for subtraction followed by product.
            if (recogniseToken(lexer, subtractionToken) && repeatedProductProduction.recognise(lexer, repeatedProductNode)) {
                node.addChild(new TreeNode<>(subtractionToken));
                node.addChild(repeatedProductNode);
                continue;
            }
            else {
                lexer.setPosition(previousPos);
            }

            // if neither productions are recognise break.
            break;
        }

        return true;

    }

}
