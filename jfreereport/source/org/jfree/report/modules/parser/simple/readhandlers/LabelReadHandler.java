package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.PropertyStringReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class LabelReadHandler extends AbstractTextElementReadHandler
{
  private LabelElementFactory labelElementFactory;
  private PropertyStringReadHandler stringReadHandler;

  public LabelReadHandler ()
  {
    labelElementFactory = new LabelElementFactory();
    stringReadHandler = new PropertyStringReadHandler(null);
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
    super.doneParsing();
  }

  protected TextElementFactory getTextElementFactory ()
  {
    return labelElementFactory;
  }
}
