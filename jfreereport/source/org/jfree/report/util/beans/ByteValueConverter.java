package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Byte} attributes to and from their
 * {@link String} representation.
 */
public class ByteValueConverter implements ValueConverter {

    /**
     * Creates a new value converter.
     */
    public ByteValueConverter() {
        super();
    }

    /**
     * Converts the attribute to a string.
     *
     * @param o  the attribute ({@link Byte} expected).
     *
     * @return A string representing the {@link Byte} value.
     */
    public String toAttributeValue(final Object o) {
        if (o instanceof Byte) {
            return o.toString();
        }
        throw new ClassCastException("Give me a real type.");
    }

    /**
     * Converts a string to a {@link Byte}.
     *
     * @param s  the string.
     *
     * @return a {@link Byte}.
     */
    public Object toPropertyValue(final String s) {
        return new Byte (s);
    }
}
