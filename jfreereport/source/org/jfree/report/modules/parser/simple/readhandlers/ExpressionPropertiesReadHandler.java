package org.jfree.report.modules.parser.simple.readhandlers;

import java.beans.IntrospectionException;

import org.jfree.report.function.Expression;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.report.util.beans.BeanUtility;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ExpressionPropertiesReadHandler extends AbstractXmlReadHandler
{
  private CharacterEntityParser characterEntityParser;
  private BeanUtility beanUtility;

  public ExpressionPropertiesReadHandler (final Expression expression)
          throws IntrospectionException
  {
    this.beanUtility = new BeanUtility(expression);
    this.characterEntityParser = CharacterEntityParser.createXMLEntityParser();
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final Attributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("property"))
    {
      return new ExpressionPropertyReadHandler(beanUtility, characterEntityParser);
    }
    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return this;
  }
}
