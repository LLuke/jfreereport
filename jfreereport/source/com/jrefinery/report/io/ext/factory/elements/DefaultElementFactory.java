/**
 * Date: Jan 12, 2003
 * Time: 6:08:03 PM
 *
 * $Id: DefaultElementFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.elements;

import com.jrefinery.report.ImageElement;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.TextElement;

public class DefaultElementFactory extends AbstractElementFactory
{
  public DefaultElementFactory()
  {
    registerElement(new TextElement());
    registerElement(new ShapeElement());
    registerElement(new ImageElement());
  }
}
