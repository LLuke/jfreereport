/**
 * Date: Jan 14, 2003
 * Time: 2:32:00 PM
 *
 * $Id: ExcelProcessor.java,v 1.2 2003/01/25 20:34:12 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.table.TableProcessor;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.NullOutputStream;

import java.io.OutputStream;

public class ExcelProcessor extends TableProcessor
{
  private OutputStream outputStream;

  public ExcelProcessor(JFreeReport report) throws ReportProcessingException, FunctionInitializeException
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
    ExcelProducer prod = null;
    if (dummy == true)
    {
      prod = new ExcelProducer(new NullOutputStream(), isStrictLayout());
      prod.setDummy(true);
    }
    else
    {
      prod = new ExcelProducer(getOutputStream(), isStrictLayout());
      prod.setDummy(false);
    }
    return prod;
  }
}
