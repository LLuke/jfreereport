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
 * DefaultTemplateCollection.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultTemplateCollection.java,v 1.8 2003/03/07 17:05:08 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.factory.templates;

/**
 * A template collection.
 *
 * @author Thomas Morgner.
 */
public class DefaultTemplateCollection extends TemplateCollection
{
  /**
   * Creates a new collection.
   */
  public DefaultTemplateCollection()
  {
    addTemplate(new DateFieldTemplateDescription("date-field"));
    addTemplate(new ImageFieldTemplateDescription("image-field"));
    addTemplate(new ImageURLFieldTemplateDescription("image-url-field"));
    addTemplate(new ImageURLElementTemplateDescription("image-url-element"));
    addTemplate(new LabelTemplateDescription("label"));
    addTemplate(new NumberFieldTemplateDescription("number-field"));
    addTemplate(new StringFieldTemplateDescription("string-field"));
    addTemplate(new ResourceFieldTemplateDescription("resource-field"));
    addTemplate(new ResourceLabelTemplateDescription("resource-label"));
    addTemplate(new ShapeFieldTemplateDescription("shape-field"));
  }
}
