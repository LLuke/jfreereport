/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: StringFieldTemplateDescription.java,v 1.2 2003/01/13 19:01:09 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.StringFieldTemplate;

public class StringFieldTemplateDescription extends AbstractTemplateDescription
{
  public StringFieldTemplateDescription(String name)
  {
    super(name, StringFieldTemplate.class);
  }
}
