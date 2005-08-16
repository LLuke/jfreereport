package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;
import java.lang.reflect.Array;

import org.apache.commons.beanutils.PropertyUtils;

public class NotEmptyTemplate extends CompoundTemplate
{
  private String var;
  private String bean;

  public NotEmptyTemplate(final String bean, final String var)
  {
    this.bean = bean;
    this.var = var;
  }

  public void print(final PrintWriter writer, final Context context) throws TemplateException
  {
    final Object value = getObjectValue(context);
    if (value == null)
    {
      return;
    }
    if (value.getClass().isArray() &&
        Array.getLength(value) == 0)
    {
      return;
    }

    super.print(writer, context);
  }

  private Object getObjectValue (final Context context) throws TemplateException
  {
    try
    {
      final Object value = PropertyUtils.getProperty(context.get(bean), var);
      return value;
    }
    catch (Exception e)
    {
      throw new TemplateException("Illegal bean operation", e);
    }
  }


}
