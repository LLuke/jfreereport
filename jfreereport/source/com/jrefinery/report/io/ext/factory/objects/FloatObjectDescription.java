/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: FloatObjectDescription.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

public class FloatObjectDescription extends AbstractObjectDescription
{
  public FloatObjectDescription()
  {
    super(Float.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    return Float.valueOf(o);
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Float)
    {
      setParameter("value", String.valueOf(o));
    }
  }
}
