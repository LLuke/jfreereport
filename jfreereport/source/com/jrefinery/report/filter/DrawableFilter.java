/**
 * Date: Mar 6, 2003
 * Time: 4:51:23 PM
 *
 * $Id: DrawableFilter.java,v 1.1 2003/03/07 13:49:37 taqua Exp $
 */
package com.jrefinery.report.filter;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.DrawableContainer;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.ui.Drawable;

/**
 * A filter that converts an Drawable to an DrawableContainer. The DataSource is expected to contain an
 * com.jrefinery.ui.Drawable, the image is then wrapped into an Drawable container and this DrawableContainer
 * is returned to the caller.
 *
 * @author Thomas Morgner
 * @see Drawable
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
  public void setDataSource(DataSource dataSource)
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
    DataSource ds = getDataSource();
    if (ds == null)
    {
      return null;
    }
    Object o = ds.getValue();
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
    DrawableFilter r = (DrawableFilter) super.clone();
    if (dataSource != null)
    {
      r.dataSource = (DataSource) dataSource.clone();
    }
    return r;
  }

}
