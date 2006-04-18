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
 * ShowElementByNameFunction.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ShowElementByNameFunction.java,v 1.2 2005/10/01 11:48:16 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.function;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.report.DataSourceException;
import org.jfree.report.structure.Element;
import org.jfree.util.ObjectUtilities;

/**
 * This function hiddes the elements with the name specified in the 'element'
 * parameter, if the given field has one of the values specified in the
 * values array.
 */
public class ShowElementByNameExpression extends AbstractElementFormatExpression
{
  private String field;
  private ArrayList values;

  public ShowElementByNameExpression ()
  {
    values = new ArrayList();
  }

  public String getField ()
  {
    return field;
  }

  public void setField (final String field)
  {
    this.field = field;
  }


  public void setValues (final int index, final Object value)
  {
    if (values.size() == index)
    {
      values.add(value);
    }
    else
    {
      values.set(index, value);
    }
  }

  public Object getValues (final int index)
  {
    return values.get(index);
  }

  public Object[] getValues()
  {
    return values.toArray();
  }

  public void setValues (Object[] values)
  {
    this.values.clear();
    this.values.addAll(Arrays.asList(values));
  }

  public int getValuesCount ()
  {
    return this.values.size();
  }

  protected void processElement(Element element) throws DataSourceException
  {
    // show, if the value in the field is not equal to the element's name.
    // this is the opposite of the HideElementByName function.
    final boolean visible = isVisible();
    final Element[] elements = FunctionUtilities.findAllElements(element, getNamespace(), getElement());
    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      e.setEnabled(visible);
    }
  }

  private boolean isVisible() throws DataSourceException
  {
    final Object fieldValue = getDataRow().get(getField());
    for (int i = 0; i < values.size(); i++)
    {
      Object o = values.get(i);
      if (ObjectUtilities.equal(fieldValue, o))
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Clones the expression.
   *
   * @return a copy of this expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final ShowElementByNameExpression ex = (ShowElementByNameExpression) super.clone();
    ex.values = (ArrayList) values.clone();
    return ex;
  }


  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getField()});
  }
  
}
