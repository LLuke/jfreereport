package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;

import org.apache.commons.beanutils.BeanUtils;

public class EqualsTemplate extends CompoundTemplate
{
  private String var;
  private String bean;
  private String value;

  public EqualsTemplate(String bean, String var, String value)
  {
    this.bean = bean;
    this.var = var;
    this.value = value;
  }

  public void print(PrintWriter writer, Context context) throws TemplateException
  {
    String value = getStringValue(context);
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

  private String getStringValue (Context context) throws TemplateException
  {
    try
    {
      String value = BeanUtils.getProperty(context.get(bean), var);
      return value;
    }
    catch (Exception e)
    {
      throw new TemplateException("Illegal bean operation", e);
    }
  }


}
