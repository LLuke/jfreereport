package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

public class ForEachTemplate extends CompoundTemplate
{
  private String var;
  private String bean;
  private String asVar;

  public ForEachTemplate(String bean, String var, String asVar)
  {
    this.bean = bean;
    this.var = var;
    this.asVar = asVar;
  }

  public void print(PrintWriter writer, Context context) throws TemplateException
  {
    Object value = getObjectValue(context);
    if (value == null)
    {
      return;
    }
    if (value.getClass().isArray())
    {
      final int length = Array.getLength(value);
      if (length == 0)
      {
        return;
      }
      for (int i = 0; i < length; i++)
      {
        Context ctx = context.create();
        final Object arrVal = Array.get(value, i);
        ctx.put(asVar, arrVal);
        super.print(writer, ctx);
      }
    }
    else if (value instanceof Collection)
    {
      Collection c = (Collection) value;
      Iterator it = c.iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        Context ctx = context.create();
        ctx.put(asVar, o);
        super.print(writer, ctx);
      }
    }

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
