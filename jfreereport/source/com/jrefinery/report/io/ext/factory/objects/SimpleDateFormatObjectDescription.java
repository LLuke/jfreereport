/**
 * Date: Jan 24, 2003
 * Time: 7:18:58 PM
 *
 * $Id: SimpleDateFormatObjectDescription.java,v 1.1 2003/01/25 02:50:56 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.text.SimpleDateFormat;

public class SimpleDateFormatObjectDescription extends BeanObjectDescription
{
  public SimpleDateFormatObjectDescription()
  {
    this(SimpleDateFormat.class);
  }

  public SimpleDateFormatObjectDescription(Class className)
  {
    this(className, true);
  }

  public SimpleDateFormatObjectDescription(Class className, boolean init)
  {
    super(className, init);
    setParameterDefinition("localizedPattern", String.class);
    setParameterDefinition("pattern", String.class);
  }

  public void setParameterFromObject(Object o)
      throws ObjectFactoryException
  {
    super.setParameterFromObject(o);
    SimpleDateFormat format = (SimpleDateFormat) o;
    setParameter("localizedPattern", format.toLocalizedPattern());
    setParameter("pattern", format.toPattern());
  }

  public Object createObject()
  {
    SimpleDateFormat format = (SimpleDateFormat) super.createObject();
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
