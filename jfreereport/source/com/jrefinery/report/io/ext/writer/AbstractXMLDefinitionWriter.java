/**
 * Date: Jan 13, 2003
 * Time: 1:01:18 PM
 *
 * $Id: AbstractXMLDefinitionWriter.java,v 1.1 2003/01/13 21:39:00 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ext.BandHandler;
import com.jrefinery.report.io.ext.CompoundObjectHandler;
import com.jrefinery.report.io.ext.DataSourceHandler;
import com.jrefinery.report.io.ext.ExpressionHandler;
import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.FunctionsHandler;
import com.jrefinery.report.io.ext.GroupHandler;
import com.jrefinery.report.io.ext.GroupsHandler;
import com.jrefinery.report.io.ext.ParserConfigHandler;
import com.jrefinery.report.io.ext.ReportConfigHandler;
import com.jrefinery.report.io.ext.ReportDescriptionHandler;
import com.jrefinery.report.io.ext.StyleSheetHandler;
import com.jrefinery.report.io.ext.StylesHandler;
import com.jrefinery.report.io.ext.TemplatesHandler;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public abstract class AbstractXMLDefinitionWriter
{
  public static final boolean CLOSE = true;
  public static final boolean OPEN = false;

  private ReportWriter reportWriter;
  private static String lineSeparator;
  private static SafeTagList safeTags;

  public static SafeTagList getSafeTags()
  {
    if (safeTags == null)
    {
      safeTags = new SafeTagList();
      safeTags.add(ExtReportHandler.DATA_DEFINITION_TAG);
      safeTags.add(ExtReportHandler.FUNCTIONS_TAG);
      safeTags.add(ExtReportHandler.PARSER_CONFIG_TAG);
      safeTags.add(ExtReportHandler.REPORT_CONFIG_TAG);
      safeTags.add(ExtReportHandler.REPORT_DEFINITION_TAG);
      safeTags.add(ExtReportHandler.REPORT_DESCRIPTION_TAG);
      safeTags.add(ExtReportHandler.STYLES_TAG);
      safeTags.add(ExtReportHandler.TEMPLATES_TAG);

      safeTags.add(BandHandler.BAND_TAG);
      safeTags.add(BandHandler.DEFAULT_STYLE_TAG);
      safeTags.add(BandHandler.ELEMENT_TAG);

      safeTags.add(CompoundObjectHandler.COMPOUND_OBJECT_TAG);

      safeTags.add(DataSourceHandler.DATASOURCE_TAG);

      safeTags.add(ExpressionHandler.PROPERTIES_TAG);

      safeTags.add(FunctionsHandler.EXPRESSION_TAG);
      safeTags.add(FunctionsHandler.FUNCTION_TAG);
      safeTags.add(FunctionsHandler.PROPERTY_REF_TAG);

      safeTags.add(GroupHandler.FIELDS_TAG);
      safeTags.add(GroupHandler.GROUP_FOOTER_TAG);
      safeTags.add(GroupHandler.GROUP_HEADER_TAG);

      safeTags.add(GroupsHandler.GROUP_TAG);

      safeTags.add(ParserConfigHandler.DATADEFINITION_FACTORY_TAG);
      safeTags.add(ParserConfigHandler.DATASOURCE_FACTORY_TAG);
      safeTags.add(ParserConfigHandler.ELEMENT_FACTORY_TAG);
      safeTags.add(ParserConfigHandler.OBJECT_FACTORY_TAG);
      safeTags.add(ParserConfigHandler.STYLEKEY_FACTORY_TAG);
      safeTags.add(ParserConfigHandler.TEMPLATE_FACTORY_TAG);

      safeTags.add(ReportConfigHandler.CONFIGURATION_TAG);
      safeTags.add(ReportConfigHandler.DEFAULT_PAGEFORMAT_TAG);
      safeTags.add(ReportConfigHandler.OUTPUT_TARGET_TAG);

      safeTags.add(ReportDescriptionHandler.GROUPS_TAG);
      safeTags.add(ReportDescriptionHandler.ITEMBAND_TAG);
      safeTags.add(ReportDescriptionHandler.PAGE_FOOTER_TAG);
      safeTags.add(ReportDescriptionHandler.PAGE_HEADER_TAG);
      safeTags.add(ReportDescriptionHandler.REPORT_FOOTER_TAG);
      safeTags.add(ReportDescriptionHandler.REPORT_HEADER_TAG);

      safeTags.add(StylesHandler.STYLE_TAG);

      safeTags.add(StyleSheetHandler.COMPOUND_KEY_TAG);
      safeTags.add(StyleSheetHandler.EXTENDS_TAG);

      safeTags.add(TemplatesHandler.TEMPLATE_TAG);
    }
    return safeTags;
  }

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
    if (getSafeTags().isSafeForOpen(name))
    {
      w.write(getLineSeparator());
    }
  }


  public void writeCloseTag(Writer w, String tag) throws IOException
  {
    w.write("</");
    w.write(tag);
    w.write(">");
    if (getSafeTags().isSafeForClose(tag))
    {
      w.write(getLineSeparator());
    }
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
    if (getSafeTags().isSafeForOpen(name) || (close && getSafeTags().isSafeForClose(name)))
    {
      w.write(getLineSeparator());
    }
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
