package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;

import org.jfree.xml.ParseException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

public class PrintVarTemplate implements Template
{
  private String var;
  private String bean;
  private boolean quoted;

  public PrintVarTemplate(final String bean, final String var, final boolean quoted)
  {
    this.var = var;
    this.bean = bean;
    this.quoted = quoted;
  }

  public void print(final PrintWriter writer, final Context context) throws TemplateException
  {
    try
    {
      writer.print(getVariableValue(context));
    }
    catch (Exception e)
    {
      throw new TemplateException("Failed", e);
    }
  }

  private String getVariableValue (final Context context) throws ParseException
  {
    try
    {
      if (var == null)
      {
        return ConvertUtils.convert(context.get(bean));
      }
      
      final String value = BeanUtils.getProperty(context.get(bean), var);
      if (quoted)
      {
        return getQuotedJava(value);
      }
      return value;
    }
    catch (Exception e)
    {
      throw new ParseException("Illegal bean operation", e);
    }
  }

  public String getQuotedJava (final String text)
  {
    if (text == null)
    {
      return "null";
    }
    else
    {
      final StringBuffer b = new StringBuffer();
      final char[] textChars = text.toCharArray();
      b.append('\"');
      for (int i = 0; i< textChars.length; i++)
      {
        final char textChar = textChars[i];
        if (textChar == '\"' ||
            textChar == '\n' ||
            textChar == '\r' ||
            textChar == '\t' ||
            textChar == '\0')
        {
          b.append('\\');
        }
        b.append(textChar);
      }
      b.append('\"');
      return b.toString();
    }
  }

}
