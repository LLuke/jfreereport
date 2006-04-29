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
 * SimpleEncodingData.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 29.04.2006 : Initial version
 */
package org.jfree.fonts.encoding;

/**
 * Creation-Date: 29.04.2006, 14:22:21
 *
 * @author Thomas Morgner
 */
public class External8BitEncodingData extends EncodingData
{
  private static final long serialVersionUID = 9017639110342367007L;
  
  private int[] indexDelta;
  private int[] valueDelta;

  public External8BitEncodingData(final int[] indexDelta,
                                  final int[] valueDelta)
  {
    if (indexDelta == null)
    {
      throw new NullPointerException();
    }
    if (valueDelta == null)
    {
      throw new NullPointerException();
    }

    this.indexDelta = indexDelta;
    this.valueDelta = valueDelta;
  }

  public int[] getIndexDelta()
  {
    return indexDelta;
  }

  public int[] getValueDelta()
  {
    return valueDelta;
  }

}