package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Double} attributes to and from their
 * {@link String} representation.
 */
public class DoubleValueConverter implements ValueConverter {

    /**
     * Creates a new value converter.
     */
    public DoubleValueConverter() {
        super();
    }

    /**
     * Converts the attribute to a string.
     *
     * @param o  the attribute ({@link Double} expected).
     *
     * @return A string representing the {@link Double} value.
     */
    public String toAttributeValue(final Object o) {
        if (o instanceof Double) {
            return o.toString();
        }
        throw new ClassCastException("Give me a real type.");
    }

    /**
     * Converts a string to a {@link Double}.
     *
     * @param s  the string.
     *
     * @return a {@link Double}.
     */
    public Object toPropertyValue(final String s) {
      return new Double(s);
    }
}
