/**
 * Date: Jan 21, 2003
 * Time: 4:47:16 PM
 *
 * $Id: CSVTableProcessor.java,v 1.1 2003/01/22 19:45:28 taqua Exp $
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.table.TableProcessor;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.targets.csv.CSVProcessor;
import com.jrefinery.report.util.NullOutputStream;

import java.io.PrintWriter;
import java.io.Writer;

public class CSVTableProcessor extends TableProcessor
{
  private Writer writer;
  private String separator;

  public CSVTableProcessor(JFreeReport report)
      throws ReportProcessingException, FunctionInitializeException
  {
    this(report,
         report.getReportConfiguration().getConfigProperty(CSVProcessor.CSV_SEPARATOR, ","));
  }

  public CSVTableProcessor(JFreeReport report, String separator) throws ReportProcessingException, FunctionInitializeException
  {
    super(report);
    this.separator = separator;
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
    CSVTableProducer prod = null;
    if (dummy)
    {
      prod = new CSVTableProducer(new PrintWriter(new NullOutputStream()), isStrictLayout());
    }
    else
    {
      prod = new CSVTableProducer(new PrintWriter(getWriter()), isStrictLayout());
    }
    prod.setSeparator(separator);
    return prod;
  }
}
