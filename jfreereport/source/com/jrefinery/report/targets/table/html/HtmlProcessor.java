/**
 * Date: Jan 18, 2003
 * Time: 8:05:56 PM
 *
 * $Id: HtmlProcessor.java,v 1.6 2003/02/02 22:46:44 taqua Exp $
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
    HtmlProducer prod = null;
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
