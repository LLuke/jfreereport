package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Integer} attributes to and from their
 * {@link String} representation.
 */
public class ClassValueConverter implements ValueConverter
{
  public ClassValueConverter ()
  {
  }

  public String toAttributeValue (Object o)
  {
    if (o instanceof Class)
    {
      Class c = (Class) o;
      return c.getName();
    }
    throw new ClassCastException("Give me a real type.");
  }

  public Object toPropertyValue (String s)
  {
    try
    {
      return Class.forName(s);
    }
    catch (ClassNotFoundException e)
    {
      throw new IllegalArgumentException("No such class.");
    }
  }
}
