/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * --------------------------------
 * AbstractXMLDefinitionWriter.java
 * --------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractXMLDefinitionWriter.java,v 1.10 2003/05/30 16:57:51 taqua Exp $
 *
 * Changes
 * -------
 * 20-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;

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
import com.jrefinery.report.io.ext.PropertyHandler;
import com.jrefinery.report.io.ext.ReportConfigHandler;
import com.jrefinery.report.io.ext.ReportDescriptionHandler;
import com.jrefinery.report.io.ext.StyleSheetHandler;
import com.jrefinery.report.io.ext.StylesHandler;
import com.jrefinery.report.io.ext.TemplatesHandler;

/**
 * A base class for writer classes for the JFreeReport XML report files.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractXMLDefinitionWriter
{
  /** A constant for close. */
  public static final boolean CLOSE = true;

  /** A constant for open. */
  public static final boolean OPEN = false;

  /** A report writer. */
  private ReportWriter reportWriter;

  /** The line separator. */
  private static String lineSeparator;

  /** A list of safe tags. */
  private static SafeTagList safeTags;

  /** The indent level for that writer. */
  private int indentLevel;

  /**
   * Returns the tags that can safely extend over several lines in the XML definition.
   *
   * @return The safe tags.
   */
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
      safeTags.add(CompoundObjectHandler.BASIC_OBJECT_TAG, false, true);

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
      safeTags.add(StyleSheetHandler.BASIC_KEY_TAG, false, true);
      safeTags.add(StyleSheetHandler.EXTENDS_TAG);

      safeTags.add(TemplatesHandler.TEMPLATE_TAG);
      safeTags.add(PropertyHandler.PROPERTY_TAG, false, true);
    }
    return safeTags;
  }

  /**
   * Returns the line separator.
   *
   * @return The line separator.
   */
  public static String getLineSeparator()
  {
    if (lineSeparator == null)
    {
      lineSeparator = System.getProperty("line.separator", "\n");
    }
    return lineSeparator;
  }

  /**
   * Creates a new writer.
   *
   * @param reportWriter  the report writer.
   */
  public AbstractXMLDefinitionWriter(ReportWriter reportWriter, int indentLevel)
  {
    this.reportWriter = reportWriter;
    this.indentLevel = indentLevel;
  }

  /**
   * Returns the report writer.
   *
   * @return The report writer.
   */
  protected ReportWriter getReportWriter()
  {
    return reportWriter;
  }

  /**
   * Returns the report.
   *
   * @return The report.
   */
  protected JFreeReport getReport()
  {
    return getReportWriter().getReport();
  }

  /**
   * Writes an opening XML tag that has no attributes.
   *
   * @param w  the writer.
   * @param name  the tag name.
   *
   * @throws IOException if there is an I/O problem.
   */
  protected void writeTag(Writer w, String name) throws IOException
  {
    indent(w, OPEN_TAG_INCREASE);

    w.write("<");
    w.write(name);
    w.write(">");
    if (getSafeTags().isSafeForOpen(name))
    {
      w.write(getLineSeparator());
    }
  }

  /**
   * Writes a closing XML tag.
   *
   * @param w  the writer.
   * @param tag  the tag name.
   *
   * @throws IOException if there is an I/O problem.
   */
  protected void writeCloseTag(Writer w, String tag) throws IOException
  {
    // check whether the tag contains CData - we ma not indent such tags
    if (getSafeTags().isSafeForOpen(tag))
    {
      indent(w, CLOSE_TAG_DECREASE);
    }
    else
    {
      decreaseIndent();
    }
    w.write("</");
    w.write(tag);
    w.write(">");
    if (getSafeTags().isSafeForClose(tag))
    {
      w.write(getLineSeparator());
    }
  }

  /**
   * Writes an opening XML tag with an attribute/value pair.
   *
   * @param w  the writer.
   * @param name  the tag name.
   * @param attributeName  the attribute name.
   * @param attributeValue  the attribute value.
   * @param close  controls whether the tag is closed.
   *
   * @throws IOException if there is an I/O problem.
   */
  protected void writeTag(Writer w, String name, String attributeName, String attributeValue,
                       boolean close) throws IOException
  {
    Properties attr = new Properties();
    attr.setProperty(attributeName, attributeValue);
    writeTag(w, name, attr, close);
  }

  /**
   * Writes an opening XML tag along with a list of attribute/value pairs.
   *
   * @param w  the writer.
   * @param name  the tag name.
   * @param attributes  the attributes.
   * @param close  controls whether the tag is closed.
   *
   * @throws IOException if there is an I/O problem.
   */
  protected void writeTag(Writer w, String name, Properties attributes, boolean close)
      throws IOException
  {
    indent(w, OPEN_TAG_INCREASE);

    w.write("<");
    w.write(name);
    Enumeration keys = attributes.keys();
    while (keys.hasMoreElements())
    {
      String key = (String) keys.nextElement();
      String value = attributes.getProperty(key);
      w.write(" ");
      w.write(key);
      w.write("=\"");
      w.write(normalize(value));
      w.write("\"");
    }
    if (close)
    {
      w.write("/>");
      if (getSafeTags().isSafeForClose(name))
      {
        w.write(getLineSeparator());
      }
      decreaseIndent();
    }
    else
    {
      w.write(">");
      if (getSafeTags().isSafeForOpen(name))
      {
        w.write(getLineSeparator());
      }
    }
  }

  /**
   * Normalises a string, replacing certain characters with their escape sequences so that
   * the XML text is not corrupted.
   *
   * @param s  the string.
   *
   * @return The normalised string.
   */
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

  protected static final int OPEN_TAG_INCREASE = 1;
  protected static final int CLOSE_TAG_DECREASE = 2;
  protected static final int INDENT_ONLY = 3;

  /**
   * Indent the line. Called for proper indenting in various places
   */
  protected void indent(Writer writer, int increase) throws IOException
  {
    if (increase == CLOSE_TAG_DECREASE)
    {
      decreaseIndent();
    }
    for (int i = 0; i < indentLevel; i++)
    {
      writer.write("    "); // 4 spaces, we could also try tab, but I do not know whether this works with our XML edit pane
    }
    if (increase == OPEN_TAG_INCREASE)
    {
      increaseIndent();
    }
  }

  protected int getIndentLevel()
  {
    return indentLevel;
  }

  protected void increaseIndent()
  {
    indentLevel++;
  }

  protected void decreaseIndent()
  {
    indentLevel--;
  }


  /**
   * Writes the report definition portion. Every DefinitionWriter handles one
   * or more elements of the JFreeReport object tree, DefinitionWriter traverse
   * the object tree and write the known objects or forward objects to other
   * definition writers.
   *
   * @param writer  the writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if the report serialisation failed.
   */
  public abstract void write(Writer writer) throws IOException, ReportWriterException;

}
