/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: Point2DObjectDescription.java,v 1.2 2003/01/13 19:00:56 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

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

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Point2D == false)
      throw new ObjectFactoryException("In not assignable");

    Point2D point = (Point2D) o;
    float x = (float) point.getX();
    float y = (float) point.getY();

    setParameter("x", new Float(x));
    setParameter("y", new Float(y));
  }
}
