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
 * $Id: Element.java,v 1.13 2002/09/13 15:38:03 mungady Exp $
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
import com.jrefinery.report.targets.style.ElementDefaultStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleSheet;

import java.awt.Paint;
import java.io.Serializable;

/**
 * Base class for all report elements (display items that can appear within a report band).
 *
 * @author DG
 */
public abstract class Element implements DataTarget, Serializable, Cloneable
{
  /** A null datasource. */
  private static final DataSource NULL_DATASOURCE = new EmptyDataSource();

  /** The head of the data source chain. */
  private DataSource datasource;

  /** The name of the element. */
  private String name;

  /** The stylesheet defines global appeareance for elements */
  private ElementStyleSheet style;

  public static final int LEFT = 1;
  public static final int RIGHT = 2;
  public static final int CENTER = 3;

  public static final int TOP = 14;
  public static final int MIDDLE = 15;
  public static final int BOTTOM = 16;

  /**
   * Constructs an element.
   * <p>
   * The element is set to have no paint (the bands paint is used),
   * the element is visible and has the bounds of (0,0,0,0). The name of the
   * element is set to an anonymous construct ("anonymous@" + hashCode()) and the
   * datasource assigned with this element is set to a default source, which always
   * returns null.
   */
  protected Element()
  {
    setName("anonymousElement@" + hashCode());
    datasource = NULL_DATASOURCE;
    setStyle(new ElementStyleSheet(getName()));
    getStyle().addParent(ElementDefaultStyleSheet.getDefaultStyle());
  }

  /**
   * Defines the name for this element. The name must not be empty, or a NullPointerException
   * is thrown.
   *
   * @param name the name of this element (null not permitted)
   */
  public void setName(String name)
  {
    if (name == null)
    {
      throw new NullPointerException("The name must be valid");
    }
    this.name = name;
  }

  /**
   * Returns the name of the element. The name of the element cannot be null.
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
    Paint retval = (Paint) getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    if (retval == null) throw new IllegalStateException("No Paint Defined?");
    return retval;
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
   * @deprecated StyleSheets cascade by default, this method is no longer needed.
   * @return The paint for this element.
   */
  public Paint getPaint(StyleSheet band)
  {
    return getPaint();
  }

  /**
   * Sets the m_paint for this element. The m_paint can be null, in this case the
   * default paint of the band used to draw this element is used.
   *
   * @param p  the m_paint for this element (null permitted).
   *
   */
  public void setPaint(Paint p)
  {
    getStyle().setStyleProperty(ElementStyleSheet.PAINT, p);
  }

  /**
   * Returns the datasource for this element. You cannot override this function as the
   * element needs always be the last consumer in the chain of filters. This function
   * must never return null.
   *
   * @return the assigned datasource.
   */
  public final DataSource getDataSource()
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
  public void setDataSource(DataSource ds)
  {
    if (ds == null)
    {
      throw new NullPointerException("Null datasource is invalid");
    }
    this.datasource = ds;
  }

  /**
   * Queries this element's datasource for a value.
   *
   * @return the value of the datasource, which can be null.
   */
  public Object getValue()
  {
    DataSource ds = getDataSource();
    return ds.getValue();
  }

  /**
   * Checks whether an element is equal to this element. Equality is based on names, so make
   * sure, that you have no elements with the same name within the same band.
   *
   * @param o  the object to test against this element for equality.
   *
   * @return A boolean indicating equal or not equal.
   */
  public boolean equals(Object o)
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
   * @return the current visiblity state.
   */
  public boolean isVisible()
  {
    Boolean b = (Boolean) getStyle().getStyleProperty(ElementStyleSheet.VISIBLE, Boolean.FALSE);
    return b.booleanValue();
  }

  /**
   * Defines, whether this element will be drawn.
   *
   * @param b the new visibility state
   */
  public void setVisible(boolean b)
  {
    getStyle().setStyleProperty(ElementStyleSheet.VISIBLE, new Boolean(b));
  }

  /**
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    Element e = (Element) super.clone();
    e.style = (ElementStyleSheet) style.clone();
    e.datasource = (DataSource) datasource.clone();
    return e;
  }

  public ElementStyleSheet getStyle()
  {
    return style;
  }

  public void setStyle(ElementStyleSheet style)
  {
    this.style = style;
  }

  public abstract String getContentType ();

}
