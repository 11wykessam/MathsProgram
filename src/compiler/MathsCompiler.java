package compiler;

import common.GrammarComponent;
import exceptions.VariableUndefinedException;
import parser.productions.*;
import parser.syntaxtree.TreeNode;
import tokens.NumberToken;
import tokens.VariableToken;
import tokens.operators.*;

import java.util.HashMap;

/**
 * Simple compiler for maths.
 */
public class MathsCompiler {

    // simple hash map that maps variable names to their integer values.
    private HashMap<String, Double> variables = new HashMap<>();

    /**
     * Executes a statement.
     * @param root The root node of the syntax tree.
     */
    public void execute(TreeNode<GrammarComponent> root) throws VariableUndefinedException {
        for (TreeNode<GrammarComponent> child : root.getChildren()) {
            if (child.getData() instanceof AssignmentProduction) executeAssignment(child);
            else if (child.getData() instanceof CalculationProduction) System.out.println(evaluate(child));
        }
    }

    /**
     * Executes an assignment statement.
     * @param assignment {@link TreeNode} object containing assignment statement.
     * @return True if assignment is successful.
     * @throws VariableUndefinedException
     */
    private boolean executeAssignment(TreeNode<GrammarComponent> assignment) throws VariableUndefinedException{
        // should be able to extract the variable form first child.
        String variableName = ((VariableToken)assignment.getChildren().get(0).getData()).getName();
        try {
            double value = evaluate(assignment.getChildren().get(2));
            variables.put(variableName, value);
            return true;
        }
        catch (VariableUndefinedException e) {
            System.out.println("Variable Undefined");
            return false;
        }
    }

    /**
     * Evalue a given production.
     * @param expression The expression being evaluated.
     * @return Double given by value of evaluated expression.
     * @throws VariableUndefinedException Thrown if a variable is used which has not been defined.
     */
    private double evaluate(TreeNode<GrammarComponent> expression) throws VariableUndefinedException {

        // if expression is a calculation then simply evaluate first child.
        if (expression.getData() instanceof CalculationProduction) return evaluate(expression.getChildren().get(0));

        // if expression is a term either evaluate group or data.
        else if (expression.getData() instanceof TermProduction) {
            TreeNode<GrammarComponent> child = expression.getChildren().get(0);
            // if term is a group evaluate the group.
            if (child.getData() instanceof GroupProduction) return evaluate(child);
            // otherwise return either value of number or variable.
            if (child.getData() instanceof NumberToken) return ((NumberToken) child.getData()).getValue();
            else {
                String variableName = ((VariableToken)child.getData()).getName();
                if (variables.containsKey(variableName)) return variables.get(variableName);
                else throw new VariableUndefinedException();
            }
        }

        // if expression is a group evaulate whatever is inside brackets.
        else if (expression.getData() instanceof GroupProduction) {
            return evaluate(expression.getChildren().get(1));
        }

        // if the expression is a sum then add or subtract all the components.
        else if (expression.getData() instanceof SumProduction) {

            // extract value of first item.
            double accumulator = evaluate(expression.getChildren().get(0));

            // iterate through remaining children in operator-operand pairs.
            for (int i = 1; i < expression.getChildren().size() - 1; i += 2) {
                SumOperatorToken operator = (SumOperatorToken)expression.getChildren().get(i).getData();
                TreeNode<GrammarComponent> operand = expression.getChildren().get(i + 1);
                if (operator instanceof AdditionToken) accumulator += evaluate(operand);
                else if (operator instanceof SubtractionToken) accumulator -= evaluate(operand);
            }

            return accumulator;

        }

        // if the expression is a product then multiply or divide all components.
        else if (expression.getData() instanceof ProductProduction) {

            // extract value of first item.
            double accumulator = evaluate(expression.getChildren().get(0));

            // iterate through remaining children in operator-operand pairs.
            for (int i = 1; i < expression.getChildren().size() - 1; i += 2) {
                ProductOperatorToken operator = (ProductOperatorToken) expression.getChildren().get(i).getData();
                TreeNode<GrammarComponent> operand = expression.getChildren().get(i + 1);
                if (operator instanceof MultiplicationToken) accumulator *= evaluate(operand);
                else if (operator instanceof DivisionToken) accumulator /= evaluate(operand);
            }

            return accumulator;

        }

        // if the expression is a power production then perform calculation.
        else if (expression.getData() instanceof PowerProduction) {
            return Math.pow(evaluate(expression.getChildren().get(0)), evaluate(expression.getChildren().get(2)));
        }

        else if (expression.getData() instanceof FactorProduction) return evaluate(expression.getChildren().get(0));

        return -1;

    }

}
