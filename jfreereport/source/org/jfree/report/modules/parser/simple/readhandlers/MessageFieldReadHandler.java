package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.MessageFieldElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.PropertyStringReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class MessageFieldReadHandler extends AbstractTextElementReadHandler
{
  private PropertyStringReadHandler stringReadHandler;
  private MessageFieldElementFactory elementFactory;

  public MessageFieldReadHandler ()
  {
    elementFactory = new MessageFieldElementFactory ();
    stringReadHandler = new PropertyStringReadHandler(null);
  }

  protected TextElementFactory getTextElementFactory ()
  {
    return elementFactory;
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException, XmlReaderException
  {
    super.startParsing(atts);
    getRootHandler().delegate(stringReadHandler, getTagName(), atts);
    elementFactory.setNullString(atts.getValue("nullstring"));
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
    final String text = stringReadHandler.getResult();
    elementFactory.setFormatString(text);
    super.doneParsing();
  }
}
