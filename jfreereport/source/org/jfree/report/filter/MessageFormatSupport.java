/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * MessageFormatSupport.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MessageFormatSupport.java,v 1.9 2005/12/30 11:05:55 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.filter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.jfree.report.DataRow;
import org.jfree.report.util.CSVTokenizer;
import org.jfree.report.util.PropertyLookupParser;

public class MessageFormatSupport implements Serializable, Cloneable
{
  protected static class MessageCompiler extends PropertyLookupParser
  {
    private ArrayList fields;
    private ArrayList completeFormatString;

    public MessageCompiler()
    {
      this.fields = new ArrayList();
      this.completeFormatString = new ArrayList();
      setMarkerChar('$');
      setOpeningBraceChar('(');
      setClosingBraceChar(')');
    }

    protected String lookupVariable(final String name)
    {
      final CSVTokenizer tokenizer = new CSVTokenizer(name, ",", "\"");
      if (tokenizer.hasMoreTokens() == false)
      {
        return null;
      }
      final String varName = tokenizer.nextToken();
/*    // we have to collect every occurence, even if it is included twice
      // to allow the null-value-processing later ..
      final int index = fields.indexOf(varName);
      if (index != -1)
      {
        return (String) completeFormatString.get(index);
      }
*/
      final StringBuffer b = new StringBuffer();
      b.append("{");
      b.append(String.valueOf(fields.size()));
      while (tokenizer.hasMoreTokens())
      {
        b.append(",");
        b.append(tokenizer.nextToken());
      }
      b.append("}");
      final String formatString = b.toString();
      completeFormatString.add(formatString);
      fields.add(varName);
      return formatString;
    }

    public String[] getFields()
    {
      return (String[]) fields.toArray(new String[fields.size()]);
    }
  }

  private String[] fields;
  private MessageFormat format;
  private String formatString;
  private String compiledFormat;
  private String nullString;

  public MessageFormatSupport()
  {
  }

  public String getFormatString()
  {
    return formatString;
  }

  public void setFormatString(final String formatString)
  {
    final MessageCompiler compiler = new MessageCompiler();
    if (formatString == null)
    {
      throw new NullPointerException("Format must not be null");
    }
    compiledFormat = compiler.translateAndLookup(formatString);
    fields = compiler.getFields();
    format = new MessageFormat(compiledFormat);
    this.formatString = formatString;
  }

  public String performFormat(final DataRow dataRow)
  {
    if (fields == null || format == null)
    {
      return null;
    }

    boolean fastProcessingPossible;
    fastProcessingPossible = (nullString == null);

    final Format[] formats = format.getFormats();
    boolean fastProcessing = true;
    final Object[] parameters = new Object[fields.length];
    final boolean[] replaced = new boolean[fields.length];
    for (int i = 0; i < parameters.length; i++)
    {
      final Object value = dataRow.get(fields[i]);
      final Format currentFormat = formats[i];
      if (value == null)
      {
        parameters[i] = nullString;
        replaced[i] = currentFormat != null;
        fastProcessing = (fastProcessing && fastProcessingPossible && replaced[i] == false);
      }
      else
      {
        if (currentFormat instanceof DateFormat)
        {
          if (value instanceof Date)
          {
            parameters[i] = value;
            replaced[i] = false;
          }
          else
          {
            parameters[i] = nullString;
            replaced[i] = true;
            fastProcessing = (fastProcessing && fastProcessingPossible && replaced[i] == false);
          }
        }
        else if (currentFormat instanceof NumberFormat)
        {
          if (value instanceof Number)
          {
            parameters[i] = value;
            replaced[i] = false;
          }
          else
          {
            parameters[i] = nullString;
            replaced[i] = true;
            fastProcessing = (fastProcessing && fastProcessingPossible && replaced[i] == false);
          }
        }
        else
        {
          parameters[i] = value;
          replaced[i] = false;
        }
      }
    }
    if (fastProcessing)
    {
      return format.format(parameters);
    }

    final MessageFormat effectiveFormat = (MessageFormat) format.clone();
    for (int i = 0; i < replaced.length; i++)
    {
      boolean b = replaced[i];
      if (b)
      {
        effectiveFormat.setFormat(i, null);
      }
    }
    return effectiveFormat.format(parameters);
  }

  public Locale getLocale()
  {
    return format.getLocale();
  }

  public String getCompiledFormat()
  {
    return compiledFormat;
  }

  public void setLocale(final Locale locale)
  {
    format.setLocale(locale);
    format.applyPattern(compiledFormat);
  }

  public String getNullString()
  {
    return nullString;
  }

  public void setNullString(String nullString)
  {
    this.nullString = nullString;
  }

  public Object clone()
          throws CloneNotSupportedException
  {
    MessageFormatSupport support = (MessageFormatSupport) super.clone();
    if (format != null)
    {
      support.format = (MessageFormat) format.clone();
    }
    return support;
  }
}
