package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.elementfactory.StaticImageURLElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ImageRefReadHandler extends AbstractImageElementReadHandler
{
  private StaticImageURLElementFactory elementFactory;
  private static final String SRC_ATT = "src";

  public ImageRefReadHandler ()
  {
    this.elementFactory = new StaticImageURLElementFactory();
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
    handleSource(atts);
  }

  private void handleSource (final PropertyAttributes atts)
  {
    elementFactory.setBaseURL(getRootHandler().getContentBase());
    elementFactory.setContent(atts.getValue(SRC_ATT));
  }


  protected ElementFactory getElementFactory ()
  {
    return elementFactory;
  }
}
