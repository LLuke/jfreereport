package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Character} attributes to and from their
 * {@link String} representation.
 */
public class CharacterValueConverter implements ValueConverter {

    /**
     * Creates a new value converter.
     */
    public CharacterValueConverter() {
        super();
    }

    /**
     * Converts the attribute to a string.
     *
     * @param o  the attribute ({@link Character} expected).
     *
     * @return A string representing the {@link Character} value.
     */
    public String toAttributeValue(final Object o) {
        if (o instanceof Character) {
            return o.toString();
        }
        throw new ClassCastException("Give me a real type.");
    }

    /**
     * Converts a string to a {@link Character}.
     *
     * @param s  the string.
     *
     * @return a {@link Character}.
     */
    public Object toPropertyValue(final String s) {
      if (s.length() == 0) {
          throw new RuntimeException("Ugly, no char set!");
      }
      return new Character(s.charAt(0));
    }
}
