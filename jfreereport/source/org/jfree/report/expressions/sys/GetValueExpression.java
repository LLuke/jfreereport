/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.expressions.sys;

import org.jfree.report.expressions.AbstractExpression;
import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 02.12.2006, 22:07:57
 *
 * @author Thomas Morgner
 */
public class GetValueExpression extends AbstractExpression
{
  private String field;

  public GetValueExpression()
  {
  }

  public GetValueExpression(final String field)
  {
    this.field = field;
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
  public Object computeValue() throws DataSourceException
  {
    return getDataRow().get(getField());
  }
}
