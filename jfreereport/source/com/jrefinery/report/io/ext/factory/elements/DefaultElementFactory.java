/**
 * Date: Jan 12, 2003
 * Time: 6:08:03 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.elements;

import com.jrefinery.report.TextElement;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.ImageElement;
import com.jrefinery.report.io.ext.factory.elements.AbstractElementFactory;

public class DefaultElementFactory extends AbstractElementFactory
{
  public DefaultElementFactory()
  {
    registerElement(new TextElement());
    registerElement(new ShapeElement());
    registerElement(new ImageElement());
  }
}
