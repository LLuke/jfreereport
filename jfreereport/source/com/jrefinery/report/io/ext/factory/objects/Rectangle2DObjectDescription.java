/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

import java.awt.geom.Rectangle2D;

public class Rectangle2DObjectDescription extends AbstractObjectDescription
{
  public Rectangle2DObjectDescription()
  {
    super(Rectangle2D.class);
    setParameterDefinition("width", Float.class);
    setParameterDefinition("height", Float.class);
    setParameterDefinition("x", Float.class);
    setParameterDefinition("y", Float.class);
  }

  public Object createObject()
  {
    Rectangle2D rect = new Rectangle2D.Float();

    float w = getFloatParameter("width");
    float h = getFloatParameter("height");
    float x = getFloatParameter("x");
    float y = getFloatParameter("y");
    rect.setRect(x,y,w,h);
    return rect;
  }

  private float getFloatParameter (String param)
  {
    Float p = (Float) getParameter(param);
    if (p == null) return 0;
    return p.floatValue();
  }
}
