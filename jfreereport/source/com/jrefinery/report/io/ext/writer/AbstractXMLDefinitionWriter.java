/**
 * Date: Jan 13, 2003
 * Time: 1:01:18 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.JFreeReport;

import java.io.Writer;
import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;

public abstract class AbstractXMLDefinitionWriter
{
  public static final boolean CLOSE = true;
  public static final boolean OPEN = false;

  private ReportWriter reportWriter;
  private static String lineSeparator;

  public static String getLineSeparator ()
  {
    if (lineSeparator == null)
    {
      lineSeparator = System.getProperty("line.separator", "\n");
    }
    return lineSeparator;
  }

  public AbstractXMLDefinitionWriter(ReportWriter reportWriter)
  {
    this.reportWriter = reportWriter;
  }

  public ReportWriter getReportWriter()
  {
    return reportWriter;
  }

  public JFreeReport getReport ()
  {
    return getReportWriter().getReport();
  }

  public void writeTag (Writer w, String name) throws IOException
  {
    w.write("<");
    w.write(name);
    w.write(">");
  }


  public void writeCloseTag(Writer w, String tag) throws IOException
  {
    w.write("</");
    w.write(tag);
    w.write(">");
  }

  public void writeTag (Writer w, String name, String attributeName, String attributeValue, boolean close)
    throws IOException
  {
    Properties attr = new Properties();
    attr.setProperty(attributeName, attributeValue);
    writeTag(w, name, attr, close);
  }

  public void writeTag (Writer w, String name, Properties attributes, boolean close)
    throws IOException
  {
    w.write("<");
    w.write(name);
    Enumeration keys = attributes.keys();
    while (keys.hasMoreElements())
    {
      String key = (String) keys.nextElement();
      String value = attributes.getProperty(key);
      w.write (" ");
      w.write (key);
      w.write ("=\"");
      w.write (normalize(value));
      w.write ("\"");
    }
    if (close) w.write("/");
    w.write(">");
  }

  public static String normalize(String s)
  {
    StringBuffer str = new StringBuffer();
    int len = (s != null) ? s.length() : 0;

    for (int i = 0; i < len; i++)
    {
      char ch = s.charAt(i);

      switch (ch)
      {
        case '<':
          {
            str.append("&lt;");
            break;
          }
        case '>':
          {
            str.append("&gt;");
            break;
          }
        case '&':
          {
            str.append("&amp;");
            break;
          }
        case '"':
          {
            str.append("&quot;");
            break;
          }
        case '\n':
          {
            if (i > 0)
            {
              char lastChar = str.charAt(str.length() - 1);

              if (lastChar != '\r')
              {
                str.append(getLineSeparator());
              }
              else
              {
                str.append('\n');
              }
            }
            else
            {
              str.append(getLineSeparator());
            }
            break;
          }
        default :
          {
            str.append(ch);
          }
      }
    }

    return (str.toString());
  }

  public abstract void write (Writer writer) throws IOException, ReportWriterException;
}
