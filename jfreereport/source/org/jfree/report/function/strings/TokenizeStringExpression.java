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
 * TokenizeStringExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TokenizeStringExpression.java,v 1.1 2006/01/24 14:17:38 taqua Exp $
 *
 * Changes
 * -------------------------
 * 22.01.2006 : Initial version
 */
package org.jfree.report.function.strings;

import java.util.StringTokenizer;

import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 22.01.2006, 14:38:02
 *
 * @author Thomas Morgner
 */
public class TokenizeStringExpression extends AbstractExpression
{
  private String field;
  private String delimeter;
  private String replacement;
  private String prefix;
  private String suffix;

  public TokenizeStringExpression()
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

  public String getDelimeter()
  {
    return delimeter;
  }

  public void setDelimeter(final String delimeter)
  {
    this.delimeter = delimeter;
  }

  public String getReplacement()
  {
    return replacement;
  }

  public void setReplacement(final String replacement)
  {
    this.replacement = replacement;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(final String prefix)
  {
    this.prefix = prefix;
  }

  public String getSuffix()
  {
    return suffix;
  }

  public void setSuffix(final String suffix)
  {
    this.suffix = suffix;
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

    final StringBuffer buffer = new StringBuffer();

    if (prefix != null)
    {
      buffer.append(prefix);
    }

    final StringTokenizer strtok = new StringTokenizer(text, delimeter, false);
    while (strtok.hasMoreTokens())
    {
      final String o = strtok.nextToken();
      buffer.append(o);
      if (strtok.hasMoreTokens())
      {
        buffer.append(replacement);
      }
    }

    if (suffix != null)
    {
      buffer.append(suffix);
    }

    return buffer.toString();
  }


  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getField()});
  }
  
}
