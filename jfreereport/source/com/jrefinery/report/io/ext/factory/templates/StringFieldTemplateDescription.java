/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: StringFieldTemplateDescription.java,v 1.1 2003/01/14 21:09:16 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.StringFieldTemplate;

public class StringFieldTemplateDescription extends AbstractTemplateDescription
{
  public StringFieldTemplateDescription(String name)
  {
    super(name, StringFieldTemplate.class, true);
  }
}
