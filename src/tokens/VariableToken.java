package tokens;

/**
 * Represents a variable in an expression.
 */
public class VariableToken extends Token{

    // the name of the variable.
    private String name;

    public VariableToken(String name) {
        this.name = name;
    }

    /**
     * Getter for name.
     * @return Name of the variable token represents.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     * @param name Name of the variable.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns generic instance.
     * @return {@link VariableToken} object.
     */
    public static VariableToken getInstance() {
        return new VariableToken("");
    }
}
