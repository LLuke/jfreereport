/**
 * Date: Jan 18, 2003
 * Time: 8:05:56 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.table.TableProcessor;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.NullOutputStream;

import java.io.OutputStream;

public class HtmlProcessor extends TableProcessor
{
  private OutputStream outputStream;

  public HtmlProcessor(JFreeReport report) throws ReportProcessingException, FunctionInitializeException
  {
    super(report);
  }

  public OutputStream getOutputStream()
  {
    return outputStream;
  }

  public void setOutputStream(OutputStream outputStream)
  {
    this.outputStream = outputStream;
  }

  public TableProducer createProducer(boolean dummy)
  {
    if (dummy == true)
      return new HtmlProducer(new NullOutputStream(), getReport() );
    else
      return new HtmlProducer(getOutputStream(), getReport());
  }
}
