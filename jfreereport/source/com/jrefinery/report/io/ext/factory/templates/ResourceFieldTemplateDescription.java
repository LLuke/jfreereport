/**
 * Date: Jan 24, 2003
 * Time: 6:55:36 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.ResourceFieldTemplate;

public class ResourceFieldTemplateDescription extends AbstractTemplateDescription
{
  public ResourceFieldTemplateDescription(String name)
  {
    super(name, ResourceFieldTemplate.class, true);
  }
}
