/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: AbstractXMLDefinitionWriter.java,v 1.9 2005/02/04 19:08:53 taqua Exp $
 *
 * Changes
 * -------
 * 20-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportBuilderHints;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.xml.writer.SafeTagList;
import org.jfree.xml.writer.XMLWriterSupport;

/**
 * A base class for writer classes for the JFreeReport XML report files.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractXMLDefinitionWriter extends XMLWriterSupport
{
  /** the document element tag for the extended report format. */
  public static final String REPORT_DEFINITION_TAG = "report-definition";

  /** The parser config tag name. */
  public static final String PARSER_CONFIG_TAG = "parser-config";

  /** The report config tag name. */
  public static final String REPORT_CONFIG_TAG = "report-config";

  /** The styles tag name. */
  public static final String STYLES_TAG = "styles";

  /** The templates tag name. */
  public static final String TEMPLATES_TAG = "templates";

  /** The report description tag name. */
  public static final String REPORT_DESCRIPTION_TAG = "report-description";

  /** The functions tag name. */
  public static final String FUNCTIONS_TAG = "functions";

  /** The 'band' tag. */
  public static final String BAND_TAG = "band";

  /** The 'element' tag. */
  public static final String ELEMENT_TAG = "element";

  /** The text for the 'compound-object' tag. */
  public static final String COMPOUND_OBJECT_TAG = "compound-object";

  /** The text for the 'basic-object' tag. */
  public static final String BASIC_OBJECT_TAG = "basic-object";

  /** The datasource tag name. */
  public static final String DATASOURCE_TAG = "datasource";

  /** The properties tag name. */
  public static final String PROPERTIES_TAG = "properties";

  /** The name of the function tag. */
  public static final String FUNCTION_TAG = "function";

  /** The name of the expression tag. */
  public static final String EXPRESSION_TAG = "expression";

  /** The name of the 'property-ref' tag. */
  public static final String PROPERTY_REF_TAG = "property-ref";

  /** The 'fields' tag name. */
  public static final String FIELDS_TAG = "fields";

  /** The 'field' tag name. */
  public static final String FIELD_TAG = "field";

  /** The 'group-header' tag name. */
  public static final String GROUP_HEADER_TAG = "group-header";

  /** The 'group-footer' tag name. */
  public static final String GROUP_FOOTER_TAG = "group-footer";

  /** The 'group' tag name. */
  public static final String GROUP_TAG = "group";

  /** The 'stylekey-factory' tag name. */
  public static final String STYLEKEY_FACTORY_TAG = "stylekey-factory";

  /** The 'template-factory' tag name. */
  public static final String TEMPLATE_FACTORY_TAG = "template-factory";

  /** The 'object-factory' tag name. */
  public static final String OBJECT_FACTORY_TAG = "object-factory";

  /** The 'datadefinition-factory' tag name. */
  public static final String DATADEFINITION_FACTORY_TAG = "datadefinition-factory";

  /** The 'datasource-factory' tag name. */
  public static final String DATASOURCE_FACTORY_TAG = "datasource-factory";

  /** The 'element-factory' tag name. */
  public static final String ELEMENT_FACTORY_TAG = "element-factory";

  /** The class attribute name. */
  public static final String CLASS_ATTRIBUTE = "class";

  /** The 'default page format' tag name. */
  public static final String DEFAULT_PAGEFORMAT_TAG = "defaultpageformat";

  /** The 'configuration' tag name. */
  public static final String CONFIGURATION_TAG = "configuration";

  /** The 'output-config' tag name. */
  public static final String OUTPUT_TARGET_TAG = "output-config";

  /** Literal text for an XML attribute. */
  public static final String PAGEFORMAT_ATT = "pageformat";

  /** Literal text for an XML attribute. */
  public static final String LEFTMARGIN_ATT = "leftmargin";

  /** Literal text for an XML attribute. */
  public static final String RIGHTMARGIN_ATT = "rightmargin";

  /** Literal text for an XML attribute. */
  public static final String TOPMARGIN_ATT = "topmargin";

  /** Literal text for an XML attribute. */
  public static final String BOTTOMMARGIN_ATT = "bottommargin";

  /** Literal text for an XML attribute. */
  public static final String WIDTH_ATT = "width";

  /** Literal text for an XML attribute. */
  public static final String HEIGHT_ATT = "height";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_ATT = "orientation";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_PORTRAIT_VAL = "portrait";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_LANDSCAPE_VAL = "landscape";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_REVERSE_LANDSCAPE_VAL = "reverselandscape";

  /** A constant defining a style key factory parser hint. */
  public static final String STYLEKEY_FACTORY_HINT =
      "ext.parser.parser-config.stylekeyfactories";
  /** A constant defining an object factory parser hint. */
  public static final String OBJECT_FACTORY_HINT =
      "ext.parser.parser-config.objectfactories";
  /** A constant defining a datasource factory parser hint. */
  public static final String DATASOURCE_FACTORY_HINT =
      "ext.parser.parser-config.datasourcefactories";
  /** A constant defining a template factory parser hint. */
  public static final String TEMPLATE_FACTORY_HINT =
      "ext.parser.parser-config.templatefactories";
  /** A constant defining a element factory parser hint. */
  public static final String ELEMENT_FACTORY_HINT =
      "ext.parser.parser-config.elementfactories";
  /** The 'report-header' tag name. */
  public static final String REPORT_HEADER_TAG = "report-header";

  /** The 'report-footer' tag name. */
  public static final String REPORT_FOOTER_TAG = "report-footer";

  /** The 'page-header' tag name. */
  public static final String PAGE_HEADER_TAG = "page-header";

  /** The 'page-footer' tag name. */
  public static final String PAGE_FOOTER_TAG = "page-footer";

  /** The 'itemband' tag name. */
  public static final String ITEMBAND_TAG = "itemband";

  /** The 'groups' tag name. */
  public static final String GROUPS_TAG = "groups";

  public static final String WATERMARK_TAG = "watermark";
  /** The 'style' tag name. */
  public static final String STYLE_TAG = "style";

  /** The 'compound-key' tag name. */
  public static final String COMPOUND_KEY_TAG = "compound-key";

  /** The 'basic-key' tag name. */
  public static final String BASIC_KEY_TAG = "basic-key";

  /** The 'extends' tag name. */
  public static final String EXTENDS_TAG = "extends";

  /** The template tag. */
  public static final String TEMPLATE_TAG = "template";

  /** The 'property' tag name. */
  public static final String PROPERTY_TAG = "property";

  /** The 'name' attribute text. */
  public static final String NAME_ATTR = "name";


  /** A report writer. */
  private final ReportWriter reportWriter;

  /** A list of safe tags. */
  private static SafeTagList safeTags;

  /**
   * Returns the tags that can safely extend over several lines in the XML definition.
   *
   * @return The safe tags.
   */
  public static SafeTagList getDefaultSafeTags()
  {
    if (safeTags == null)
    {
      safeTags = new SafeTagList();
      safeTags.add(FUNCTIONS_TAG);
      safeTags.add(PARSER_CONFIG_TAG);
      safeTags.add(REPORT_CONFIG_TAG);
      safeTags.add(REPORT_DEFINITION_TAG);
      safeTags.add(REPORT_DESCRIPTION_TAG);
      safeTags.add(STYLES_TAG);
      safeTags.add(TEMPLATES_TAG);

      safeTags.add(BAND_TAG);
      safeTags.add(ELEMENT_TAG);

      safeTags.add(COMPOUND_OBJECT_TAG);
      safeTags.add(BASIC_OBJECT_TAG, false, true);

      safeTags.add(DATASOURCE_TAG);

      safeTags.add(PROPERTIES_TAG);

      safeTags.add(EXPRESSION_TAG);
      safeTags.add(FUNCTION_TAG);
      safeTags.add(PROPERTY_REF_TAG);

      safeTags.add(FIELDS_TAG);
      safeTags.add(FIELD_TAG, false, true);
      safeTags.add(GROUP_FOOTER_TAG);
      safeTags.add(GROUP_HEADER_TAG);

      safeTags.add(GROUP_TAG);

      safeTags.add(DATADEFINITION_FACTORY_TAG);
      safeTags.add(DATASOURCE_FACTORY_TAG);
      safeTags.add(ELEMENT_FACTORY_TAG);
      safeTags.add(OBJECT_FACTORY_TAG);
      safeTags.add(STYLEKEY_FACTORY_TAG);
      safeTags.add(TEMPLATE_FACTORY_TAG);

      safeTags.add(CONFIGURATION_TAG);
      safeTags.add(DEFAULT_PAGEFORMAT_TAG);
      safeTags.add(OUTPUT_TARGET_TAG);

      safeTags.add(GROUPS_TAG);
      safeTags.add(ITEMBAND_TAG);
      safeTags.add(PAGE_FOOTER_TAG);
      safeTags.add(PAGE_HEADER_TAG);
      safeTags.add(REPORT_FOOTER_TAG);
      safeTags.add(REPORT_HEADER_TAG);

      safeTags.add(STYLE_TAG);

      safeTags.add(COMPOUND_KEY_TAG);
      safeTags.add(BASIC_KEY_TAG, false, true);
      safeTags.add(EXTENDS_TAG);

      safeTags.add(TEMPLATE_TAG);
      safeTags.add(PROPERTY_TAG, false, true);
    }
    return safeTags;
  }

  /**
   * Creates a new writer.
   *
   * @param reportWriter  the report writer.
   * @param indentLevel the current indention level.
   */
  public AbstractXMLDefinitionWriter(final ReportWriter reportWriter, final int indentLevel)
  {
    super(getDefaultSafeTags(), indentLevel);
    this.reportWriter = reportWriter;
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
   * Normalises a string, replacing certain characters with their escape sequences so that
   * the XML text is not corrupted.
   *
   * @param s  the string.
   *
   * @return The normalised string.
   */
  public static String normalize(final String s)
  {
    if (s == null)
    {
      return "";
    }
    final StringBuffer str = new StringBuffer();
    final int len = s.length();

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
   * Writes the given comment. This method does nothing if the comment is null.
   *
   * @param writer the writer that should receive the content.
   * @param comment the xml comment that should be written.
   * @throws IOException if an error occurs.
   */
  protected void writeComment(final Writer writer, final String comment) throws IOException
  {
    if (comment == null)
    {
      return;
    }
    indent(writer, INDENT_ONLY);
    writer.write("<!--");
    writer.write(comment);
    writer.write("-->");
    writer.write(getLineSeparator());
  }

  /**
   * Reads a comment from the given comment hint path and hint name and Writes
   * that comment to the xml stream. This method does nothing if there is no
   * comment stored at that position.
   *
   * @param writer the writer that should receive the content
   * @param path the comment hint path that points to the comment
   * @param hintName the hint name used to store the comment
   * @throws IOException if an error occured.
   */
  protected void writeComment
      (final Writer writer, final CommentHintPath path, final String hintName)
      throws IOException
  {

    final ReportBuilderHints hints = getReport().getReportBuilderHints();
    if (hints == null)
    {
      return;
    }

    final String[] comment = (String[]) hints.getHint(path, hintName, String[].class);
    if (comment == null)
    {
      return;
    }
    for (int i = 0; i < comment.length; i++)
    {
      writeComment(writer, comment[i]);
    }
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
