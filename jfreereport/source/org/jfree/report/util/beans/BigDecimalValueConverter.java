package org.jfree.report.util.beans;

import java.math.BigDecimal;

/**
 * A class that handles the conversion of {@link BigDecimal} attributes to and from their
 * {@link String} representation.
 */
public class BigDecimalValueConverter implements ValueConverter {

    /**
     * Creates a new value converter.
     */
    public BigDecimalValueConverter() {
        super();
    }

    /**
     * Converts the attribute to a string.
     *
     * @param o  the attribute ({@link BigDecimal} expected).
     *
     * @return A string representing the {@link BigDecimal} value.
     */
    public String toAttributeValue(final Object o) {
        if (o instanceof Integer) {
            return o.toString();
        }
        throw new ClassCastException("Give me a real type.");
    }

    /**
     * Converts a string to a {@link BigDecimal}.
     *
     * @param s  the string.
     *
     * @return a {@link BigDecimal}.
     */
    public Object toPropertyValue(final String s) {
      return new BigDecimal(s);
    }
}
