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
 * --------------------
 * TemplatesWriter.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TemplatesWriter.java,v 1.3 2003/02/21 11:31:13 mungady Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import java.io.IOException;
import java.io.Writer;

import com.jrefinery.report.io.ext.ExtReportHandler;

/**
 * A templates writer.
 * 
 * @author Thomas Morgner
 */
public class TemplatesWriter extends AbstractXMLDefinitionWriter
{
  /**
   * Creates a new writer.
   * 
   * @param reportWriter  the report writer.
   */
  public TemplatesWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
  }

  /**
   * Writes the templates (not yet supported).
   * 
   * @param writer  the character stream writer.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(Writer writer) throws IOException, ReportWriterException
  {
    writer.write("<!-- templates are not supported by the writer. -->\n");
    writeTag(writer, ExtReportHandler.TEMPLATES_TAG);
    writeCloseTag(writer, ExtReportHandler.TEMPLATES_TAG);
  }
}
