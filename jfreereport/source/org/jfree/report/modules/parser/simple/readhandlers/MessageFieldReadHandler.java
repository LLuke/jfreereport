package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.MessageFieldElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class MessageFieldReadHandler extends StringFieldReadHandler
{
  public MessageFieldReadHandler ()
  {
    super(new MessageFieldElementFactory ());
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
    final MessageFieldElementFactory elementFactory = (MessageFieldElementFactory) getElementFactory();
    elementFactory.setFormatString(atts.getValue("format"));
  }
}
