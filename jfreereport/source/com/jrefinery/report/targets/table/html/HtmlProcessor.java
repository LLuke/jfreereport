/**
 * Date: Jan 18, 2003
 * Time: 8:05:56 PM
 *
 * $Id: HtmlProcessor.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.table.TableProcessor;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.NullOutputStream;

import java.io.OutputStreamWriter;
import java.io.Writer;

public class HtmlProcessor extends TableProcessor
{
  private Writer writer;

  public HtmlProcessor(JFreeReport report) throws ReportProcessingException, FunctionInitializeException
  {
    super(report);
  }

  public Writer getWriter()
  {
    return writer;
  }

  public void setWriter(Writer writer)
  {
    this.writer = writer;
  }

  public TableProducer createProducer(boolean dummy)
  {
    if (dummy == true)
      return new HtmlProducer(new OutputStreamWriter(new NullOutputStream()), getReport() );
    else
      return new HtmlProducer(getWriter(), getReport());
  }
}
