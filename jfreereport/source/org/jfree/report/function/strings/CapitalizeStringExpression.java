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
 * CapitalizeStringExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.01.2006 : Initial version
 */
package org.jfree.report.function.strings;

import org.jfree.report.function.AbstractExpression;

/**
 * Creation-Date: 20.01.2006, 18:19:18
 *
 * @author Thomas Morgner
 */
public class CapitalizeStringExpression extends AbstractExpression
{
  private String field;

  public CapitalizeStringExpression()
  {
  }

  public String getField()
  {
    return field;
  }

  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    Object raw = getDataRow().get(getField());
    if (raw == null)
    {
      return null;
    }
    final String text = String.valueOf(raw);
    final char[] textArray = text.toCharArray();
    boolean startOfWord = true;

    for (int i = 0; i < textArray.length; i++)
    {
      char c = textArray[i];
      // we ignore the punctutation chars or any other possible extra chars
      // for now. Words start at whitespaces ...
      if (Character.isWhitespace(c))
      {
        startOfWord = true;
      }
      else
      {
        if (startOfWord == true)
        {
          textArray[i] = Character.toTitleCase(c);
        }
        startOfWord = false;
      }
    }
    return new String (textArray);
  }
}
