/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: IntegerObjectDescription.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

public class IntegerObjectDescription extends AbstractObjectDescription
{
  public IntegerObjectDescription()
  {
    super(Integer.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    return Integer.valueOf(o);
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Integer)
    {
      setParameter("value", String.valueOf(o));
    }
  }
}
