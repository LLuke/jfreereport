/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

import java.awt.geom.Point2D;
import java.util.GregorianCalendar;
import java.util.Date;

public class DateObjectDescription extends AbstractObjectDescription
{
  public DateObjectDescription()
  {
    super(Date.class);
    setParameterDefinition("year", Integer.class);
    setParameterDefinition("month", Integer.class);
    setParameterDefinition("day", Integer.class);
  }

  public Object createObject()
  {
    int y = getIntParameter("year");
    int m = getIntParameter("month");
    int d = getIntParameter("day");

    return new GregorianCalendar(y,m,d).getTime();
  }

  private int getIntParameter (String param)
  {
    Integer p = (Integer) getParameter(param);
    if (p == null) return 0;
    return p.intValue();
  }
}
