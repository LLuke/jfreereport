/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * DrawableFieldElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DrawableFieldElementFactory.java,v 1.6 2003/10/05 21:52:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DrawableFilter;

/**
 * The drawable field element factory can be used to create elements that display
 * org.jfree.ui.Drawable elements.
 * <p>
 * Once the desired properties are set, the factory can be reused to create
 * similiar elements.
 *
 * @author Thomas Morgner
 */
public class DrawableFieldElementFactory extends ElementFactory
{
  /** The fieldname of the datarow from where to read the element data. */
  private String fieldname;

  /**
   * DefaultConstructor.
   */
  public DrawableFieldElementFactory()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public String getFieldname()
  {
    return fieldname;
  }

  /**
   * Defines the field name from where to read the content of the element.
   * The field name is the name of a datarow column.
   *
   * @param fieldname the field name.
   */
  public void setFieldname(final String fieldname)
  {
    this.fieldname = fieldname;
  }

  /**
   * Creates a new drawable field element based on the defined properties.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   *
   * @return the generated elements
   * @throws IllegalStateException if the field name is not set.
   */
  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DrawableElement element = new DrawableElement();
    applyElementName(element);
    applyStyle(element.getStyle());

    final DataRowDataSource drds = new DataRowDataSource(getFieldname());
    final DrawableFilter filter = new DrawableFilter();
    filter.setDataSource(drds);
    element.setDataSource(filter);

    return element;
  }
}
