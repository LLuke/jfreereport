/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: ImageURLFieldTemplateDescription.java,v 1.1 2003/01/14 21:08:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.ImageURLFieldTemplate;

public class ImageURLFieldTemplateDescription extends AbstractTemplateDescription
{
  public ImageURLFieldTemplateDescription(String name)
  {
    super(name, ImageURLFieldTemplate.class, true);
  }

}
