package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.coretypes.StringReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LabelReadHandler extends AbstractTextElementReadHandler
{
  private LabelElementFactory labelElementFactory;
  private StringReadHandler stringReadHandler;

  public LabelReadHandler ()
  {
    labelElementFactory = new LabelElementFactory();
    stringReadHandler = new StringReadHandler();
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
    getRootHandler().delegate(stringReadHandler, getTagName(), atts);
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
    labelElementFactory.setText(text);
  }

  protected TextElementFactory getTextElementFactory ()
  {
    return labelElementFactory;
  }
}
