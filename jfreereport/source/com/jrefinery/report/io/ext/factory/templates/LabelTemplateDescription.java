/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: LabelTemplateDescription.java,v 1.1 2003/01/14 21:08:56 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.LabelTemplate;

public class LabelTemplateDescription extends AbstractTemplateDescription
{
  public LabelTemplateDescription(String name)
  {
    super(name, LabelTemplate.class, true);
  }
}
