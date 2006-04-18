/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
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
 * HideNullValuesFunction.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: HideNullValuesFunction.java,v 1.1 2005/10/01 11:48:59 taqua Exp $
 *
 * Changes
 * -------
 * 01-Oct-2005 : Initial version.
 *
 */
package org.jfree.report.function;

import org.jfree.report.DataSourceException;
import org.jfree.report.structure.Element;

/**
 * Hides the specified elements if the given field contains empty strings or
 * zero numbers.
 *
 * @author Thomas Morgner
 */
public class HideNullValuesExpression extends AbstractElementFormatExpression
{
  private String field;

  public HideNullValuesExpression()
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

  protected void processElement(Element element) throws DataSourceException
  {
    final Element[] elements =
            FunctionUtilities.findAllElements(element, getNamespace(), getElement());
    final Object value = getDataRow().get(getField());
    final boolean visible = isVisible(value);

    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      e.setEnabled(visible);
    }
  }

  protected boolean isVisible(final Object value)
  {
    if (value == null)
    {
      return false;
    }
    if (value instanceof String)
    {
      String strValue = (String) value;
      return strValue.trim().length() > 0;
    }
    if (value instanceof Number)
    {
      Number numValue = (Number) value;
      return numValue.doubleValue() != 0;
    }
    return true;
  }
}
