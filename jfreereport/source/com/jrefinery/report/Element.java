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
 * ------------
 * Element.java
 * ------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: Element.java,v 1.3 2002/05/16 12:14:56 jaosch Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Integration of Thomas Morgner's code, plus PDF report generation via iText (DG);
 * 10-May-2002 : Removed complex-Constructor to support automatic generation of elements using
 *               generic parsers.
 * 16-May-2002 : Line delimiters adjusted
 *               paint now protected member
 * 20-May-2002 : Support for DataTarget interface added. The drawing scheme has changed to fit
 *               the new OutputTarget implementation
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DataTarget;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

/**
 * Base class for all report elements (display items that can appear within a report band).
 */
public abstract class Element implements ElementConstants, DataTarget
{
  private DataSource datasource;

  /** The name of the element. */
  private String name;

  /** The area that the element occupies within its band. */
  private Rectangle2D area;

  /** The paint used to draw the element. */
  protected Paint m_paint;

  /**
   * Constructs an element.
   */

  protected Element()
  {
    setPaint(Color.black);
    setBounds(new Rectangle2D.Float());
  }

  /**
   * Defines the name for this element. The name must not be empty, or a NullPointerException
   * is raised.
   *
   * @param name the name of this element
   * @throws NullPointerException if the name is empty
   */
  public void setName(String name)
  {
    if (name == null)
      throw new NullPointerException("The name must be valid");
    this.name = name;
  }

  /**
   * Defines the bounds for this element. The bounds must not be null, or a NullPointerException
   * is thrown.
   *
   * @param bounds the bounds of this element
   * @throws NullPointerException
   */
  public void setBounds(Rectangle2D bounds)
  {
    if (bounds == null)
      throw new NullPointerException("Bounds must be valid");

    this.area = bounds;

  }

  /**
   * Returns the name of the element.
   *
   * @return The name.
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * Returns the m_paint used to draw this element.
   *
   * @return The m_paint.
   */
  public Paint getPaint()
  {
    return m_paint;
  }

  /**
   * Sets the m_paint for this element. The m_paint must not be null.
   *
   * @param p the m_paint for this element
   * @throws NullPointerException if the m_paint is null
   */
  public void setPaint(Paint p)
  {
    m_paint = p;
  }

  public final DataSource getDataSource ()
  {
    return datasource;
  }

  public void setDataSource (DataSource ds)
  {
    if (ds == null)
      throw new NullPointerException("Null datasource is invalid");
    this.datasource = ds;
  }

  public Object getValue ()
  {
    DataSource ds = getDataSource();
    if (ds == null) return null;
    return ds.getValue();
  }

  /**
   * Queries the bounds of this element using a new Rectangle2D object.
   */
  public Rectangle2D getBounds()
  {
    return getBounds(null);
  }

  /**
   * Queries the bounds of this element. If carrier is null, a new rectangle is created,
   * otherwise the bounds for this element are filled into the carrier.
   *
   * @param carrier the rectangle2D object to carry the bounds of this element.
   */
  public Rectangle2D getBounds(Rectangle2D carrier)
  {
    if (carrier == null)
      carrier = new Rectangle2D.Float();
    carrier.setRect(area);
    return carrier;
  }

  /**
   * Draws the element at its location relative to the band co-ordinates supplied.
   *
   * @param target The output target.
   * @param band The band that the element is being drawn inside of.
   */
  public abstract void draw(OutputTarget target, Band band) throws OutputTargetException;

}