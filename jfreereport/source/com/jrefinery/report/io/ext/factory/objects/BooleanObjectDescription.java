/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

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
}
