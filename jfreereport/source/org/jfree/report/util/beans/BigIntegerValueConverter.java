package org.jfree.report.util.beans;

import java.math.BigInteger;

/**
 * A class that handles the conversion of {@link java.math.BigInteger} attributes to and
 * from their {@link String} representation.
 */
public class BigIntegerValueConverter implements ValueConverter
{

  /**
   * Creates a new value converter.
   */
  public BigIntegerValueConverter ()
  {
    super();
  }

  /**
   * Converts the attribute to a string.
   *
   * @param o the attribute ({@link java.math.BigInteger} expected).
   * @return A string representing the {@link java.math.BigInteger} value.
   */
  public String toAttributeValue (final Object o)
  {
    if (o instanceof Integer)
    {
      return o.toString();
    }
    throw new ClassCastException("Give me a real type.");
  }

  /**
   * Converts a string to a {@link java.math.BigInteger}.
   *
   * @param s the string.
   * @return a {@link java.math.BigInteger}.
   */
  public Object toPropertyValue (final String s)
  {
    return new BigInteger(s);
  }
}
