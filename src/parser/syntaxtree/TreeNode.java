package parser.syntaxtree;

import tokens.NumberToken;
import tokens.VariableToken;

import java.util.ArrayList;

/**
 * Class represents a node in a tree.
 * @param <T> The type of object the tree is storing.
 */
public class TreeNode<T> {

    // the children of the node.
    private ArrayList<TreeNode<T>> children = new ArrayList<>();

    // the contents of the node.
    private T data;

    public TreeNode(T data) {
        this.data = data;
    }

    /**
     * Add child to node.
     * @param node {@link TreeNode} object being added.
     */
    public void addChild(TreeNode<T> node) {
        children.add(node);
    }

    /**
     * Getter for data.
     * @return {@link T} object being stored by node.
     */
    public T getData() {
        return data;
    }

    /**
     * Overrides value given when attempted to be cast to String.
     * @return Visual representation of String.
     */
    @Override
    public String toString() {
        return recursivePrint(0);
    }

    private String recursivePrint(int level) {
        StringBuilder builder = new StringBuilder();
        // append indent.
        for (int i = 0; i < level; i++) builder.append("\t");
        // append class data belongs to.
        builder.append(data.getClass().getSimpleName());
        // if data is variable or number append data.
        if (data instanceof NumberToken) {
            builder.append("(");
            builder.append(((NumberToken) data).getValue());
            builder.append(")");
        }
        else if (data instanceof VariableToken) {
            builder.append("(");
            builder.append(((VariableToken) data).getName());
            builder.append(")");
        }

        builder.append("\n");

        // append children's data.
        for (TreeNode child : children) builder.append(child.recursivePrint(level + 1));

        return builder.toString();

    }

    /**
     * Getter for children.
     * @return Returns list of children nodes.
     */
    public ArrayList<TreeNode<T>> getChildren() {
        return children;
    }
}
