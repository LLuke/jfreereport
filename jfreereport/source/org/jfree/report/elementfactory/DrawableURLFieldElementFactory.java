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
 * DrawableURLFieldElementFactory.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.elementfactory;

import java.net.URL;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.DrawableURLFieldTemplate;

public class DrawableURLFieldElementFactory extends ElementFactory
{
  /**
   * The fieldname of the datarow from where to read the content.
   */
  private String fieldname;

  private URL baseURL;

  public DrawableURLFieldElementFactory ()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public String getFieldname ()
  {
    return fieldname;
  }

  /**
   * Defines the field name from where to read the content of the element. The field name
   * is the name of a datarow column.
   *
   * @param fieldname the field name.
   */
  public void setFieldname (final String fieldname)
  {
    this.fieldname = fieldname;
  }

  public URL getBaseURL ()
  {
    return baseURL;
  }

  public void setBaseURL (final URL baseURL)
  {
    this.baseURL = baseURL;
  }

  /**
   * Creates a new drawable field element based on the defined properties.
   *
   * @return the generated elements
   *
   * @throws IllegalStateException if the field name is not set.
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DrawableElement element = new DrawableElement();
    applyElementName(element);
    applyStyle(element.getStyle());

    final DrawableURLFieldTemplate fieldTemplate = new DrawableURLFieldTemplate();
    fieldTemplate.setField(getFieldname());
    fieldTemplate.setBaseURL(getBaseURL());
    element.setDataSource(fieldTemplate);

    return element;
  }

}
