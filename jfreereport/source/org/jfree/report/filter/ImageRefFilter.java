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
 * ImageRefFilter.java
 * -------------------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageRefFilter.java,v 1.15 2003/06/29 16:59:24 taqua Exp $
 *
 * Changes
 * -------
 * 28-Jun-2002 : Version 1 (TM);
 * 16-Jul-2002 : Added standard header and Javadoc comments (DG);
 * 08-Aug-2002 : unused imports removed
 * 14-Aug-2002 : BugFix: If the value from the datasource is already an ImageRef, then
 */

package org.jfree.report.filter;

import java.awt.Image;
import java.io.Serializable;

import org.jfree.report.ImageReference;

/**
 * A filter that converts an Image to an ImageReference. The DataSource is expected to contain an
 * java.awt.Image, the image is then wrapped into an ImageReference and this ImageReference is
 * returned to the caller.
 *
 * @author Thomas Morgner
 */
public class ImageRefFilter implements DataFilter, Serializable
{
  /**
   * Default constructor.
   */
  public ImageRefFilter()
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
    if (o instanceof ImageReference)
    {
      return o;
    }
    if (o == null || (o instanceof Image) == false)
    {
      return null;
    }

    return new ImageReference((Image) o);
  }

  /**
   * Returns the data type for the filter.
   * <P>
   * The type is ImageReference.
   *
   * @return The data type.
   */
  public Class getDataType()
  {
    return ImageReference.class;
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
    final ImageRefFilter r = (ImageRefFilter) super.clone();
    if (dataSource != null)
    {
      r.dataSource = (DataSource) dataSource.clone();
    }
    return r;
  }

}