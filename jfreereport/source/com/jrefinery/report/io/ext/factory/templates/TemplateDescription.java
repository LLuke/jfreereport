/**
 * Date: Jan 14, 2003
 * Time: 1:10:35 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.Template;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

public interface TemplateDescription extends ObjectDescription
{
  public Template createTemplate();
  public String getName();
  public void setName(String name);
}
