package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.geom.Rectangle2D;

import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RectangleReadHandler extends AbstractShapeElementReadHandler
{
  private StaticShapeElementFactory elementFactory;

  public RectangleReadHandler ()
  {
    elementFactory = new StaticShapeElementFactory();
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
    if (elementFactory.getShouldDraw() == null)
    {
      elementFactory.setShouldDraw(Boolean.TRUE);
    }
    if (elementFactory.getShouldFill() == null)
    {
      elementFactory.setShouldFill(Boolean.TRUE);
    }
    elementFactory.setScale(Boolean.TRUE);
    elementFactory.setDynamicHeight(Boolean.FALSE);
    elementFactory.setKeepAspectRatio(Boolean.FALSE);
    elementFactory.setShape(new Rectangle2D.Float(0, 0, 100, 100));
  }

  protected ElementFactory getElementFactory ()
  {
    return elementFactory;
  }
}
