/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: StaticDataSource.java,v 1.11 2003/06/19 18:44:09 taqua Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 *
 */

package com.jrefinery.report.filter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.jrefinery.report.util.SerializerHelper;

/**
 * A data source that returns a constant value.  An example is a label on a report.
 *
 * @author Thomas Morgner
 */
public class StaticDataSource implements DataSource, Serializable
{
  /** The value. */
  private transient Object value;

  /**
   * Default constructor.
   */
  public StaticDataSource()
  {
  }

  /**
   * Constructs a new static data source.
   *
   * @param o The value.
   */
  public StaticDataSource(Object o)
  {
    setValue(o);
  }

  /**
   * Sets the value of the data source.
   *
   * @param o The value.
   */
  public void setValue(Object o)
  {
    this.value = o;
  }

  /**
   * Returns the value of the data source.
   *
   * @return The value.
   */
  public Object getValue()
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
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Helper method for serialization.
   *
   * @param out the output stream where to write the object.
   * @throws IOException if errors occur while writing the stream.
   */
  private void writeObject(ObjectOutputStream out)
      throws IOException
  {
    out.defaultWriteObject();
    SerializerHelper.getInstance().writeObject(value, out);
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object
   * could not be found.
   */
  private void readObject(java.io.ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    value = SerializerHelper.getInstance().readObject(in);
  }
}
