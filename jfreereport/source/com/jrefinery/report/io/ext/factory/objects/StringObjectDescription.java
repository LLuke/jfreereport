/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

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
}
