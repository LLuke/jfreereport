package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.NumberFieldElementFactory;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NumberFieldReadHandler extends StringFieldReadHandler
{
  public NumberFieldReadHandler ()
  {
    super(new NumberFieldElementFactory ());
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes atts)
          throws SAXException, XmlReaderException
  {
    super.startParsing(atts);
    final NumberFieldElementFactory elementFactory =
            (NumberFieldElementFactory) getTextElementFactory();
    elementFactory.setFormatString(atts.getValue("format"));
  }
}
