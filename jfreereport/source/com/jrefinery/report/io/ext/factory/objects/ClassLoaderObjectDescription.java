/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: ClassLoaderObjectDescription.java,v 1.3 2003/02/02 23:43:49 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;



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
      return getClass().getClassLoader().loadClass(o).newInstance();
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
