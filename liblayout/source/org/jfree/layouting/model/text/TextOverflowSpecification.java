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
 * TextOverflowSpecification.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TextOverflowSpecification.java,v 1.1 2006/02/12 21:43:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 21.12.2005 : Initial version
 */
package org.jfree.layouting.model.text;

import org.jfree.layouting.input.style.keys.text.TextOverflowMode;
import org.jfree.layouting.input.style.values.CSSStringValue;

/**
 * Creation-Date: 21.12.2005, 14:02:58
 *
 * @author Thomas Morgner
 */
public class TextOverflowSpecification
{
  private TextOverflowMode overflowMode;
  private CSSStringValue lineOverflowText;
  private CSSStringValue boxOverflowText;

  public TextOverflowSpecification()
  {
  }

  public TextOverflowMode getOverflowMode()
  {
    return overflowMode;
  }

  public void setOverflowMode(final TextOverflowMode overflowMode)
  {
    this.overflowMode = overflowMode;
  }

  public CSSStringValue getLineOverflowText()
  {
    return lineOverflowText;
  }

  public void setLineOverflowText(final CSSStringValue lineOverflowText)
  {
    this.lineOverflowText = lineOverflowText;
  }

  public CSSStringValue getBoxOverflowText()
  {
    return boxOverflowText;
  }

  public void setBoxOverflowText(final CSSStringValue boxOverflowText)
  {
    this.boxOverflowText = boxOverflowText;
  }
}
