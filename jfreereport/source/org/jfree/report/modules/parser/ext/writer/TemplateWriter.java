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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 17.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;

import org.jfree.report.modules.parser.ext.ElementHandler;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.report.util.Log;
import org.jfree.util.ObjectUtils;

public class TemplateWriter extends ObjectWriter
{
  private TemplateDescription template;
  private TemplateDescription parent;

  public TemplateWriter(final ReportWriter reportWriter, final int indentLevel,
                        final TemplateDescription template, final TemplateDescription parent)
  {
    super(reportWriter, template, indentLevel);
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
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if the report serialisation failed.
   */
  public void write(Writer writer) throws IOException, ReportWriterException
  {
    final Properties p = new Properties();
    if (template.getName() != null)
    {
      p.setProperty("name", template.getName());
    }
    p.setProperty("references", parent.getName());

    boolean tagWritten = false;

    Iterator it = template.getParameterNames();
    while (it.hasNext())
    {
      String name = (String) it.next();
      if (shouldWriteParameter(name))
      {
        if (tagWritten == false)
        {
          writeTag(writer, ElementHandler.TEMPLATE_TAG, p, OPEN);
          tagWritten = true;
        }
        writeParameter(writer, name);
      }
    }
    if (tagWritten)
    {
      writeCloseTag(writer, ElementHandler.TEMPLATE_TAG);
    }
    else
    {
      writeTag(writer, ElementHandler.TEMPLATE_TAG, p, CLOSE);
    }
  }

  private boolean shouldWriteParameter (String parameterName)
  {
    Object parameterObject = template.getParameter(parameterName);
    if (parameterObject == null)
    {
      Log.debug ("Should not write: Parameter is null.");
      return false;
    }
    Object parentObject = parent.getParameter(parameterName);
    if (ObjectUtils.equalOrBothNull(parameterObject, parentObject))
    {
      Log.debug ("Should not write: Parameter objects are equal.");
      return false;
    }
    return true;
  }
}
