/**
 * Date: Feb 1, 2003
 * Time: 7:52:45 PM
 *
 * $Id: RTFProcessor.java,v 1.1 2003/02/01 22:10:37 taqua Exp $
 */
package com.jrefinery.report.targets.table.rtf;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.targets.table.TableProcessor;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.NullOutputStream;

import java.io.OutputStream;

public class RTFProcessor extends TableProcessor
{
  private OutputStream outputStream;

  public RTFProcessor(JFreeReport report)
      throws ReportProcessingException, FunctionInitializeException
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
    RTFProducer prod;
    if (dummy == true)
    {
      prod = new RTFProducer(new NullOutputStream(),
                              isStrictLayout());
      prod.setDummy(true);
    }
    else
    {
      prod = new RTFProducer(getOutputStream(), isStrictLayout());
      prod.setDummy(false);
    }

    return prod;
  }
}
