package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import org.jfree.report.Element;
import org.jfree.report.util.Log;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LineReadHandler extends AbstractXmlReadHandler
{
  private Element element;
  private static final String NAME_ATT = "name";
  private static final String COLOR_ATT = "color";

  public LineReadHandler ()
  {
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
    final String name = atts.getValue(NAME_ATT);
    final Color c = ParserUtil.parseColor(atts.getValue(COLOR_ATT));
    final float x1 = ParserUtil.parseRelativeFloat(atts.getValue("x1"), "Element x1 not specified");
    final float y1 = ParserUtil.parseRelativeFloat(atts.getValue("y1"), "Element y1 not specified");
    final float x2 = ParserUtil.parseRelativeFloat(atts.getValue("x2"), "Element x2 not specified");
    final float y2 = ParserUtil.parseRelativeFloat(atts.getValue("y2"), "Element y2 not specified");
    final Stroke stroke = ParserUtil.parseStroke(atts.getValue("weight"));

    if (x1 == x2 && y1 == y2)
    {
      Log.warn ("creating a horizontal line with 'x1 == x2 && y1 == y2' is deprecated. " +
              "Use relative coordinates instead."); 
      element = StaticShapeElementFactory.createHorizontalLine
          (name, c, stroke, y2);
    }
    else
    {
      final Line2D line = new Line2D.Float(x1, y1, x2, y2);
      element = StaticShapeElementFactory.createShapeElement
                (name, c, stroke, line, true, false);
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
    return element;
  }
}
