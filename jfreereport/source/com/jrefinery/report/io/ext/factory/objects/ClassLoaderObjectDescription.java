/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: ClassLoaderObjectDescription.java,v 1.2 2003/01/13 19:00:52 taqua Exp $
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
