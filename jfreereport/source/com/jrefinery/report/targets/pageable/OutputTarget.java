/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: OutputTarget.java,v 1.12 2003/03/07 16:56:02 taqua Exp $
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
 * 10-Dec-2002 : Javadoc updates (DG);
 * 29-Jan-2003 : Extracted SizeCalculator super-interface.
 */

package com.jrefinery.report.targets.pageable;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.DrawableContainer;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.pageable.physicals.PhysicalPage;
import com.jrefinery.report.util.ReportConfiguration;

/**
 * An interface that defines the methods that must be supported by a report output target.
 * JFreeReport currently implements three targets:  one for Graphics2D (screen and printer),
 * one for Acrobat PDF files and an other target for PlainText output.
 *
 * @author David Gilbert
 */
public interface OutputTarget extends LayoutSupport
{
  /** Literal text for the 'title' property name. */
  public static final String TITLE = "Title";

  /** Literal text for the 'author' property name. */
  public static final String AUTHOR = "Author";

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
   * Returns true if the target is open, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isOpen ();

  /**
   * Closes the target.
   *
   * @throws OutputTargetException if there is some problem closing the target.
   */
  public void close () throws OutputTargetException;

  /**
   * Signals that a page is being started.  Stores the state of the target to
   * make it possible to restore the complete output target.
   *
   * @param page  the physical page.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void beginPage (PhysicalPage page) throws OutputTargetException;

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is finished,
   * others can simply ignore this message.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void endPage () throws OutputTargetException;

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
   * Returns the current font.
   *
   * @return the current font.
   */
  public FontDefinition getFont ();

  /**
   * Sets the font.
   *
   * @param font  the font.
   *
   * @throws OutputTargetException if there is a problem setting the font.
   */
  public void setFont (FontDefinition font) throws OutputTargetException;

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
   * Defines the bounds for the next set of operations.
   *
   * @param bounds  the bounds.
   */
  public void setOperationBounds (Rectangle2D bounds);

  /**
   * Returns the operation bounds.
   *
   * @return  the bounds.
   */
  public Rectangle2D getOperationBounds ();

  /**
   * Draws a string at the current cursor position.
   *
   * @param text  the text.
   */
  public void drawString (String text);

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
   * Draws a drawable relative to the current position.
   *
   * @param drawable the drawable to draw.
   */
  public void drawDrawable (DrawableContainer drawable);

  /**
   * Draws a image relative to the specified coordinates.
   *
   * @param image The image to draw (as ImageReference for possible embedding of raw data).
   *
   * @throws OutputTargetException if there is a problem setting the paint.
   */
  public void drawImage (ImageReference image) throws OutputTargetException;

  /**
   * Creates an output target that mimics a real output target, but produces no output.
   * This is used by the reporting engine when it makes its first pass through the report,
   * calculating page boundaries etc.  The second pass will use a real output target.
   *
   * @return a dummy output target.
   */
  public OutputTarget createDummyWriter ();

  /**
   * Configures the output target.
   *
   * @param config  the configuration.
   */
  public void configure (ReportConfiguration config);

  /**
   * Returns the logical page.
   *
   * @return the logical page.
   */
  public LogicalPage getLogicalPage();
}
