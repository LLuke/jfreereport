/**
 * Date: Mar 7, 2003
 * Time: 6:02:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.ShapeFieldTemplate;

public class ShapeFieldTemplateDescription  extends AbstractTemplateDescription
{
  /**
   * Creates a new template description.
   *
   * @param name  the name.
   */
  public ShapeFieldTemplateDescription(String name)
  {
    super(name, ShapeFieldTemplate.class, true);
  }
}