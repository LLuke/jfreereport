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
 * OutputTarget.java
 * -----------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: OutputTarget.java,v 1.5 2002/09/01 15:49:31 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 18-Apr-2002 : Introduced drawImage and drawMultiLine
 * 10-May-2002 : Documentation
 * 16-May-2002 : Interface of drawShape changed
 * 20-May-2002 : Moved into new package. Extended to support Strokes, cursors and saveable states.
 *               Created beginPage() state callback to property initialize new pages. FillShape
 *               added.
 * 31-Aug-2002 : Added properties to support a generic configuration interface
 */

package com.jrefinery.report.targets;

import com.jrefinery.report.ImageReference;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;

/**
 * An interface that defines the methods that must be supported by a report output target.
 * JFreeReport currently implements two targets:  one for Graphics2D (screen and printer) and
 * one for Acrobat PDF files.
 *
 * @author DG
 */
public interface OutputTarget
{
  /** Literal text for the 'title' property name. */
  public static final String TITLE = "title";

  /** Literal text for the 'author' property name. */
  public static final String AUTHOR = "author";

  /**
   * Returns the value of the specified property.  If the property is not found, the
   * <code>defaultValue</code> is returned.
   *
   * @param property  the property name (or key).
   * @param defaultValue  the default value.
   *
   * @return the property value.
   *
   * @throws NullPointerException if <code>property</code> is null.
   */
  public Object getProperty (String property, Object defaultValue);

  /**
   * Defines a property for this target.
   * <P>
   * Properties provide a mechanism for configuring a target.  For example, you can add title and
   * author information to a PDF report using the 'title' and 'author' properties.
   *
   * @param property  the property name (key).
   * @param value  the property value (use null to remove an existing property).
   */
  public void setProperty (String property, Object value);

  /**
   * Opens the target.
   *
   * @throws OutputTargetException if there is some problem opening the target.
   */
  public void open () throws OutputTargetException;

  /**
   * Closes the target.
   *
   * @throws OutputTargetException if there is some problem closing the target.
   */
  public void close () throws OutputTargetException;

  /**
   * Signals that a page is being started.  Some targets need to know when a page is being started,
   * others can simply ignore this message.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void beginPage () throws OutputTargetException;

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is finished,
   * others can simply ignore this message.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void endPage () throws OutputTargetException;

  /**
   * Saves this state and returns a state encapsulation suitable to be restored by restoreState().
   *
   * @return the state.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public Object saveState () throws OutputTargetException;

  /**
   * Restores a previously saved state.
   *
   * @param o  the state.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void restoreState (Object o) throws OutputTargetException;

  /**
   * Returns the value of the specified property.  If the property is not found, <code>null</code>
   * is returned.
   *
   * @param property  the property name (or key).
   *
   * @return the property value.
   *
   * @throws NullPointerException if <code>property</code> is null.
   */
  public Object getProperty (String property);

  /**
   * Returns the page format for the target.
   *
   * @return the page format.
   */
  public PageFormat getPageFormat ();

  /**
   * Sets the page format for the target.
   *
   * @param format  the page format.
   */
  public void setPageFormat (PageFormat format);

  /**
   * Returns the coordinate of the left edge of the page.
   *
   * @return the left edge of the page.
   */
  public float getPageX ();

  /**
   * Returns the coordinate of the top edge of the page.
   *
   * @return the top edge of the page.
   */
  public float getPageY ();

  /**
   * Returns the page width in points (1/72 inch).
   *
   * @return the page width.
   */
  public float getPageWidth ();

  /**
   * Returns the page height in points (1/72 inch).
   *
   * @return the page height.
   */
  public float getPageHeight ();

  /**
   * Returns the left edge of the printable area of the page.  The units are points.
   *
   * @return the left edge of the printable area of the page.
   */
  public float getUsableX ();

  /**
   * Returns the top edge of the printable area of the page.  The units are points.
   *
   * @return the top edge of the printable area of the page.
   */
  public float getUsableY ();

  /**
   * Returns the width (in points) of the printable area of the page.
   *
   * @return the width of the printable area of the page.
   */
  public float getUsableWidth ();

  /**
   * Returns the height (in points) of the printable area of the page.
   *
   * @return the height of the printable area of the page.
   */
  public float getUsableHeight ();

  /**
   * Returns the current font.
   *
   * @return the current font.
   */
  public Font getFont ();

  /**
   * Sets the font.
   *
   * @param font  the font.
   *
   * @throws OutputTargetException if there is a problem setting the font.
   */
  public void setFont (Font font) throws OutputTargetException;

  /**
   * Returns the current stroke.
   *
   * @return the stroke.
   */
  public Stroke getStroke ();

  /**
   * Defines the current stroke for the target.
   * <P>
   * The stroke is used to draw the outlines of shapes.
   *
   * @param stroke  the stroke.
   *
   * @throws OutputTargetException if there is a problem setting the stroke.
   */
  public void setStroke (Stroke stroke) throws OutputTargetException;

  /**
   * Returns the current paint.
   *
   * @return the paint.
   */
  public Paint getPaint ();

  /**
   * Sets the paint.
   *
   * @param paint The paint.
   *
   * @throws OutputTargetException if there is a problem setting the paint.
   */
  public void setPaint (Paint paint) throws OutputTargetException;

  /**
   * Defines the bounds for the current band. This will also adjust the bandBounds of the
   * assigned cursor.
   *
   * @param bounds  the bounds.
   */
  public void setClippingArea (Rectangle2D bounds);

  /**
   * Returns the defined bound to the current band.
   *
   * @return the clipping area.
   */
  public Rectangle2D getClippingArea ();

  /**
   * Returns the cursor assigned to this target. The cursor is used to translate between
   * the different coordinate spaces.
   *
   * @return the cursor.
   */
  public BandCursor getCursor ();

  /**
   * Draws a string at the current cursor position.
   *
   * @param text  the text.
   * @param alignment  the horizontal alignment.
   */
  public void drawString (String text, int alignment);


  /**
   * Draws a string at the current cursor position. The text is split at the end of the line and
   * continued in the next line.
   *
   * @param text  the text.
   * @param alignment  the horizontal alignment.
   */
  public void drawMultiLineText (String text, int alignment);

  /**
   * Draws a string at the current cursor position. The text is split at the end of the line and
   * continued in the next line.
   *
   * @param text  the text.
   * @param alignment  the horizontal alignment.
   * @param dynamic  ??
   */
  public void drawMultiLineText (String text, int alignment, boolean dynamic);

  /**
   * Draws a shape relative to the current position.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape (Shape shape);

  /**
   * Fills the shape relative to the current position.
   *
   * @param shape  the shape to draw.
   */
  public void fillShape (Shape shape);

  /**
   * Draws a image relative to the specified coordinates.
   *
   * @param image The image to draw (as ImageReference for possible embedding of raw data).
   *
   * @throws OutputTargetException if there is a problem setting the paint.
   */
  public void drawImage (ImageReference image) throws OutputTargetException;

}
