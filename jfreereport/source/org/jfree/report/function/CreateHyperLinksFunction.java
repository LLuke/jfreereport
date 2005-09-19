/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * CreateHyperLinksFunction.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CreateHyperLinksFunction.java,v 1.2 2005/03/03 23:00:00 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.function;

import org.jfree.report.Band;
import org.jfree.report.Element;

/**
 * Adds hyperlinks to all elements with the name specified in 'element'. The
 * link target is read from a specified field. The column referenced by this
 * field should contain URLs or Strings.
 *
 * @author Thomas Morgner
 */
public class CreateHyperLinksFunction extends AbstractElementFormatFunction
{
  private String field;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public CreateHyperLinksFunction ()
  {
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return null;
  }

  public String getField ()
  {
    return field;
  }

  public void setField (final String field)
  {
    this.field = field;
  }

  protected void processRootBand (final Band b)
  {
    final Object targetRaw = getDataRow().get(getField());
    if (targetRaw == null)
    {
      return;
    }
    final String target = String.valueOf(targetRaw);
    final Element[] elements = FunctionUtilities.findAllElements(b, getElement());
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].setHRefTarget(target);
    }
  }
}
