/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -------------------
 * DrawableFilter.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DrawableFilter.java,v 1.8 2003/06/29 16:59:24 taqua Exp $
 *
 * Changes
 * -------
 * 07-Mar-2003 : Version 1;
 *
 */

package org.jfree.report.filter;

import java.awt.geom.Rectangle2D;

import org.jfree.report.DrawableContainer;
import org.jfree.ui.Drawable;
import org.jfree.ui.FloatDimension;

/**
 * A filter that converts an Drawable to an DrawableContainer. The DataSource is expected to
 * contain a <code>org.jfree.ui.Drawable</code>, the image is then wrapped into a Drawable
 * container and this DrawableContainer is returned to the caller.
 *
 * @author Thomas Morgner
 * @see org.jfree.ui.Drawable
 */
public class DrawableFilter implements DataFilter
{

  /**
   * Default constructor.
   */
  public DrawableFilter()
  {
  }

  /** The data source. */
  private DataSource dataSource;

  /**
   * Returns the data source for the filter.
   *
   * @return The data source.
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Sets the data source for the filter.
   *
   * @param dataSource The data source.
   */
  public void setDataSource(final DataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  /**
   * Returns the current value for the data source.
   * <P>
   * The returned object, unless it is null, will be an instance of ImageReference.
   *
   * @return The value.
   */
  public Object getValue()
  {
    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return null;
    }
    final Object o = ds.getValue();
    if (o instanceof DrawableContainer)
    {
      return o;
    }
    if (o == null || (o instanceof Drawable) == false)
    {
      return null;
    }

    /**
     * The new drawable has no size and no clipping area, the drawable content
     * can scale to any size, and has no minimum size by default.
     */
    return new DrawableContainer((Drawable) o, new FloatDimension(), new Rectangle2D.Float());
  }

  /**
   * Clones the filter.
   *
   * @return A clone of this filter.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final DrawableFilter r = (DrawableFilter) super.clone();
    if (dataSource != null)
    {
      r.dataSource = (DataSource) dataSource.clone();
    }
    return r;
  }

}
