/**
 * Date: Jan 22, 2003
 * Time: 7:24:06 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.ElementAlignment;

public class AlignmentObjectDescription extends AbstractObjectDescription
{
  public AlignmentObjectDescription()
  {
    super(ElementAlignment.class);
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    if (o == null)
      return null;
    if (o.equalsIgnoreCase("left"))
    {
      return ElementAlignment.LEFT;
    }
    if (o.equalsIgnoreCase("right"))
    {
      return ElementAlignment.RIGHT;
    }
    if (o.equalsIgnoreCase("center"))
    {
      return ElementAlignment.CENTER;
    }
    if (o.equalsIgnoreCase("top"))
    {
      return ElementAlignment.TOP;
    }
    if (o.equalsIgnoreCase("middle"))
    {
      return ElementAlignment.MIDDLE;
    }
    if (o.equalsIgnoreCase("bottom"))
    {
      return ElementAlignment.BOTTOM;
    }
    return null;
  }

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o.equals(ElementAlignment.BOTTOM))
    {
      setParameter("value", "bottom");
    }
    else if (o.equals(ElementAlignment.MIDDLE))
    {
      setParameter("value", "middle");
    }
    else if (o.equals(ElementAlignment.TOP))
    {
      setParameter("value", "top");
    }
    else if (o.equals(ElementAlignment.CENTER))
    {
      setParameter("value", "center");
    }
    else if (o.equals(ElementAlignment.RIGHT))
    {
      setParameter("value", "right");
    }
    else if (o.equals(ElementAlignment.LEFT))
    {
      setParameter("value", "left");
    }
    else
      throw new ObjectFactoryException("Invalid value specified for ElementAlignment");
  }
}
