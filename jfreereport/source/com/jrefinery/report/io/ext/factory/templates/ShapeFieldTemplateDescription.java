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
 * ----------------------------------
 * ShapeFieldTemplateDescription.java
 * ----------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeFieldTemplateDescription.java,v 1.3 2003/06/27 14:25:19 taqua Exp $
 *
 * Changes (from 09-Apr-2003)
 * -------------------------
 * 09-Apr-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.ShapeFieldTemplate;

/**
 * A shape field template description.
 *
 * @author Thomas Morgner.
 */
public class ShapeFieldTemplateDescription extends AbstractTemplateDescription
{
  /**
   * Creates a new template description.
   *
   * @param name  the name.
   */
  public ShapeFieldTemplateDescription(final String name)
  {
    super(name, ShapeFieldTemplate.class, true);
  }
}