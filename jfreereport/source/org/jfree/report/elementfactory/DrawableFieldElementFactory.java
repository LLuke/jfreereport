/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 06.07.2003 : Initial version
 *  
 */

package org.jfree.report.elementfactory;

import org.jfree.report.Element;
import org.jfree.report.DrawableElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DrawableFilter;

public class DrawableFieldElementFactory extends ElementFactory
{
  private String fieldname;

  public DrawableFieldElementFactory()
  {
  }

  public String getFieldname()
  {
    return fieldname;
  }

  public void setFieldname(String fieldname)
  {
    this.fieldname = fieldname;
  }

  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    DrawableElement element = new DrawableElement();
    if (getName() != null)
    {
      element.setName(getName());
    }

    final DataRowDataSource drds = new DataRowDataSource(getFieldname());
    final DrawableFilter filter = new DrawableFilter();
    filter.setDataSource(drds);
    element.setDataSource(filter);

    ElementStyleSheet style = element.getStyle();
    style.setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, getAbsolutePosition());
    style.setStyleProperty(ElementStyleSheet.MAXIMUMSIZE, getMaximumSize());
    style.setStyleProperty(ElementStyleSheet.MINIMUMSIZE, getMinimumSize());
    style.setStyleProperty(ElementStyleSheet.PREFERREDSIZE, getPreferredSize());

    return element;
  }
}
