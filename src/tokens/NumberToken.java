package tokens;

/**
 * Represents a number in an expression.
 */
public class NumberToken extends Token {

    // the value represented by the token.
    private double value;

    public NumberToken(double value) {
        this.value = value;
    }

    /**
     * Getter for value.
     * @return Value of number this token represents.
     */
    public double getValue() {
        return value;
    }

    /**
     * Setter for value
     * @param value Value for the number.
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Returns generic NumberToken object.
     * @return {@link NumberToken} object.
     */
    public static NumberToken getInstance() {
        return new NumberToken(0);
    }
}
