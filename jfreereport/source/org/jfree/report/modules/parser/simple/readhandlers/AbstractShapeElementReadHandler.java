package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.Stroke;

import org.jfree.report.elementfactory.ShapeElementFactory;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AbstractShapeElementReadHandler extends AbstractElementReadHandler
{
  private static final String SCALE_ATT = "scale";
  private static final String KEEP_ASPECT_RATIO_ATT = "keepAspectRatio";
  private static final String FILL_ATT = "fill";
  private static final String DRAW_ATT = "draw";

  public AbstractShapeElementReadHandler ()
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
    super.startParsing(atts);
    handleScale (atts);
    handleKeepAspectRatio (atts);
    handleFill(atts);
    handleDraw(atts);
    handleStroke(atts);
    handleColor(atts);
  }

  private void handleColor (final Attributes atts)
  {
    final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
    elementFactory.setColor(ParserUtil.parseColor(atts.getValue("color"), null));
  }

  private void handleStroke (final Attributes atts)
  {
    final Stroke stroke = ParserUtil.parseStroke(atts.getValue("weight"));
    if (stroke != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setStroke(stroke);
    }
  }

  protected void handleScale (final Attributes atts)
  {
    final String booleanValue = atts.getValue(SCALE_ATT);
    if (booleanValue != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setScale(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

  protected void handleKeepAspectRatio (final Attributes atts)
  {
    final String booleanValue = atts.getValue(KEEP_ASPECT_RATIO_ATT);
    if (booleanValue != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setKeepAspectRatio(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

  protected void handleFill (final Attributes atts)
  {
    final String booleanValue = atts.getValue(FILL_ATT);
    if (booleanValue != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setShouldFill(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

  protected void handleDraw (final Attributes atts)
  {
    final String booleanValue = atts.getValue(DRAW_ATT);
    if (booleanValue != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setShouldDraw(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

}
