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

  public ForEachTemplate(final String bean, final String var, final String asVar)
  {
    this.bean = bean;
    this.var = var;
    this.asVar = asVar;
  }

  public void print(final PrintWriter writer, final Context context) throws TemplateException
  {
    final Object value = getObjectValue(context);
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
        final Context ctx = context.create();
        final Object arrVal = Array.get(value, i);
        ctx.put(asVar, arrVal);
        super.print(writer, ctx);
      }
    }
    else if (value instanceof Collection)
    {
      final Collection c = (Collection) value;
      final Iterator it = c.iterator();
      while (it.hasNext())
      {
        final Object o = it.next();
        final Context ctx = context.create();
        ctx.put(asVar, o);
        super.print(writer, ctx);
      }
    }

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
