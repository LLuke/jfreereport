/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
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
}
