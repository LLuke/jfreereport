/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------
 * HtmlProcessor.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlProcessor.java,v 1.8 2003/02/04 17:56:32 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.table.TableProcessor;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.NullOutputStream;

public class HtmlProcessor extends TableProcessor
{
  private HtmlFilesystem filesystem;
  private boolean useXHTML;
  private String encoding;

  public HtmlProcessor(JFreeReport report)
    throws ReportProcessingException, FunctionInitializeException
  {
    this (report, false);
  }
  public HtmlProcessor(JFreeReport report, boolean useXHTML, String encoding)
      throws ReportProcessingException, FunctionInitializeException
  {
    super(report);
    this.useXHTML = useXHTML;
    this.encoding = encoding;
  }

  public HtmlProcessor(JFreeReport report, boolean useXHTML) throws ReportProcessingException, FunctionInitializeException
  {
    this (report, useXHTML, System.getProperty("file.encoding", "UTF-8"));
  }

  public boolean isGenerateXHTML()
  {
    return useXHTML;
  }

  public void setGenerateXHTML(boolean useXHTML)
  {
    this.useXHTML = useXHTML;
  }

  public HtmlFilesystem getFilesystem()
  {
    return filesystem;
  }

  public void setFilesystem(HtmlFilesystem filesystem)
  {
    this.filesystem = filesystem;
  }

  public String getEncoding()
  {
    return encoding;
  }

  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
  }

  public TableProducer createProducer(boolean dummy)
  {
    HtmlProducer prod;
    if (dummy == true)
    {
      prod = new HtmlProducer(new StreamHtmlFilesystem(new NullOutputStream()),
                              getReport().getName(), isStrictLayout(), useXHTML, getEncoding());
      prod.setDummy(true);
    }
    else
    {
      prod = new HtmlProducer(getFilesystem(), getReport().getName(),
                              isStrictLayout(), useXHTML, getEncoding());
      prod.setDummy(false);
    }

    return prod;
  }
}
