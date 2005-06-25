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
 * AnchorFieldElementFactory.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AnchorFieldElementFactory.java,v 1.3 2005/03/03 22:59:59 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.elementfactory;

import org.jfree.report.AnchorElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.AnchorFieldTemplate;

/**
 * The AnchorFieldElementFactory can be used to construct Anchor fields. Anchor fields
 * generate Anchor-Objects from content found in a DataRow-column or function.
 *
 * @author Thomas Morgner
 */
public class AnchorFieldElementFactory extends ElementFactory
{
  /** The fieldname. */
  private String fieldname;

  /**
   * Creates a new Factory.
   */
  public AnchorFieldElementFactory ()
  {
  }

  /**
   * Returns the element's field name.
   *
   * @return the fieldname
   */
  public String getFieldname ()
  {
    return fieldname;
  }

  /**
   * Defines the field name. The field name should be the name of a 'marked' report
   * property, a function or expression name or the name of a table model column.
   *
   * @param field the field name.
   */
  public void setFieldname (final String field)
  {
    this.fieldname = field;
  }

  /**
   * Creates a new instance of the element.
   *
   * @return the newly generated instance of the element.
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }
    
    final AnchorElement element = new AnchorElement();
    final AnchorFieldTemplate anchorFieldTemplate = new AnchorFieldTemplate();
    anchorFieldTemplate.setField(getFieldname());

    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(anchorFieldTemplate);
    return element;
  }
}
