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
 * DateSpanExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: DateSpanExpression.java,v 1.1 2006/01/20 19:50:52 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.01.2006 : Initial version
 */
package org.jfree.report.function.date;

import java.util.Date;

import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 20.01.2006, 19:13:39
 *
 * @author Thomas Morgner
 */
public class DateSpanExpression extends AbstractExpression
{
  private String startDateField;
  private String endDateField;

  public DateSpanExpression()
  {
  }

  public String getStartDateField()
  {
    return startDateField;
  }

  public void setStartDateField(final String startDateField)
  {
    this.startDateField = startDateField;
  }

  public String getEndDateField()
  {
    return endDateField;
  }

  public void setEndDateField(final String endDateField)
  {
    this.endDateField = endDateField;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue() throws DataSourceException
  {
    if (startDateField == null)
    {
      return null;
    }
    if (endDateField == null)
    {
      return null;
    }

    Object startRaw = getDataRow().get(startDateField);
    if (startRaw instanceof Date == false)
    {
      return null;
    }
    Object endRaw = getDataRow().get(endDateField);
    if (endRaw instanceof Date == false)
    {
      return null;
    }
    Date start = (Date) startRaw;
    Date end = (Date) endRaw;
    return new Date(end.getTime() - start.getTime());
  }


  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getStartDateField(), getEndDateField()});
  }

}
