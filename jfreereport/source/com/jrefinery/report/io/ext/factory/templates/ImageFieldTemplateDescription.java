/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: ImageFieldTemplateDescription.java,v 1.1 2003/01/14 21:08:25 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.ImageFieldTemplate;

public class ImageFieldTemplateDescription extends AbstractTemplateDescription
{
  public ImageFieldTemplateDescription(String name)
  {
    super(name, ImageFieldTemplate.class, true);
  }
}
