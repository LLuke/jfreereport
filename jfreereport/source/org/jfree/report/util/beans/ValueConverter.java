package org.jfree.report.util.beans;

/**
 * A value converter is an object that can transform an object into a
 * string or vice versa.
 */
public interface ValueConverter
{
  /**
   * Converts an object to an attribute value.
   *
   * @param o  the object.
   *
   * @return the attribute value.
   */
  public String toAttributeValue (Object o);

  /**
   * Converts a string to a property value.
   *
   * @param s  the string.
   *
   * @return a property value.
   */
  public Object toPropertyValue (String s);
}
