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
 * $Id: MessageFormatSupport.java,v 1.3 2005/03/03 22:59:59 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.filter;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

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

  public Locale getLocale ()
  {
    return format.getLocale();
  }

  public void setLocale (final Locale locale)
  {
    format.setLocale(locale);
  }
}
