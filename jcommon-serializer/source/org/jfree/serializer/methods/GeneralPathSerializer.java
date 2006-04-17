/**
 * ===================================================
 * JCommon-Serializer : a free serialization framework
 * ===================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/jcommon-serializer/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
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
 * GeneralPathSerializer.java
 * ------------
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.serializer.methods;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.serializer.SerializeMethod;

/**
 * A serialize method that handles java.awt.geom.GeneralPath objects.
 *
 * @author Thomas Morgner
 */
public class GeneralPathSerializer implements SerializeMethod
{
  /**
   * Default constructor.
   */
  public GeneralPathSerializer ()
  {
  }

  /**
   * The class of the object, which this object can serialize.
   *
   * @return the class of the object type, which this method handles.
   */
  public Class getObjectClass ()
  {
    return GeneralPath.class;
  }

  /**
   * Reads the object from the object input stream.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   *
   * @throws java.io.IOException    if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    final int winding = in.readInt();
    final GeneralPath gp = new GeneralPath(winding);

    // type will be -1 at the end of the GPath ..
    int type = in.readInt();
    while (type >= 0)
    {
      switch (type)
      {
        case PathIterator.SEG_MOVETO:
          {
            final float x = in.readFloat();
            final float y = in.readFloat();
            gp.moveTo(x, y);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            final float x = in.readFloat();
            final float y = in.readFloat();
            gp.lineTo(x, y);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            final float x1 = in.readFloat();
            final float y1 = in.readFloat();
            final float x2 = in.readFloat();
            final float y2 = in.readFloat();
            gp.quadTo(x1, y1, x2, y2);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            final float x1 = in.readFloat();
            final float y1 = in.readFloat();
            final float x2 = in.readFloat();
            final float y2 = in.readFloat();
            final float x3 = in.readFloat();
            final float y3 = in.readFloat();
            gp.curveTo(x1, y1, x2, y2, x3, y3);
            break;
          }
        case PathIterator.SEG_CLOSE:
          {
            break;
          }
        default:
          throw new IOException("Unexpected type encountered: " + type);
      }
      type = in.readInt();
    }
    return gp;
  }

  /**
   * Writes a serializable object description to the given object output stream.
   *
   * @param o   the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws java.io.IOException if an I/O error occured.
   */
  public void writeObject (final Object o, final ObjectOutputStream out)
          throws IOException
  {
    final GeneralPath gp = (GeneralPath) o;
    final PathIterator it = gp.getPathIterator(new AffineTransform());
    out.writeInt(it.getWindingRule());
    while (it.isDone() == false)
    {
      final float[] corrds = new float[6];
      final int type = it.currentSegment(corrds);
      out.writeInt(type);

      switch (type)
      {
        case PathIterator.SEG_MOVETO:
          {
            out.writeFloat(corrds[0]);
            out.writeFloat(corrds[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            out.writeFloat(corrds[0]);
            out.writeFloat(corrds[1]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            out.writeFloat(corrds[0]);
            out.writeFloat(corrds[1]);
            out.writeFloat(corrds[2]);
            out.writeFloat(corrds[3]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            out.writeFloat(corrds[0]);
            out.writeFloat(corrds[1]);
            out.writeFloat(corrds[2]);
            out.writeFloat(corrds[3]);
            out.writeFloat(corrds[4]);
            out.writeFloat(corrds[5]);
            break;
          }
        case PathIterator.SEG_CLOSE:
          {
            break;
          }
        default:
          throw new IOException("Unexpected type encountered: " + type);
      }
      it.next();
    }
    out.writeInt(-1);
  }
}
