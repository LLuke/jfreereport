/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: AbstractXMLDefinitionWriter.java,v 1.15 2003/06/29 16:59:25 taqua Exp $
 *
 * Changes
 * -------
 * 20-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.ext.BandHandler;
import org.jfree.report.modules.parser.ext.CompoundObjectHandler;
import org.jfree.report.modules.parser.ext.DataSourceHandler;
import org.jfree.report.modules.parser.ext.ExpressionHandler;
import org.jfree.report.modules.parser.ext.ExtReportHandler;
import org.jfree.report.modules.parser.ext.FunctionsHandler;
import org.jfree.report.modules.parser.ext.GroupHandler;
import org.jfree.report.modules.parser.ext.GroupsHandler;
import org.jfree.report.modules.parser.ext.ParserConfigHandler;
import org.jfree.report.modules.parser.ext.PropertyHandler;
import org.jfree.report.modules.parser.ext.ReportConfigHandler;
import org.jfree.report.modules.parser.ext.ReportDescriptionHandler;
import org.jfree.report.modules.parser.ext.StyleSheetHandler;
import org.jfree.report.modules.parser.ext.StylesHandler;
import org.jfree.report.modules.parser.ext.TemplatesHandler;

/**
 * A base class for writer classes for the JFreeReport XML report files.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractXMLDefinitionWriter
{
  /** A int constant for controling the indent function. */
  protected static final int OPEN_TAG_INCREASE = 1;
  /** A int constant for controling the indent function. */
  protected static final int CLOSE_TAG_DECREASE = 2;
  /** A int constant for controling the indent function. */
  protected static final int INDENT_ONLY = 3;

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
      safeTags.add(GroupHandler.FIELD_TAG, false, true);
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
   * @param indentLevel the current indention level.
   */
  public AbstractXMLDefinitionWriter(final ReportWriter reportWriter, final int indentLevel)
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
  protected void writeTag(final Writer w, final String name) throws IOException
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
  protected void writeCloseTag(final Writer w, final String tag) throws IOException
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
  protected void writeTag(final Writer w, final String name, final String attributeName, final String attributeValue,
                          final boolean close) throws IOException
  {
    final Properties attr = new Properties();
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
  protected void writeTag(final Writer w, final String name, final Properties attributes, final boolean close)
      throws IOException
  {
    indent(w, OPEN_TAG_INCREASE);

    w.write("<");
    w.write(name);
    final Enumeration keys = attributes.keys();
    while (keys.hasMoreElements())
    {
      final String key = (String) keys.nextElement();
      final String value = attributes.getProperty(key);
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
  public static String normalize(final String s)
  {
    final StringBuffer str = new StringBuffer();
    final int len = (s != null) ? s.length() : 0;

    for (int i = 0; i < len; i++)
    {
      final char ch = s.charAt(i);

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
              final char lastChar = str.charAt(str.length() - 1);

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

  /**
   * Indent the line. Called for proper indenting in various places.
   *
   * @param writer the writer which should receive the indentention.
   * @param increase the current indent level.
   * @throws IOException if writing the stream failed.
   */
  protected void indent(final Writer writer, final int increase) throws IOException
  {
    if (increase == CLOSE_TAG_DECREASE)
    {
      decreaseIndent();
    }
    for (int i = 0; i < indentLevel; i++)
    {
      writer.write("    "); // 4 spaces, we could also try tab,
      // but I do not know whether this works
      // with our XML edit pane
    }
    if (increase == OPEN_TAG_INCREASE)
    {
      increaseIndent();
    }
  }

  /**
   * Returns the current indent level.
   *
   * @return the current indent level.
   */
  protected int getIndentLevel()
  {
    return indentLevel;
  }

  /**
   * Increases the indention by one level.
   */
  protected void increaseIndent()
  {
    indentLevel++;
  }

  /**
   * Decreates the indention by one level.
   */
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
