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
 * LineReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LineReadHandler.java,v 1.6 2005/03/03 23:00:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.content.ShapeTransform;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.util.Log;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class LineReadHandler extends AbstractPropertyXmlReadHandler
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
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException, XmlReaderException
  {
    final String name = atts.getValue(NAME_ATT);
    final Color c = ParserUtil.parseColor(atts.getValue(COLOR_ATT));
    final float x1 = ParserUtil.parseRelativeFloat(atts.getValue("x1"), "Element x1 not specified");
    final float y1 = ParserUtil.parseRelativeFloat(atts.getValue("y1"), "Element y1 not specified");
    final float x2 = ParserUtil.parseRelativeFloat(atts.getValue("x2"), "Element x2 not specified");
    final float y2 = ParserUtil.parseRelativeFloat(atts.getValue("y2"), "Element y2 not specified");
    final Stroke stroke = ParserUtil.parseStroke(atts.getValue("weight"));

    final String widthValue = atts.getValue("width");
    final float width;
    if (widthValue != null)
    {
      width = ParserUtil.parseRelativeFloat(widthValue, "Width is invalid");
    }
    else
    {
      width = x2 - x1;
    }

    final String heightValue = atts.getValue("height");
    final float height;
    if (heightValue != null)
    {
      height = ParserUtil.parseRelativeFloat(heightValue, "Height is invalid");
    }
    else
    {
      height = y2 - y1;
    }

    if (x1 == x2 && y1 == y2)
    {
      Log.warn("creating a horizontal line with 'x1 == x2 && y1 == y2' is deprecated. " +
              "Use relative coordinates instead.");
      element = (StaticShapeElementFactory.createHorizontalLine
              (name, c, stroke, y2));
    }
    else
    {
      // create the bounds as specified by the user
      final Rectangle2D bounds =
              new Rectangle2D.Float(x1, y1, width, height);
      // first version of the line (not originating @(0,0))
      final Line2D line = new Line2D.Float(x1, y1, x2, y2);
      // the bounds of that line
      final Rectangle2D shapeBounds = line.getBounds2D();
      // translate so that the shape starts at position (0,0)
      final Shape transformedShape =
              ShapeTransform.translateShape(line, -shapeBounds.getX(), -shapeBounds.getY());
      // and use that shape with the user's bounds to create the element.
      element = StaticShapeElementFactory.createShapeElement
              (name, bounds, c, stroke, transformedShape, true, false, true, false);
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

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath commentHintPath = new CommentHintPath(element);
    defaultStoreComments(commentHintPath);
  }
}
