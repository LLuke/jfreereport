/**
 * Date: Jan 24, 2003
 * Time: 7:13:03 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.text.DecimalFormat;

public class DecimalFormatObjectDescription extends BeanObjectDescription
{
  public DecimalFormatObjectDescription()
  {
    this(DecimalFormat.class);
  }

  public DecimalFormatObjectDescription(Class className)
  {
    this(className, true);
  }

  public DecimalFormatObjectDescription(Class className, boolean init)
  {
    super(className, init);
    setParameterDefinition("localizedPattern", String.class);
    setParameterDefinition("pattern", String.class);
  }

  public void setParameterFromObject(Object o)
      throws ObjectFactoryException
  {
    super.setParameterFromObject(o);
    DecimalFormat format = (DecimalFormat) o;
    setParameter("localizedPattern", format.toLocalizedPattern());
    setParameter("pattern", format.toPattern());
  }

  public Object createObject()
  {
    DecimalFormat format = (DecimalFormat) super.createObject();
    if (getParameter("pattern") != null)
    {
      format.applyPattern((String) getParameter("pattern"));
    }
    if (getParameter("localizedPattern") != null)
    {
      format.applyLocalizedPattern((String) getParameter("localizedPattern"));
    }
    return format;
  }
}
