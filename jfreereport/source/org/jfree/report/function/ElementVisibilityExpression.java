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
 * ElementVisibilityFunction.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ElementVisibilityFunction.java,v 1.1 2006/01/27 20:15:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.01.2006 : Initial version
 */
package org.jfree.report.function;

import org.jfree.report.DataSourceException;
import org.jfree.report.structure.Element;

/**
 * This expression controls, whether the expressions parent element will be
 * processed. 
 *
 * @author Thomas Morgner
 */
public class ElementVisibilityExpression extends AbstractElementFormatExpression
{
  private String field;

  /**
   * Creates an unnamed function. Make sure the name of the function is set
   * using {@link #setName} before the function is added to the report's
   * function collection.
   */
  public ElementVisibilityExpression()
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
    final boolean visible = isVisible();
    final Element[] elements = FunctionUtilities.findAllElements
            (element, getNamespace(), getElement());
    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      e.setEnabled(visible);
    }

  }

  protected boolean isVisible () throws DataSourceException
  {
    return Boolean.TRUE.equals(getDataRow().get(getField()));
  }
}