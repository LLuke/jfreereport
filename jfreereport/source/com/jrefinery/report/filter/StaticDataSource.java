/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ---------------------
 * StaticDataSource.java
 * ---------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StaticDataSource.java,v 1.7 2002/09/13 15:38:07 mungady Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 *
 */

package com.jrefinery.report.filter;

/**
 * A data source that returns a constant value.  An example is a label on a report.
 *
 * @author Thomas Morgner
 */
public class StaticDataSource implements DataSource
{
  /** The value. */
  private Object value;

  /**
   * Default constructor.
   */
  public StaticDataSource ()
  {
  }

  /**
   * Constructs a new static data source.
   *
   * @param o The value.
   */
  public StaticDataSource (Object o)
  {
    setValue (o);
  }

  /**
   * Sets the value of the data source.
   *
   * @param o The value.
   */
  public void setValue (Object o)
  {
    this.value = o;
  }

  /**
   * Returns the value of the data source.
   *
   * @return The value.
   */
  public Object getValue ()
  {
    return value;
  }

  /**
   * Clones the data source, although the enclosed 'static' value is not cloned.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }
}
