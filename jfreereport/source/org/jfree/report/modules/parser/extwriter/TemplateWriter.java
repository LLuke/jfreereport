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
 * ------------------------------
 * TemplateWriter.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TemplateWriter.java,v 1.2 2003/08/18 18:28:02 taqua Exp $
 *
 * Changes
 * -------------------------
 * 17-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;

import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.ext.ElementHandler;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.util.ObjectUtils;

/**
 * The template writer writes a single template definition to the xml-definition
 * stream. This writer requires report builder hints to be present for all 
 * templates. 
 * 
 * @author Thomas Morgner
 */
public class TemplateWriter extends ObjectWriter
{
  /** The template that should be written. */
  private TemplateDescription template;
  /** The parent of the current template. */
  private TemplateDescription parent;

  /**
   * Creates a new template writer.
   * 
   * @param reportWriter the report writer that is used to coordinate the writing.
   * @param indentLevel the current indention level.
   * @param template the template that should be written.
   * @param parent the parent of the template.
   * @param path the comment hint path used to resolve xml-comments from the 
   * parser.
   */
  public TemplateWriter(final ReportWriter reportWriter, final int indentLevel,
                        final TemplateDescription template, final TemplateDescription parent,
                        final CommentHintPath path)
  {
    super(reportWriter, template, indentLevel, path);
    if (template == null)
    {
      throw new NullPointerException("Template is null.");
    }
    if (parent == null)
    {
      throw new NullPointerException("Parent is null.");
    }
    this.parent = parent;
    this.template = template;
  }

  /**
   * Writes the report definition portion. Every DefinitionWriter handles one
   * or more elements of the JFreeReport object tree, DefinitionWriter traverse
   * the object tree and write the known objects or forward objects to other
   * definition writers.
   *
   * @param writer  the writer.
   *
   * @throws java.io.IOException if there is an I/O problem.
   * @throws ReportWriterException if the report serialisation failed.
   */
  public void write(Writer writer) throws IOException, ReportWriterException
  {
    final Properties p = new Properties();
    p.setProperty("references", parent.getName());
    if (template.getName() != null)
    {
      // dont copy the parent name for anonymous templates ...
      if (template.getName().equals(parent.getName()) == false)
      {
        p.setProperty("name", template.getName());
      }
    }

    boolean tagWritten = false;

    Iterator it = template.getParameterNames();
    while (it.hasNext())
    {
      String name = (String) it.next();
      if (shouldWriteParameter(name))
      {
        if (tagWritten == false)
        {
          writeComment(writer, getCommentHintPath(), CommentHandler.OPEN_TAG_COMMENT);
          writeTag(writer, ElementHandler.TEMPLATE_TAG, p, OPEN);
          tagWritten = true;
        }
        writeParameter(writer, name);
      }
    }
    if (tagWritten)
    {
      writeComment(writer, getCommentHintPath(), CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(writer, ElementHandler.TEMPLATE_TAG);
    }
    else
    {
      writeComment(writer, getCommentHintPath(), CommentHandler.OPEN_TAG_COMMENT);
      writeTag(writer, ElementHandler.TEMPLATE_TAG, p, CLOSE);
    }
  }

  /**
   * Tests, whether the given parameter should be written in this template.
   * This will return false, if the parameter is not set, or the parent
   * contains the same value.
   * 
   * @param parameterName the name of the parameter that should be tested
   * @return true, if the parameter should be written, false otherwise.
   */
  private boolean shouldWriteParameter (String parameterName)
  {
    Object parameterObject = template.getParameter(parameterName);
    if (parameterObject == null)
    {
      //Log.debug ("Should not write: Parameter is null.");
      return false;
    }
    Object parentObject = parent.getParameter(parameterName);
    if (ObjectUtils.equalOrBothNull(parameterObject, parentObject))
    {
      //Log.debug ("Should not write: Parameter objects are equal.");
      return false;
    }
    return true;
  }
}
