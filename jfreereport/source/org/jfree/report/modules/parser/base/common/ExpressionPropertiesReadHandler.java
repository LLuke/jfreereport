package org.jfree.report.modules.parser.base.common;

import java.beans.IntrospectionException;

import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.report.util.beans.BeanUtility;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ExpressionPropertiesReadHandler extends AbstractPropertyXmlReadHandler
{
  private CharacterEntityParser characterEntityParser;
  private BeanUtility beanUtility;
  private Expression expression;

  public ExpressionPropertiesReadHandler (final Expression expression)
          throws IntrospectionException
  {
    this.beanUtility = new BeanUtility(expression);
    this.expression = expression;
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
                                               final PropertyAttributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("property"))
    {
      final CommentHintPath commentHintPath =
              new CommentHintPath(expression);
      commentHintPath.addName("properties");
      return new ExpressionPropertyReadHandler
              (beanUtility, characterEntityParser, commentHintPath);
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

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath commentHintPath = new CommentHintPath(expression);
    commentHintPath.addName("properties");
    defaultStoreComments(commentHintPath);
  }
}
