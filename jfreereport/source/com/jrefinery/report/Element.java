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
 * ------------
 * Element.java
 * ------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: Element.java,v 1.12 2002/09/05 08:31:51 taqua Exp $
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
 * 04-Jun-2002 : Documentation tags changed. A default name is generated, a default datasource
 *               is set. Elements paint is no longer protected, the paint is retrieved by the
 *               getPaint (Band) method. If neither band nor element have a paint declared, fail
 *               with and exception.
 * 04-Jul-2002 : Serializable and Cloneable
 * 05-Sep-2002 : Documentation
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.DataTarget;
import com.jrefinery.report.filter.EmptyDataSource;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Base class for all report elements (display items that can appear within a report band).
 *
 * @author DG
 */
public abstract class Element implements ElementConstants, DataTarget, Serializable, Cloneable
{
  /** A null datasource. */
  private static final DataSource NULL_DATASOURCE = new EmptyDataSource ();

  /** The head of the data source chain. */
  private DataSource datasource;

  /** The name of the element. */
  private String name;

  /** The area that the element occupies within its band. */
  private Rectangle2D area;

  /** The paint used to draw the element. */
  private Paint paint;

  /** The visiblity of an element is used to decide whether the element is printed */
  private boolean visible;

  /**
   * Constructs an element.
   * <p>
   * The element is set to have no paint (the bands paint is used),
   * the element is visible and has the bounds of (0,0,0,0). The name of the
   * element is set to an anonymous construct ("anonymous@" + hashCode()) and the
   * datasource assigned with this element is set to a default source, which always
   * returns null.
   */
  protected Element ()
  {
    // Initialize the rectangles
    this.area = new Rectangle2D.Float ();

    // and inform all superclasses (if they override set bounds)
    setBounds (new Rectangle2D.Float ());

    setVisible (true);
    setPaint (null);
    setName ("anonymous@" + hashCode ());
    // initialize the private datasource to be valid.
    datasource = NULL_DATASOURCE;
  }

  /**
   * Defines the name for this element. The name must not be empty, or a NullPointerException
   * is thrown.
   *
   * @param name the name of this element (null not permitted)
   */
  public void setName (String name)
  {
    if (name == null)
    {
      throw new NullPointerException ("The name must be valid");
    }
    this.name = name;
  }

  /**
   * Defines the bounds for this element.
   * <p>
   * The bounds must not be null, or a NullPointerException is thrown.
   * The contents of the bounds are copied into this elements bounds, the parameter
   * object can be reused by the caller.
   *
   * @param bounds the bounds of this element
   *
   *  @throws NullPointerException
   */
  public void setBounds (Rectangle2D bounds)
  {
    if (bounds == null)
    {
      throw new NullPointerException ("Bounds must be valid");
    }

    this.area.setRect (bounds);

  }

  /**
   * Returns the name of the element. The name of the element cannot be null.
   *
   * @return The name.
   */
  public String getName ()
  {
    return this.name;
  }

  /**
   * Returns the m_paint used to draw this element.
   *
   * @return The m_paint.
   */
  public Paint getPaint ()
  {
    return getPaint (null);
  }

  /**
   * Returns the paint for this element.
   * <p>
   * If a paint has been explicitly set for the element,
   * then it is used.  If nothing at all has been
   * specified, the band's default paint is used.
   * <p>
   * If no band is specified, this function may return null.
   * If a band is defined, the function will never return null.
   * If neither the band and the element have defined a paint, a
   * NullPointerException is thrown instead.
   *
   * @param band  the band that the element belongs to (used to obtain default settings).
   *
   * @return The paint for this element.
   */
  public Paint getPaint (Band band)
  {
    if (band == null)
    {
      return this.paint;
    }

    Paint result = this.paint;

    if (this.paint == null)
    {
      result = band.getDefaultPaint ();
    }
    if (result == null)
    {
      throw new NullPointerException ("Neither element nor band have defined a Paint, this is not "
                                    + "valid");
    }
    return result;

  }

  /**
   * Sets the m_paint for this element. The m_paint can be null, in this case the
   * default paint of the band used to draw this element is used.
   *
   * @param p  the m_paint for this element (null permitted).
   *
   */
  public void setPaint (Paint p)
  {
    this.paint = p;
  }

  /**
   * Returns the datasource for this element. You cannot override this function as the
   * element needs always be the last consumer in the chain of filters. This function
   * must never return null.
   *
   * @return the assigned datasource.
   */
  public final DataSource getDataSource ()
  {
    return datasource;
  }

  /**
   * Define the datasource for this element. This datasource is queried on populateElements(),
   * to fill in the values. The datasource for an element must not be null.
   *
   * @param ds  the datasource.
   *
   * @throws NullPointerException if an null-datasource is set.
   */
  public void setDataSource (DataSource ds)
  {
    if (ds == null)
    {
      throw new NullPointerException ("Null datasource is invalid");
    }
    this.datasource = ds;
  }

  /**
   * Queries this element's datasource for a value.
   *
   * @return the value of the datasource, which can be null.
   */
  public Object getValue ()
  {
    DataSource ds = getDataSource ();
    return ds.getValue ();
  }

  /**
   * Queries the bounds of this element using a new Rectangle2D object.
   *
   * @return The element bounds (in a new Rectangle2D).
   */
  public Rectangle2D getBounds ()
  {
    return getBounds (null);
  }

  /**
   * Queries the bounds of this element. If carrier is null, a new rectangle is created,
   * otherwise the bounds for this element are filled into the carrier.
   *
   * @param carrier  the rectangle2D object to carry the bounds of this element.
   *
   * @return  The element bounds.
   */
  public Rectangle2D getBounds (Rectangle2D carrier)
  {
    if (carrier == null)
    {
      carrier = new Rectangle2D.Float ();
    }
    carrier.setRect (area);
    return carrier;
  }

  /**
   * Checks whether an element is equal to this element. Equality is based on names, so make
   * sure, that you have no elements with the same name within the same band.
   *
   * @param o  the object to test against this element for equality.
   *
   * @return A boolean indicating equal or not equal.
   */
  public boolean equals (Object o)
  {
    if (o instanceof Element)
    {
      Element el = (Element) o;
      return el.getName ().equals (getName ());
    }
    return false;
  }

  /**
   * Defines whether this element will be painted. Regardless of the state of this property,
   * a band will reserve space for this element.
   *
   * @return the current visiblity state.
   */
  public boolean isVisible ()
  {
    return visible;
  }

  /**
   * Defines, whether this element will be drawn.
   *
   * @param b the new visibility state
   */
  public void setVisible (boolean b)
  {
    this.visible = b;
  }

  /**
   * Draws the element at its location relative to the band.
   *
   * @param target The output target.
   * @param band The band that the element is being drawn inside of.
   *
   * @throws OutputTargetException if there is any problem with the output target.
   */
  public abstract void draw (OutputTarget target, Band band) throws OutputTargetException;

  /**
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    Element e = (Element) super.clone ();
    e.area = (Rectangle2D) area.clone ();
    e.datasource = (DataSource) datasource.clone ();
    return e;
  }

}
