/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * AttributedStringSerializer.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18.02.2006 : Initial version
 */
package org.jfree.serializer.methods;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.text.AttributedString;
import java.util.Map;
import java.util.HashMap;

import org.jfree.serializer.SerializeMethod;

/**
 * Creation-Date: 18.02.2006, 21:53:53
 *
 * @author Thomas Morgner
 */
public class AttributedStringSerializer implements SerializeMethod
{
  public AttributedStringSerializer()
  {
  }

  /**
   * Writes a serializable object description to the given object output
   * stream.
   *
   * @param o   the to be serialized object.
   * @param out the outputstream that should receive the object.
   * @throws IOException if an I/O error occured.
   */
  public void writeObject(Object o, ObjectOutputStream stream) throws IOException
  {
    AttributedString as = (AttributedString) o;
    AttributedCharacterIterator aci = as.getIterator();
    // build a plain string from aci
    // then write the string
    StringBuffer plainStr = new StringBuffer();
    char current = aci.first();
    while (current != CharacterIterator.DONE) {
        plainStr = plainStr.append(current);
        current = aci.next();
    }
    stream.writeObject(plainStr.toString());

    // then write the attributes and limits for each run
    current = aci.first();
    int begin = aci.getBeginIndex();
    while (current != CharacterIterator.DONE) {
        // write the current character - when the reader sees that this
        // is not CharacterIterator.DONE, it will know to read the
        // run limits and attributes
        stream.writeChar(current);

        // now write the limit, adjusted as if beginIndex is zero
        int limit = aci.getRunLimit();
        stream.writeInt(limit - begin);

        // now write the attribute set
        Map atts = new HashMap(aci.getAttributes());
        stream.writeObject(atts);
        current = aci.setIndex(limit);
    }
    // write a character that signals to the reader that all runs
    // are done...
    stream.writeChar(CharacterIterator.DONE);

  }

  /**
   * Reads the object from the object input stream.
   *
   * @param in the object input stream from where to read the serialized data.
   * @return the generated object.
   * @throws IOException            if reading the stream failed.
   * @throws ClassNotFoundException if serialized object class cannot be found.
   */
  public Object readObject(ObjectInputStream stream)
          throws IOException, ClassNotFoundException
  {
    // read string and attributes then create result
    String plainStr = (String) stream.readObject();
    AttributedString result = new AttributedString(plainStr);
    char c = stream.readChar();
    int start = 0;
    while (c != CharacterIterator.DONE) {
        int limit = stream.readInt();
        Map atts = (Map) stream.readObject();
        result.addAttributes(atts, start, limit);
        start = limit;
        c = stream.readChar();
    }
    return result;
  }

  /**
   * The class of the object, which this object can serialize.
   *
   * @return the class of the object type, which this method handles.
   */
  public Class getObjectClass()
  {
    return AttributedString.class;
  }
}
