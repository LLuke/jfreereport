/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * LayoutTextNode.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
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

package org.jfree.layouting.model;

import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.output.OutputProcessor;

/**
 * This represents a text node (also known as CDATA) in the document model.
 * TextNodes always have the same style as their parent element. So we can
 * reuse the parent element's style and do not have to create a new one
 * (or have to compute the style).
 */
public class LayoutTextNode extends LayoutNode
{
  private char[] data;
  private int offset;
  private int length;

  public LayoutTextNode(final ContextId id,
                        final OutputProcessor outputProcessor,
                        final char[] data,
                        final int offset,
                        final int length)
  {
    super(id, outputProcessor);
    if (data == null)
    {
      throw new NullPointerException("Data must not be null");
    }
    if (offset < 0)
    {
      throw new IndexOutOfBoundsException("Offset is invalid");
    }
    if (length < 0)
    {
      throw new IndexOutOfBoundsException("Length is invalid");
    }
    if (offset + length > data.length)
    {
      throw new IndexOutOfBoundsException("StringLength violation");
    }
    this.data = data;
    this.offset = offset;
    this.length = length;
  }

  public LayoutStyle getStyle()
  {
    LayoutElement parent = getParent();
    if (parent == null)
    {
      return null;
    }
    return parent.getStyle();
  }

  public char[] getData()
  {
    return data;
  }

  public int getOffset()
  {
    return offset;
  }

  public int getLength()
  {
    return length;
  }

  public String toString ()
  {
    return "LayoutText={text=" + new String (data, offset, length) + "}";
  }

}
