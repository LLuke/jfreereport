/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: ShortObjectDescription.java,v 1.2 2003/01/13 19:00:57 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;



public class ShortObjectDescription extends AbstractObjectDescription
{
  public ShortObjectDescription()
  {
    super(Short.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    return Short.valueOf(o);
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Short)
    {
      setParameter("value", String.valueOf(o));
    }
  }
}
