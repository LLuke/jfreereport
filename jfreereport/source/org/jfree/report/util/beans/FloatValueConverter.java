package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Float} attributes to and from their
 * {@link String} representation.
 */
public class FloatValueConverter implements ValueConverter {

    /**
     * Creates a new value converter.
     */
    public FloatValueConverter() {
        super();
    }

    /**
     * Converts the attribute to a string.
     *
     * @param o  the attribute ({@link Float} expected).
     *
     * @return A string representing the {@link Float} value.
     */
    public String toAttributeValue(final Object o) {
        if (o instanceof Float) {
            return o.toString();
        }
        throw new ClassCastException("Give me a real type.");
    }

    /**
     * Converts a string to a {@link Float}.
     *
     * @param s  the string.
     *
     * @return a {@link Float}.
     */
    public Object toPropertyValue(final String s) {
      return new Float(s);
    }
}
