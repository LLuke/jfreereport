/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: LabelTemplateDescription.java,v 1.2 2003/01/13 19:01:07 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.LabelTemplate;

public class LabelTemplateDescription extends AbstractTemplateDescription
{
  public LabelTemplateDescription(String name)
  {
    super(name, LabelTemplate.class);
  }
}
