/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

import java.awt.geom.Line2D;

public class Line2DObjectDescription extends AbstractObjectDescription
{
  public Line2DObjectDescription()
  {
    super(Line2D.class);
    setParameterDefinition("x1", Float.class);
    setParameterDefinition("x2", Float.class);
    setParameterDefinition("y1", Float.class);
    setParameterDefinition("y2", Float.class);
  }

  public Object createObject()
  {
    Line2D line = new Line2D.Float();

    float x1 = getFloatParameter("x1");
    float x2 = getFloatParameter("x2");
    float y1 = getFloatParameter("y1");
    float y2 = getFloatParameter("y2");
    line.setLine(x1, y1, x2, y2);
    return line;
  }

  private float getFloatParameter (String param)
  {
    Float p = (Float) getParameter(param);
    if (p == null) return 0;
    return p.floatValue();
  }
}
