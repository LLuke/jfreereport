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
 * --------------------
 * TemplatesWriter.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TemplatesWriter.java,v 1.10 2005/02/04 19:08:53 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.jfree.report.ReportBuilderHints;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.report.util.Log;
import org.jfree.xml.CommentHandler;

/**
 * The templates writer is responsible to write the templates section.
 *
 * @author Thomas Morgner
 */
public class TemplatesWriter extends AbstractXMLDefinitionWriter
{
  /** the standard templates comment hint path. */
  private static final CommentHintPath TEMPLATES_PATH = new CommentHintPath
      (new String[]{REPORT_DEFINITION_TAG, TEMPLATES_TAG});

  /**
   * Creates a new writer.
   *
   * @param reportWriter  the report writer.
   * @param indentLevel the current indention level.
   */
  public TemplatesWriter(final ReportWriter reportWriter, final int indentLevel)
  {
    super(reportWriter, indentLevel);
  }

  /**
   * Writes the templates (not yet supported).
   *
   * @param writer  the character stream writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(final Writer writer) throws IOException, ReportWriterException
  {

    writeComment(writer, TEMPLATES_PATH, CommentHandler.OPEN_TAG_COMMENT);

    final ReportBuilderHints hints = getReport().getReportBuilderHints();
    if (hints == null)
    {
      return;
    }
    final List l = (List) hints.getHint(getReport(), "ext.parser.template-definition", List.class);
    if (l == null)
    {
      return;
    }
    if (l.isEmpty())
    {
      return;
    }

    writeTag(writer, TEMPLATES_TAG);
    final ArrayList invalidTemplates = new ArrayList();

    final TemplateDescription[] td = (TemplateDescription[])
        l.toArray(new TemplateDescription[l.size()]);

    for (int i = 0; i < td.length; i++)
    {
      final TemplateDescription template = td[i];
      template.configure(getReportWriter().getConfiguration());

      final String templateExtends = (String)
          hints.getHint(template, "ext.parser.template-reference", String.class);
      if (templateExtends == null)
      {
        // should not happen with a sane parser here ...
        Log.warn("Invalid parser hint: Template reference missing for template " +
            template.getName());
        invalidTemplates.add(template.getName());
        continue;
      }
      if (invalidTemplates.contains(templateExtends))
      {
        Log.warn("Invalid parser hint: Template reference points to invalid template " +
            template.getName());
        invalidTemplates.add(template.getName());
        continue;
      }
      final TemplateDescription parentTemplate = TemplatesWriter.getTemplateDescription
          (getReportWriter(), templateExtends);
      if (parentTemplate == null)
      {
        Log.warn("Invalid parser hint: Template reference invalid for template " +
            template.getName());
        invalidTemplates.add(template.getName());
        continue;
      }
      //Log.debug ("Searching template: " + templateExtends);

      final CommentHintPath templatePath = TEMPLATES_PATH.getInstance();
      templatePath.addName(template);
      final TemplateWriter templateWriter = new TemplateWriter
          (getReportWriter(), getIndentLevel(), template, parentTemplate, templatePath);
      templateWriter.write(writer);
    }

    writeComment(writer, TEMPLATES_PATH, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, TEMPLATES_TAG);
    writer.write(getLineSeparator());
  }

  /**
   * Searches the template description that has the given name using
   * the factories defined in the report writer.
   *
   * @param writer the report writer
   * @param name the template description name, never null
   * @return the template description or null, if there is no such
   * description.
   * @throws NullPointerException if the name is null.
   */
  public static TemplateDescription getTemplateDescription
      (final ReportWriter writer, final String name)
  {
    // search by name in the parser hints ...
    if (name == null)
    {
      throw new NullPointerException("Name must be specified.");
    }
    final ReportBuilderHints hints = writer.getReport().getReportBuilderHints();
    if (hints == null)
    {
      return null;
    }
    final List l = (List) hints.getHint
      (writer.getReport(), "ext.parser.template-definition", List.class);
    if (l == null)
    {
      return null;
    }
    if (l.isEmpty())
    {
      return null;
    }

    final TemplateDescription[] td = 
      (TemplateDescription[]) l.toArray(new TemplateDescription[l.size()]);
    for (int i = 0; i < td.length; i++)
    {
      if (td[i].getName().equals(name))
      {
        td[i].configure(writer.getConfiguration());
        return td[i];
      }
    }

    // if searching did not find anything or if no name was specified,
    // then check the global (predefined) templates
    return writer.getTemplateCollector().getTemplate(name);
  }
}
