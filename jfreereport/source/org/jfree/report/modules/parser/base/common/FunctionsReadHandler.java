package org.jfree.report.modules.parser.base.common;

import java.util.ArrayList;

import org.jfree.report.JFreeReport;
import org.jfree.report.function.Expression;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class FunctionsReadHandler extends AbstractPropertyXmlReadHandler
{
  private JFreeReport report;
  private ArrayList expressionHandlers;
  private ArrayList propertyRefs;

  public FunctionsReadHandler (final JFreeReport report)
  {
    this.report = report;
    this.expressionHandlers = new ArrayList();
    this.propertyRefs = new ArrayList();
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
    if (tagName.equals("expression") || tagName.equals("function"))
    {
      final ExpressionReadHandler readHandler = new ExpressionReadHandler();
      expressionHandlers.add(readHandler);
      return readHandler;

    }
    else if (tagName.equals("property-ref"))
    {
      final CommentHintPath commentHintPath = new CommentHintPath("report-definition");
      commentHintPath.addName("functions");
      commentHintPath.addName("property-ref");

      final PropertyReferenceReadHandler readHandler =
              new PropertyReferenceReadHandler(commentHintPath);
      propertyRefs.add(readHandler);
      return readHandler;

    }
    return super.getHandlerForChild(tagName, atts);
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
    for (int i = 0; i < expressionHandlers.size(); i++)
    {
      final ExpressionReadHandler readHandler =
              (ExpressionReadHandler) expressionHandlers.get(i);
      report.addExpression((Expression) readHandler.getObject());
    }

    for (int i = 0; i < propertyRefs.size(); i++)
    {
      final PropertyReferenceReadHandler readHandler =
              (PropertyReferenceReadHandler) propertyRefs.get(i);
      final Object object = readHandler.getObject();
      if (object != null)
      {
        report.setProperty(readHandler.getPropertyName(), object);
      }
      report.setPropertyMarked(readHandler.getPropertyName(), true);
    }
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
    return null;
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath commentHintPath = new CommentHintPath("functions");
    defaultStoreComments(commentHintPath);
  }
}
