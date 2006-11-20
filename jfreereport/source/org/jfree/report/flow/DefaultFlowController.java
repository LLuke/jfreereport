/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultFlowControler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultFlowControler.java,v 1.6 2006/11/11 20:37:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import java.util.Stack;

import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportData;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.expressions.Expression;
import org.jfree.report.expressions.GlobalReportContext;
import org.jfree.report.data.ExpressionDataRow;
import org.jfree.report.data.GlobalMasterRow;
import org.jfree.report.data.ImportedVariablesDataRow;
import org.jfree.report.data.ParameterDataRow;
import org.jfree.report.data.ReportDataRow;
import org.jfree.report.data.StaticExpressionRuntimeData;
import org.jfree.report.data.CachingReportDataFactory;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.ReportDefinition;
import org.jfree.report.structure.SubReport;
import org.jfree.report.util.IntegerCache;

/**
 * Creation-Date: 20.02.2006, 15:30:21
 *
 * @author Thomas Morgner
 */
public class DefaultFlowController implements FlowController
{
  private static class ReportContext
  {
    private Stack markStack;

    public ReportContext(final Stack markStack)
    {
      this.markStack = markStack;
    }

    public Stack getMarkStack()
    {
      return markStack;
    }
  }

  private CachingReportDataFactory reportDataFactory;
  private ReportJob job;
  private GlobalMasterRow dataRow;
  private boolean advanceRequested;
  private Stack reportStack;
  private Stack markStack;
  private Stack expressionsStack;
  private String exportDescriptor;

  public DefaultFlowController(final ReportJob job,
                               final String exportDescriptor)
      throws DataSourceException
  {
    this.exportDescriptor = exportDescriptor;
    if (job == null)
    {
      throw new NullPointerException();
    }

    this.reportDataFactory = new CachingReportDataFactory(job.getDataFactory());
    this.reportStack = new Stack();
    this.markStack = new Stack();
    this.expressionsStack = new Stack();
    this.dataRow = GlobalMasterRow.createReportRow();
    this.dataRow.setParameterDataRow(new ParameterDataRow(job.getParameters()));
    this.job = job;
  }

  protected DefaultFlowController(final DefaultFlowController fc,
                                  final GlobalMasterRow dataRow)
  {
    this.reportStack = (Stack) fc.reportStack.clone();
    this.markStack = (Stack) fc.markStack.clone();
    this.expressionsStack = (Stack) fc.expressionsStack.clone();
    this.job = fc.job;
    this.advanceRequested = fc.advanceRequested;
    this.dataRow = dataRow;
    this.exportDescriptor = fc.exportDescriptor;
  }


  public FlowController performOperation(FlowControlOperation operation)
          throws DataSourceException
  {
    if (operation == FlowControlOperation.ADVANCE)
    {
      if (dataRow.isAdvanceable())
      {
        DefaultFlowController fc = new DefaultFlowController(this, dataRow);
        fc.advanceRequested = true;
        return fc;
      }
    }
    else if (operation == FlowControlOperation.MARK)
    {
      DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.markStack.push(dataRow);
      return fc;
    }
    else if (operation == FlowControlOperation.RECALL)
    {
      if (markStack.isEmpty())
      {
        return this;
      }
      
      DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.dataRow = (GlobalMasterRow) fc.markStack.pop();
      fc.advanceRequested = false;
      return fc;
    }
    else if (operation == FlowControlOperation.DONE)
    {
      // do not change the current data row..

      DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.markStack.pop();
      return fc;
    }
    else if (operation == FlowControlOperation.COMMIT)
    {
      if (isAdvanceRequested())
      {
        DefaultFlowController fc = new DefaultFlowController(this, dataRow);
        fc.dataRow = dataRow.advance();
        fc.advanceRequested = false;
        return fc;
      }
    }
    return this;
  }

  public GlobalMasterRow getMasterRow()
  {
    return dataRow;
  }

  public GlobalReportContext getGlobalContext()
  {
    return dataRow.getExpressionDataRow().getGlobalReportContext();
  }

  public boolean isAdvanceRequested()
  {
    return advanceRequested;
  }

  /**
   * This should be called only once per report processing. A JFreeReport object
   * defines the global master report - all other reports are subreport instances.
   * <p/>
   * The global master report receives its parameter set from the Job-Definition,
   * while subreports will read their parameters from the current datarow state.
   *
   * @param report
   * @return
   * @throws ReportDataFactoryException
   * @throws DataSourceException
   */
  public FlowController performQuery(final JFreeReport report)
          throws ReportDataFactoryException, DataSourceException
  {

    final GlobalMasterRow masterRow =
            GlobalMasterRow.createReportRow(dataRow);
    masterRow.setParameterDataRow(new ParameterDataRow(job.getParameters()));

    final String query = report.getQuery();
    masterRow.setReportDataRow(ReportDataRow.createDataRow
        (reportDataFactory, query, dataRow.getGlobalView()));

    DefaultFlowController fc = new DefaultFlowController(this, masterRow);
    fc.reportStack.push(new ReportContext(fc.markStack));
    fc.markStack = new Stack();
    fc.dataRow = masterRow;
    return fc;
  }

  public FlowController performQuery(final SubReport report)
          throws ReportDataFactoryException, DataSourceException
  {
    final GlobalMasterRow outerRow = dataRow.derive();

    // create a view for the parameters of the report ...
    final GlobalMasterRow masterRow =
            GlobalMasterRow.createReportRow(outerRow);

    if (report.isGlobalImport())
    {
      masterRow.setParameterDataRow
              (new ParameterDataRow(report, outerRow.getGlobalView()));
    }
    else
    {
      // check and rebuild the parameter mapping from the outer to the int
      // context. We allow only the defined input set here. This helps a lot
      // in reducing (or at least documenting) the dependencies between subreports
      final String[] importedParams = report.getInputParameters();
      final String[] importedNames = report.getPeerInputParameters();
      masterRow.setParameterDataRow
              (new ParameterDataRow(report, new ImportedVariablesDataRow
              (masterRow, importedNames, importedParams)));
    }

    // perform the query ...
    final String query = report.getQuery();
    // add the resultset ...
    masterRow.setReportDataRow(ReportDataRow.createDataRow
        (reportDataFactory, query, masterRow.getGlobalView()));

    if (report.isGlobalExport())
    {
      outerRow.setExportedDataRow(new ImportedVariablesDataRow(masterRow));
    }
    else
    {
      // check and rebuild the parameter mapping from the inner to the outer
      // context. Only deep-traversal expressions will be able to see these
      // values (unless they have been defined as local variables).
      final String[] exportedParams = report.getExportParameters();
      final String[] exportedNames = report.getPeerExportParameters();
      outerRow.setExportedDataRow(new ImportedVariablesDataRow
              (masterRow, exportedNames, exportedParams));
    }

    DefaultFlowController fc = new DefaultFlowController(this, masterRow);
    fc.reportStack.push(new ReportContext(fc.markStack));
    fc.markStack = new Stack();
    fc.dataRow = masterRow;
    return fc;
  }

  public FlowController activateExpressions(final Element element)
          throws DataSourceException
  {
    final JFreeReport rootReport = element.getRootReport();
    if (rootReport == null)
    {
      throw new IllegalStateException("An element without an assigned report.");
    }
    final ReportDefinition report = element.getReport();
    final Expression[] expressions = element.getExpressions();
    if (expressions.length == 0)
    {
      DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.expressionsStack.push(IntegerCache.getInteger(0));
      return fc;
    }

    final StaticExpressionRuntimeData sdd = new StaticExpressionRuntimeData();
    sdd.setData(dataRow.getReportDataRow().getReportData());
    sdd.setDeclaringParent(element);
    sdd.setConfiguration(job.getConfiguration());
    sdd.setResourceBundleFactory(report.getResourceBundleFactory());
    sdd.setGlobalReportContext(dataRow.getExpressionDataRow().getGlobalReportContext());
    sdd.setExportDescriptor(exportDescriptor);

    final GlobalMasterRow dataRow = this.dataRow.derive();
    final ExpressionDataRow edr = dataRow.getExpressionDataRow();
    edr.pushExpressions(expressions, sdd);


    DefaultFlowController fc = new DefaultFlowController(this, dataRow);
    final Integer exCount = IntegerCache.getInteger(expressions.length);
    fc.expressionsStack.push(exCount);
    return fc;
  }

  public FlowController deactivateExpressions() throws DataSourceException
  {
    final Integer counter = (Integer) this.expressionsStack.peek();
    final int counterRaw = counter.intValue();
    if (counterRaw == 0)
    {
      DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.expressionsStack.pop();
      return fc;
    }

    final GlobalMasterRow dataRow = this.dataRow.derive();
    final ExpressionDataRow edr = dataRow.getExpressionDataRow();

    DefaultFlowController fc = new DefaultFlowController(this, dataRow);
    fc.expressionsStack.pop();
    edr.popExpressions(counterRaw);
    return fc;
  }

  public FlowController performReturnFromQuery() throws DataSourceException
  {
    DefaultFlowController fc = new DefaultFlowController(this, dataRow);
    final ReportDataRow reportDataRow = dataRow.getReportDataRow();
    if (reportDataRow == null)
    {
      return this;
    }
    // We dont close the report data, as some previously saved states may
    // still reference it.
//
//    final ReportData reportData = reportDataRow.getReportData();
//    reportData.close();
    
    ReportContext context = (ReportContext) fc.reportStack.pop();
    fc.dataRow = dataRow.getParentDataRow();
    fc.dataRow = fc.dataRow.derive();
    fc.dataRow.setExportedDataRow(null);
    fc.markStack = context.getMarkStack();
    return fc;
  }

  public ReportJob getReportJob()
  {
    return job;
  }

  public String getExportDescriptor()
  {
    return exportDescriptor;
  }
}