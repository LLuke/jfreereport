package org.jfree.report.util.beans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

/**
 * A class that handles the conversion of {@link Integer} attributes to and from their
 * {@link String} representation.
 */
public class GenericValueConverter implements ValueConverter
{
  private PropertyDescriptor propertyDescriptor;
  private PropertyEditor propertyEditor;

  /**
   * Creates a new value converter.
   */
  public GenericValueConverter (final PropertyDescriptor pd)
          throws IntrospectionException
  {
    if (pd == null)
    {
      throw new NullPointerException("PropertyDescriptor must not be null.");
    }
    if (pd.getPropertyEditorClass() == null)
    {
      throw new IntrospectionException("Property has no editor.");
    }
    this.propertyDescriptor = pd;
    this.propertyEditor = createPropertyEditor(pd);
  }

  private PropertyEditor createPropertyEditor (final PropertyDescriptor pi)
          throws IntrospectionException
  {
    final Class c = pi.getPropertyEditorClass();
    try
    {
      return (PropertyEditor) c.newInstance();
    }
    catch (Exception e)
    {
      throw new IntrospectionException("Unable to create PropertyEditor.");
    }
  }


  /**
   * Converts the attribute to a string.
   *
   * @param o the attribute ({@link Integer} expected).
   * @return A string representing the {@link Integer} value.
   */
  public String toAttributeValue (final Object o)
  {
    if (propertyDescriptor.getPropertyType().isInstance(o) == false)
    {
      throw new ClassCastException("Give me a real type.");
    }

    propertyEditor.setValue(o);
    return propertyEditor.getAsText();
  }

  /**
   * Converts a string to a {@link Integer}.
   *
   * @param s the string.
   * @return a {@link Integer}.
   */
  public Object toPropertyValue (final String s)
  {
    propertyEditor.setAsText(s);
    return propertyEditor.getValue();
  }
}
