/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

import java.awt.geom.Point2D;

public class Point2DObjectDescription extends AbstractObjectDescription
{
  public Point2DObjectDescription()
  {
    super(Point2D.class);
    setParameterDefinition("x", Float.class);
    setParameterDefinition("y", Float.class);
  }

  public Object createObject()
  {
    Point2D point = new Point2D.Float();

    float x = getFloatParameter("x");
    float y = getFloatParameter("y");
    point.setLocation(x,y);
    return point;
  }

  private float getFloatParameter (String param)
  {
    Float p = (Float) getParameter(param);
    if (p == null) return 0;
    return p.floatValue();
  }
}
