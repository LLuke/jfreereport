/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: ClassLoaderObjectDescription.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

public class ClassLoaderObjectDescription extends AbstractObjectDescription
{
  public ClassLoaderObjectDescription()
  {
    super(Object.class);
    setParameterDefinition("class", String.class);
  }

  public Object createObject()
  {
    try
    {
      String o = (String) getParameter("class");
      return Class.forName(o).newInstance();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    setParameter("value", o.getClass().getName());
  }
}
