package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Boolean} attributes to and from their
 * {@link String} representation.
 */
public class BooleanValueConverter implements ValueConverter {

    /**
     * Creates a new value converter.
     */
    public BooleanValueConverter() {
        super();
    }

    /**
     * Converts the attribute to a string.
     *
     * @param o  the attribute ({@link Boolean} expected).
     *
     * @return A string representing the {@link Boolean} value.
     */
    public String toAttributeValue(final Object o) {
        if (o instanceof Boolean) {
            return o.toString();
        }
        throw new ClassCastException("Give me a real type.");
    }

    /**
     * Converts a string to a {@link Boolean}.
     *
     * @param s  the string.
     *
     * @return a {@link Boolean}.
     */
    public Object toPropertyValue(final String s) {
        return new Boolean (s);
    }
}
