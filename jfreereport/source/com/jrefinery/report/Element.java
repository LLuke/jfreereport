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
 * $Id: Element.java,v 1.6 2002/05/27 21:42:46 taqua Exp $
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
 * 26-May-2002 : Elements visible property controls whether an element is drawn by its band
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

  private boolean visible;

  /**
   * Constructs an element.
   */
  protected Element()
  {
    setVisible (true);
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

  /**
   * Returns the datasource for this element. You cannot override this function as the
   * element needs always be the last consumer in the chain.
   *
   * @return the assigned datasource.
   */
  public final DataSource getDataSource ()
  {
    return datasource;
  }

  /**
   * Define the datasource for this element. This datasource is queried on populateElements(),
   * to fill in the values.
   */
  public void setDataSource (DataSource ds)
  {
    if (ds == null)
      throw new NullPointerException("Null datasource is invalid");
    this.datasource = ds;
  }

  /**
   * Queries this elements datasource for a value. If no datasource is set, null will be returned.
   *
   * @returns the value of the datasource, which can be null or also null if no datasource is set.
   */
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
   * Checks whether an element is equal to this element. Equality is based on names, so make
   * sure, that you have no elements with the same name within the same band.
   */
  public boolean equals (Object o)
  {
    if (o instanceof Element)
    {
      Element el = (Element) o;
      return el.getName().equals(getName());
    }
    return false;
  }

  /**
   * Defines whether this element will be painted. Regardless of the state of this property,
   * a band will reserve space for this element.
   *
   * @returns the current visiblity state.
   */
  public boolean isVisible ()
  {
    return visible;
  }

  /**
   * Defines, whether this element will be drawn.
   *
   * @param the new visibility state
   */
  public void setVisible (boolean b)
  {
    this.visible = b;
  }

  /**
   * Draws the element at its location relative to the band co-ordinates supplied.
   *
   * @param target The output target.
   * @param band The band that the element is being drawn inside of.
   */
  public abstract void draw(OutputTarget target, Band band) throws OutputTargetException;

}
