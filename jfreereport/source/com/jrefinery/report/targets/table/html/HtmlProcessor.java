/**
 * Date: Jan 18, 2003
 * Time: 8:05:56 PM
 *
 * $Id: HtmlProcessor.java,v 1.3 2003/01/25 20:34:12 taqua Exp $
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

  public HtmlProcessor(JFreeReport report)
    throws ReportProcessingException, FunctionInitializeException
  {
    this (report, false);
  }

  public HtmlProcessor(JFreeReport report, boolean useXHTML) throws ReportProcessingException, FunctionInitializeException
  {
    super(report);
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

  public TableProducer createProducer(boolean dummy)
  {
    if (dummy == true)
      return new HtmlProducer(new StreamHtmlFilesystem(new NullOutputStream()),
                              getReport().getName(), isStrictLayout(), useXHTML );
    else
      return new HtmlProducer(getFilesystem(), getReport().getName(),
                              isStrictLayout(), useXHTML);
  }
}
