/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: StringObjectDescription.java,v 1.2 2003/01/13 19:00:58 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;



public class StringObjectDescription extends AbstractObjectDescription
{
  public StringObjectDescription()
  {
    super(String.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    return String.valueOf(o);
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof String)
    {
      setParameter("value", String.valueOf(o));
    }
  }
}
