/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: Line2DObjectDescription.java,v 1.3 2003/01/22 19:38:26 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

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

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Line2D == false)
      throw new ObjectFactoryException("In not assignable");

    Line2D line = (Line2D) o;
    float x1 = (float) line.getX1();
    float x2 = (float) line.getX2();
    float y1 = (float) line.getY1();
    float y2 = (float) line.getY2();

    setParameter("x1", new Float(x1));
    setParameter("x2", new Float(x2));
    setParameter("y1", new Float(y1));
    setParameter("y2", new Float(y2));
  }
}
