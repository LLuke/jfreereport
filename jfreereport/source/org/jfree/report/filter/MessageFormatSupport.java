package org.jfree.report.filter;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;

import org.jfree.report.DataRow;
import org.jfree.report.util.PropertyLookupParser;

public class MessageFormatSupport implements Serializable
{
  private static class MessageCompiler extends PropertyLookupParser
  {
    private ArrayList fields;

    public MessageCompiler ()
    {
      this.fields = new ArrayList();
      setMarkerChar('$');
      setOpeningBraceChar('(');
      setClosingBraceChar(')');
    }

    protected String lookupVariable (final String entity)
    {
      return (String) performInitialLookup(entity);
    }

    protected Object performInitialLookup (final String name)
    {
      final int index = fields.indexOf(name);
      if (index != -1)
      {
        return String.valueOf(index);
      }
      fields.add(name);
      return String.valueOf(fields.size() - 1);
    }

    public String[] getFields ()
    {
      return (String[]) fields.toArray(new String[fields.size()]);
    }
  }

  private String[] fields;
  private MessageFormat format;
  private String formatString;

  public MessageFormatSupport ()
  {
  }

  public String getFormatString ()
  {
    return formatString;
  }

  public void setFormatString (final String formatString)
  {
    final MessageCompiler compiler = new MessageCompiler();
    if (formatString == null)
    {
      throw new NullPointerException("Format must not be null");
    }
    final String pattern = compiler.translateAndLookup(formatString);
    format = new MessageFormat(pattern);
    fields = compiler.getFields();
    this.formatString = formatString;
  }

  public String performFormat (final DataRow dataRow)
  {
    if (fields == null || format == null)
    {
      return null;
    }

    final Object[] parameters = new Object[fields.length];
    for (int i = 0; i < parameters.length; i++)
    {
      parameters[i] = dataRow.get(fields[i]);
    }
    return format.format(parameters);
  }

  public static void main (final String[] args)
  {
    final MessageFormatSupport supper = new MessageFormatSupport();
    supper.setFormatString("Invoice for your order from {$(report.date), date,EEE, MMM d, yyyy}");

  }
}
