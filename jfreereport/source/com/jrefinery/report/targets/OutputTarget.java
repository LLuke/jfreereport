/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * $Id: OutputTarget.java,v 1.2 2002/05/28 19:38:10 taqua Exp $
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
 */
public interface OutputTarget
{

  /**
   * Opens the target.
   *
   * @param title The report title.
   * @param author The report author.
   */
  public void open (String title, String author) throws OutputTargetException;

  /**
   * Closes the target.
   */
  public void close () throws OutputTargetException;

  /**
   * Returns the page format for the target.
   *
   * @return The page format.
   */
  public PageFormat getPageFormat ();

  /**
   * Sets the page format for the target.
   *
   * @param format The page format.
   */
  public void setPageFormat (PageFormat format);

  /**
   * Returns the coordinate of the left edge of the page.
   *
   * @return The left edge of the page.
   */
  public float getPageX ();

  /**
   * Returns the coordinate of the top edge of the page.
   *
   * @return The top edge of the page.
   */
  public float getPageY ();

  /**
   * Returns the page width in points (1/72 inch).
   *
   * @return The page width.
   */
  public float getPageWidth ();

  /**
   * Returns the page height in points (1/72 inch).
   *
   * @return The page height.
   */
  public float getPageHeight ();

  /**
   * Returns the left edge of the printable area of the page.  The units are points.
   *
   * @return The left edge of the printable area of the page.
   */
  public float getUsableX ();

  /**
   * Returns the top edge of the printable area of the page.  The units are points.
   *
   * @return The top edge of the printable area of the page.
   */
  public float getUsableY ();

  /**
   * Returns the width (in points) of the printable area of the page.
   *
   * @return The width of the printable area of the page.
   */
  public float getUsableWidth ();

  /**
   * Returns the height (in points) of the printable area of the page.
   *
   * @return The height of the printable area of the page.
   */
  public float getUsableHeight ();

  /**
   * Sets the font.
   *
   * @param font The font.
   */
  public void setFont (Font font) throws OutputTargetException;

  /**
   * returns the currently defined font for this Target.
   */
  public Font getFont ();

  /**
   * Defines the current stroke for the target. The stroke is used to draw the outlines
   * of shapes.
   */
  public void setStroke (Stroke stroke) throws OutputTargetException;

  /**
   * Returns the current stroke assigned with this target.
   */
  public Stroke getStroke ();

  /**
   * Sets the paint.
   *
   * @param paint The paint.
   */
  public void setPaint (Paint paint) throws OutputTargetException;

  /**
   * Returns the paint currently set for this target.
   */
  public Paint getPaint ();

  /**
   * defines the bounds for the current band. This will also adjust the bandBounds of the
   * assigned cursor.
   */
  public void setClippingArea (Rectangle2D bounds);

  /**
   * Returns the defined bound to the current band.
   */
  public Rectangle2D getClippingArea ();

  /**
   * Returns the cursor assigned with is outputtarget. The cursor is used to translate between
   * the different coordinate spaces.
   */
  public BandCursor getCursor ();

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text).
   *
   * @param text The text.
   * @param alignment The horizontal alignment.
   */
  public void drawString (String text, int alignment);


  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text). The text is split at the end of the line and continued in the next line.
   *
   * @param text The text.
   * @param alignment The horizontal alignment.
   */
  public void drawMultiLineText (String mytext, int align);

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text). The text is split at the end of the line and continued in the next line.
   *
   * @param text The text.
   * @param alignment The horizontal alignment.
   */
  public void drawMultiLineText (String mytext, int align, boolean dynamic);

  /**
   * Draws a shape relative to the specified coordinates.
   *
   * @param shape The shape to draw.
   */
  public void drawShape (Shape shape);

  /**
   * Fills the shape relative to the current position
   */
  public void fillShape (Shape shape);

  /**
   * Draws a image relative to the specified coordinates.
   *
   * @param image The image to draw as imagereference for possible embedding of rawdata.
   */
  public void drawImage (ImageReference image) throws OutputTargetException;

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is finished,
   * others can simply ignore this message.
   */
  public void endPage () throws OutputTargetException;

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is being started,
   * others can simply ignore this message.
   */
  public void beginPage () throws OutputTargetException;

  /**
   * Saves this state and returns a state encapsulation suitable to be restored by restoreState().
   */
  public Object saveState () throws OutputTargetException;

  /**
   * Restores a previously saved state.
   */
  public void restoreState (Object o) throws OutputTargetException;
}
