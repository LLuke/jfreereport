/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------------
 * BandLayoutManagerSerializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandLayoutManagerSerializer.java,v 1.1 2003/07/07 22:44:09 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30-May-2003 : Initial version
 * 26-Jun-2003 : Documentation.
 */

package org.jfree.report.util.serializers;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.report.layout.BandLayoutManager;
import org.jfree.report.util.SerializeMethod;

/**
 * A SerializeMethod implementation that handles BandLayoutManagers.
 *
 * @author Thomas Morgner
 * @see org.jfree.report.layout.BandLayoutManager
 */
public class BandLayoutManagerSerializer implements SerializeMethod
{
  /**
   * Default Constructor.
   */
  public BandLayoutManagerSerializer()
  {
  }

  /**
   * Writes a serializable object description to the given object output stream.
   * As bandlayoutmanagers need to be instantiable by their default constructor,
   * it is sufficient to write the class of the layout manager.
   *
   * @param o the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject(final Object o, final ObjectOutputStream out) throws IOException
  {
    out.writeObject(o.getClass());
  }

  /**
   * Reads the object from the object input stream. This will read a serialized
   * class name of the BandLayoutManager. The specified class is then instantiated
   * using its default constructor.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   * @throws IOException if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    final Class c = (Class) in.readObject();
    try
    {
      return c.newInstance();
    }
    catch (Exception e)
    {
      throw new NotSerializableException(c.getName());
    }
  }

  /**
   * The class of the object, which this object can serialize.
   *
   * @return the class <code>org.jfree.report.layout.BandLayoutManager</code>.
   */
  public Class getObjectClass()
  {
    return BandLayoutManager.class;
  }
}
