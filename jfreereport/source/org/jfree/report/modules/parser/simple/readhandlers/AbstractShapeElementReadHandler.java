/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * AbstractShapeElementReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.Stroke;

import org.jfree.report.elementfactory.ShapeElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReaderException;
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
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException, XmlReaderException
  {
    super.startParsing(atts);
    handleScale(atts);
    handleKeepAspectRatio(atts);
    handleFill(atts);
    handleDraw(atts);
    handleStroke(atts);
    handleColor(atts);
  }

  private void handleColor (final PropertyAttributes atts)
  {
    final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
    elementFactory.setColor(ParserUtil.parseColor(atts.getValue("color"), null));
  }

  private void handleStroke (final PropertyAttributes atts)
  {
    final Stroke stroke = ParserUtil.parseStroke(atts.getValue("weight"));
    if (stroke != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setStroke(stroke);
    }
  }

  protected void handleScale (final PropertyAttributes atts)
  {
    final String booleanValue = atts.getValue(SCALE_ATT);
    if (booleanValue != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setScale(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

  protected void handleKeepAspectRatio (final PropertyAttributes atts)
  {
    final String booleanValue = atts.getValue(KEEP_ASPECT_RATIO_ATT);
    if (booleanValue != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setKeepAspectRatio(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

  protected void handleFill (final PropertyAttributes atts)
  {
    final String booleanValue = atts.getValue(FILL_ATT);
    if (booleanValue != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setShouldFill(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

  protected void handleDraw (final PropertyAttributes atts)
  {
    final String booleanValue = atts.getValue(DRAW_ATT);
    if (booleanValue != null)
    {
      final ShapeElementFactory elementFactory = (ShapeElementFactory) getElementFactory();
      elementFactory.setShouldDraw(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

}
