/**
 * Date: Jan 22, 2003
 * Time: 7:33:26 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.awt.BasicStroke;

public class BasicStrokeObjectDescription extends AbstractObjectDescription
{
  public BasicStrokeObjectDescription()
  {
    super(BasicStroke.class);
    setParameterDefinition("value", String.class);
  }

  private float getFloatParameter (String param)
  {
    Float p = (Float) getParameter(param);
    if (p == null) return 0;
    return p.floatValue();
  }

  public Object createObject()
  {
    float width = getFloatParameter("value");
    return new BasicStroke(width);
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if ((o instanceof BasicStroke) == false)
    {
      throw new ObjectFactoryException("Expected object of type BasicStroke");
    }
    BasicStroke bs = (BasicStroke) o;
    setParameter("value", String.valueOf(bs.getLineWidth()));
  }
}
