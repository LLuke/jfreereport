/**
 * Date: Jan 11, 2003
 * Time: 2:48:47 PM
 *
 * $Id: ImageURLElementTemplateDescription.java,v 1.1 2003/01/14 21:08:43 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.ImageURLElementTemplate;

public class ImageURLElementTemplateDescription extends AbstractTemplateDescription
{
  public ImageURLElementTemplateDescription(String name)
  {
    super(name, ImageURLElementTemplate.class, true);
  }
}
