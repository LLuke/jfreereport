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

  public PrintVarTemplate(String bean, String var, boolean quoted)
  {
    this.var = var;
    this.bean = bean;
    this.quoted = quoted;
  }

  public void print(PrintWriter writer, Context context) throws TemplateException
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

  private String getVariableValue (Context context) throws ParseException
  {
    try
    {
      if (var == null)
      {
        return ConvertUtils.convert(context.get(bean));
      }
      
      String value = BeanUtils.getProperty(context.get(bean), var);
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

  public String getQuotedJava (String text)
  {
    if (text == null)
    {
      return "null";
    }
    else
    {
      StringBuffer b = new StringBuffer();
      char[] textChars = text.toCharArray();
      b.append('\"');
      for (int i = 0; i< textChars.length; i++)
      {
        char textChar = textChars[i];
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
