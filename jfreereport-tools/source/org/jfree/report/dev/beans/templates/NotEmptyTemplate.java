package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;
import java.lang.reflect.Array;

import org.apache.commons.beanutils.PropertyUtils;

public class NotEmptyTemplate extends CompoundTemplate
{
  private String var;
  private String bean;

  public NotEmptyTemplate(String bean, String var)
  {
    this.bean = bean;
    this.var = var;
  }

  public void print(PrintWriter writer, Context context) throws TemplateException
  {
    Object value = getObjectValue(context);
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

  private Object getObjectValue (Context context) throws TemplateException
  {
    try
    {
      Object value = PropertyUtils.getProperty(context.get(bean), var);
      return value;
    }
    catch (Exception e)
    {
      throw new TemplateException("Illegal bean operation", e);
    }
  }


}
