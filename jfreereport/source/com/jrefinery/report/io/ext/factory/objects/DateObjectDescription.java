/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: DateObjectDescription.java,v 1.2 2003/01/13 19:00:53 taqua Exp $
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

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Date)
    {
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTime((Date) o);
      int year = gc.get(GregorianCalendar.YEAR);
      int month = gc.get(GregorianCalendar.MONTH);
      int day = gc.get(GregorianCalendar.DAY_OF_MONTH);

      setParameter("year", new Integer(year));
      setParameter("month", new Integer(month));
      setParameter("day", new Integer(day));
    }
    else
    {
      throw new ObjectFactoryException("Is no instance of date");
    }

  }
}
