/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------
 * ShapeElement.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ShapeElement.java,v 1.17 2002/09/13 15:38:04 mungady Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Added paint attribute to Element.java (DG);
 * 10-May-2002 : Removed all but the default constructor. Added accessor functions for all
 *               properties.
 * 12-May-2002 : Declared abstract and moved line functionality into LineShapeElement-class
 * 16-May-2002 : using protected member m_paint instead of getter methode
 *               stroke property added (JS)
 * 26-May-2002 : Added shoudDraw and shouldFill properties. These are internal and customize
 *               whether a shape is drawn, filled or both by the default drawing engine
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleKey;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Used to draw shapes (typically lines and boxes) on a report band. This is the abstract
 * base class for all specialized shape elements.
 *
 * !!!! Complete REDESIGN! Use the Filters!
 *
 * @author DG
 */
public class ShapeElement extends Element
{
  /** default stroke size. */
  public static final BasicStroke DEFAULT_STROKE = new BasicStroke (0.5f);

  public static final StyleKey FILL_SHAPE = StyleKey.getStyleKey("fill-shape", Boolean.class);
  public static final StyleKey DRAW_SHAPE = StyleKey.getStyleKey("draw-shape", Boolean.class);

  private static class ShapeElementDefaultStyleSheet extends ElementStyleSheet
  {
    public ShapeElementDefaultStyleSheet()
    {
      super("Shape-default");
      setStyleProperty(ElementStyleSheet.STROKE, DEFAULT_STROKE);
    }
  }

  private static ShapeElementDefaultStyleSheet defaultShapeStyle;

  public static ShapeElementDefaultStyleSheet getDefaultStyle ()
  {
    if (defaultShapeStyle == null)
    {
      defaultShapeStyle = new ShapeElementDefaultStyleSheet();
    }
    return defaultShapeStyle;
  }

  /**
   * Constructs a shape element.
   */
  public ShapeElement ()
  {
    getStyle().addParent(getDefaultStyle());
  }

  /**
   * Returns a string describing the element.  Useful for debugging.
   *
   * @return the string.
   */
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("Shape={ name=");
    b.append (getName ());
    b.append (", shape=");
    b.append (getValue());
    b.append ("}");

    return b.toString ();
  }

  /**
   * Specifies whether the outline of this elements shape should be printed.
   * By default this returns true.
   *
   * @return true if the outline should be drawn, false otherwise
   */
  public boolean isShouldDraw ()
  {
    return getStyle().getBooleanStyleProperty(DRAW_SHAPE);
  }

  /**
   * Specifies whether the contents of this elements shape should be filled with this elements
   * paint. By default this returns true.
   *
   * @return true if the outline should be drawn, false otherwise
   */
  public boolean isShouldFill ()
  {
    return getStyle().getBooleanStyleProperty(FILL_SHAPE);
  }

  /**
   * Sets a flag that controls whether or not the outline of the shape is drawn.
   *
   * @param shouldDraw  the flag.
   */
  public void setShouldDraw (boolean shouldDraw)
  {
    getStyle().setStyleProperty(DRAW_SHAPE, new Boolean(shouldDraw));
  }

  /**
   * Specifies whether the contents of this elements shape should be filled with this elements
   * paint. By default this returns true.
   *
   * @return true if the outline should be drawn, false otherwise
   */
  public boolean isScale()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE);
  }

  /**
   * Sets a flag that controls whether the shape should be scaled to fit the element bounds
   *
   * @param scale the flag.
   */
  public void setScale(boolean scale)
  {
    getStyle().setStyleProperty(ElementStyleSheet.SCALE, new Boolean(scale));
  }

  /**
   * Specifies whether the contents of this elements shape should be filled with this elements
   * paint. By default this returns true.
   *
   * @return true if the outline should be drawn, false otherwise
   */
  public boolean isKeepAspectRatio()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO);
  }

  /**
   * Sets a flag that controls whether the shape should be scaled to fit the element bounds
   *
   * @param kar the flag.
   */
  public void setKeepAspectRatio(boolean kar)
  {
    getStyle().setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, new Boolean(kar));
  }

  /**
   * Sets a flag that controls whether or not the area of the shape is filled.
   *
   * @param shouldFill  the flag.
   */
  public void setShouldFill (boolean shouldFill)
  {
    getStyle().setStyleProperty(FILL_SHAPE, new Boolean(shouldFill));
  }

  public static final String CONTENT_TYPE = "shape/generic";

  public String getContentType()
  {
    return CONTENT_TYPE;
  }

  public Stroke getStroke ()
  {
    return (Stroke) getStyle().getStyleProperty(ElementStyleSheet.STROKE);
  }

  public void setStroke (Stroke stroke)
  {
    getStyle().setStyleProperty(ElementStyleSheet.STROKE, stroke);
  }
}
