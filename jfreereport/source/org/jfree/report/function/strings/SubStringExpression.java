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
 * SubStringExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: SubStringExpression.java,v 1.2 2006/01/24 14:17:38 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.01.2006 : Initial version
 */
package org.jfree.report.function.strings;

import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 20.01.2006, 17:47:00
 *
 * @author Thomas Morgner
 */
public class SubStringExpression extends AbstractExpression
{
  private String field;
  private String ellipsis;
  private int start;
  private int length;

  public SubStringExpression()
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

  public String getEllipsis()
  {
    return ellipsis;
  }

  public void setEllipsis(final String ellipsis)
  {
    this.ellipsis = ellipsis;
  }

  public int getStart()
  {
    return start;
  }

  public void setStart(final int start)
  {
    if (length < 0)
    {
      throw new IndexOutOfBoundsException
              ("String start position cannot be negative.");
    }
    this.start = start;
  }

  public int getLength()
  {
    return length;
  }

  public void setLength(final int length)
  {
    if (length < 0)
    {
      throw new IndexOutOfBoundsException(
              "String length cannot be negative.");
    }
    this.length = length;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue() throws DataSourceException
  {
    Object raw = getDataRow().get(getField());
    if (raw == null)
    {
      return null;
    }
    final String text = String.valueOf(raw);

    // the text is not large enough to fit the start-bounds. Return nothing,
    // but indicate that there would have been some more content ...
    if (start >= text.length())
    {
      return appendEllipsis(null);
    }

    // the text fully fits into the given range. No clipping needed ...
    if ((start + length) >= text.length())
    {
      return text;
    }

    return appendEllipsis(text.substring(start, start + length));
  }

  private String appendEllipsis(String value)
  {
    if (ellipsis == null)
    {
      return value;
    }
    if (value == null)
    {
      return ellipsis;
    }
    return value + ellipsis;
  }


  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getField()});
  }
  
}
