/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ServletSwingIconsDemoTableModel.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.servletdemo;

import java.net.URL;

import org.jfree.report.URLImageContainer;
import org.jfree.report.demo.SwingIconsDemoTableModel;

public class ServletSwingIconsDemoTableModel extends SwingIconsDemoTableModel
{
  private static class IconImageReference implements URLImageContainer
  {
    private int entryNumber;

    public IconImageReference (final int entryNumber)
    {
      this.entryNumber = entryNumber;
    }

    public String getSourceURLString ()
    {
      return "imageServlet?entry=" + entryNumber;
    }

    public URL getSourceURL ()
    {
      // we have no clue about the URL to the servlet ...
      return null;
    }

    public boolean isLoadable ()
    {
      return false;
    }

    public int getImageHeight ()
    {
      return 24;
    }

    public int getImageWidth ()
    {
      return 24;
    }

    /**
     * Defines the image's horizontal scale. This is the factor to convert the image from
     * it's original resolution to the java resolution of 72dpi.
     * <p/>
     * This is not the scale that is computed by the layouter; that one is derived from the
     * ImageContent.
     *
     * @return the horizontal scale.
     */
    public float getScaleX ()
    {
      return 1;
    }

    /**
     * Defines the image's vertical scale. This is the factor to convert the image from it's
     * original resolution to the java resolution of 72dpi.
     * <p/>
     * This is not the scale that is computed by the layouter; that one is derived from the
     * ImageContent.
     *
     * @return the vertical scale.
     */
    public float getScaleY ()
    {
      return 1;
    }
  }

  /**
   * Creates a new table model.
   */
  public ServletSwingIconsDemoTableModel ()
  {
  }

  /**
   * Creates a new table model.
   *
   * @param url the url for the jlfgr-1_0.jar file (or <code>null</code> to search the
   *            classpath).
   */
  public ServletSwingIconsDemoTableModel (final URL url)
  {
    super(url);
  }

  /**
   * Returns the data item at the specified row and column.
   *
   * @param row    The row index.
   * @param column The column index.
   * @return The data item.
   */
  public Object getValueAt (final int row, final int column)
  {
    if (column == 2)
    {
      return new IconImageReference(row);
    }
    return super.getValueAt(row, column);
  }
}
