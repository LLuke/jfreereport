package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.elementfactory.StaticDrawableURLElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class DrawableRefReadHandler extends AbstractElementReadHandler
{
  private StaticDrawableURLElementFactory elementFactory;

  public DrawableRefReadHandler ()
  {
    this.elementFactory = new StaticDrawableURLElementFactory();
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

    final String content = atts.getValue("src");
    if (content == null)
    {
      throw new ElementDefinitionException ("Required attribute 'src' is missing.");
    }
    elementFactory.setContent(content);
    elementFactory.setBaseURL(getRootHandler().getContentBase());
  }

  protected ElementFactory getElementFactory ()
  {
    return elementFactory;
  }
}
