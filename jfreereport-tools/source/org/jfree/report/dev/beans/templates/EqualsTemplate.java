package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;

import org.apache.commons.beanutils.BeanUtils;

public class EqualsTemplate extends CompoundTemplate
{
  private String var;
  private String bean;
  private String value;

  public EqualsTemplate(final String bean, final String var, final String value)
  {
    this.bean = bean;
    this.var = var;
    this.value = value;
  }

  public void print(final PrintWriter writer, final Context context) throws TemplateException
  {
    final String value = getStringValue(context);
    if (value == null)
    {
      return;
    }
    if (this.value.equals(value) == false)
    {
      return;
    }

    super.print(writer, context);
  }

  private String getStringValue (final Context context) throws TemplateException
  {
    try
    {
      final String value = BeanUtils.getProperty(context.get(bean), var);
      return value;
    }
    catch (Exception e)
    {
      throw new TemplateException("Illegal bean operation", e);
    }
  }


}
