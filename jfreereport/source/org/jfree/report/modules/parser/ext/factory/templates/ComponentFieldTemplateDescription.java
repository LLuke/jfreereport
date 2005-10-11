/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * DrawableFieldTemplateDescription.java
 * ----------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DrawableFieldTemplateDescription.java,v 1.4 2005/02/23 21:05:40 taqua Exp $
 *
 * Changes (from 09-Apr-2003)
 * -------------------------
 * 09-Apr-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.ext.factory.templates;

import org.jfree.report.filter.templates.ComponentFieldTemplate;

/**
 * A drawable field template description.
 *
 * @author Thomas Morgner.
 */
public class ComponentFieldTemplateDescription extends AbstractTemplateDescription
{
  /**
   * Creates a new template description.
   *
   * @param name the name.
   */
  public ComponentFieldTemplateDescription (final String name)
  {
    super(name, ComponentFieldTemplate.class, true);
  }
}