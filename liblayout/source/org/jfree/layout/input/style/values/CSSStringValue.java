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
 * CSSStringValue.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 23.11.2005, 11:50:28
 *
 * @author Thomas Morgner
 */
public class CSSStringValue implements CSSValue
{
  private CSSStringType type;
  private String value;

  public CSSStringValue(final CSSStringType type, final String value)
  {
    this.type = type;
    this.value = value;
  }

  public CSSStringType getType()
  {
    return type;
  }

  public String getValue()
  {
    return value;
  }

  public String toString()
  {
    return getCSSText();
  }

  public String getCSSText()
  {
    if (type == CSSStringType.URI )
    {
      return "uri(" + value + ")";
    }
    else
    {
      return "\"" + value + "\"";
    }
  }
}
