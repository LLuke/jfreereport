package org.jfree.report.modules.parser.base.common;

import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.PropertyStringReadHandler;
import org.jfree.report.util.beans.ConverterRegistry;
import org.jfree.report.util.beans.StringValueConverter;
import org.jfree.report.util.beans.ValueConverter;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class PropertyReferenceReadHandler extends PropertyStringReadHandler
{
  public static final String CLASS_ATT = "class";
  public static final String NAME_ATT = "name";

  private String propertyName;
  private Object value;
  private ValueConverter valueType;

  public PropertyReferenceReadHandler (final CommentHintPath commentHintPath)
  {
    super(commentHintPath);
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException, XmlReaderException
  {
    super.startParsing(attrs);
    propertyName = attrs.getValue(NAME_ATT);
    if (propertyName == null)
    {
      throw new ElementDefinitionException("Required attribute 'name' is null.");
    }

    final String className = attrs.getValue(CLASS_ATT);
    if (className == null)
    {
      valueType = new StringValueConverter();
    }
    else
    {
      try
      {
        final Class c = ObjectUtilities.getClassLoader(getClass()).loadClass(className);
        valueType = ConverterRegistry.getInstance().getValueConverter(c);
      }
      catch (Exception e)
      {
        throw new ElementDefinitionException("Attribute 'class' is invalid.");
      }
    }
    getHintPath().addName(propertyName);
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    super.doneParsing();
    value = valueType.toPropertyValue(getResult());
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return value;
  }

  public String getPropertyName ()
  {
    return propertyName;
  }
}
