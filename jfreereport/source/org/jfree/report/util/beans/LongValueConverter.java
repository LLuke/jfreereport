package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Integer} attributes to and from their
 * {@link String} representation.
 */
public class LongValueConverter implements ValueConverter
{

  /**
   * Creates a new value converter.
   */
  public LongValueConverter ()
  {
    super();
  }

  /**
   * Converts the attribute to a string.
   *
   * @param o the attribute ({@link Integer} expected).
   * @return A string representing the {@link Integer} value.
   */
  public String toAttributeValue (final Object o)
  {
    if (o instanceof Long)
    {
      return o.toString();
    }
    throw new ClassCastException("Give me a real type.");
  }

  /**
   * Converts a string to a {@link Integer}.
   *
   * @param s the string.
   * @return a {@link Integer}.
   */
  public Object toPropertyValue (final String s)
  {
    return new Long(s);
  }
}
