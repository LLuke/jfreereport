/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: BooleanObjectDescription.java,v 1.2 2003/01/13 19:00:50 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;



public class BooleanObjectDescription extends AbstractObjectDescription
{
  public BooleanObjectDescription()
  {
    super(Boolean.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    return Boolean.valueOf(o);
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Boolean)
    {
      setParameter("value", String.valueOf(o));
    }
  }
}
