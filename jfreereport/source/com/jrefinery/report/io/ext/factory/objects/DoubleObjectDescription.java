/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: DoubleObjectDescription.java,v 1.2 2003/01/13 19:00:54 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;



public class DoubleObjectDescription extends AbstractObjectDescription
{
  public DoubleObjectDescription()
  {
    super(Double.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    return Double.valueOf(o);
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Double)
    {
      setParameter("value", String.valueOf(o));
    }
  }
}
