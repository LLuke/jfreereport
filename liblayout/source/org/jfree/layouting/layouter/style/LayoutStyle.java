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
 * LayoutStyle.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LayoutStyle.java,v 1.2 2006/04/17 20:51:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.style;

import java.util.Arrays;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Unlike the old JFreeReport stylesheet, this implementation has no inheritance at all.
 * It needs to be resolved manually, which is no bad thing, as we have to do this anyway
 * during the computation.
 *
 * @author Thomas Morgner
 */
public class LayoutStyle
{
  private CSSValue[] values;
  private LayoutStylePool layoutStylePool;
  private Object reference;

  public LayoutStyle ()
  {
  }

  public LayoutStyle (final LayoutStylePool layoutStylePool)
  {
    this.layoutStylePool = layoutStylePool;
  }

  public Object getReference ()
  {
    return reference;
  }

  public void setReference (Object reference)
  {
    this.reference = reference;
  }

  public synchronized CSSValue getValue (StyleKey key)
  {
    if (values == null)
    {
      return null;
    }
    return values[key.getIndex()];
  }

  public synchronized void setValue (StyleKey key, CSSValue value)
  {
    if (values == null)
    {
      values = new CSSValue[StyleKeyRegistry.getRegistry().getKeyCount()];
    }

    values[key.getIndex()] = value;
  }

  public synchronized void dispose ()
  {
    if (layoutStylePool == null)
    {
      return;
    }
    if (values == null)
    {
      return;
    }
    Arrays.fill(values, null);
    layoutStylePool.reclaim(this, reference);
  }

  public synchronized LayoutStyle createCopy ()
  {
    final LayoutStyle style = LayoutStylePool.getPool().getStyle();
    if (values == null)
    {
      return style;
    }
    if (style.values == null)
    {
      style.values = (CSSValue[]) values.clone();
    }

    System.arraycopy(style.values, 0, values, 0, values.length);
    return style;
  }
}
