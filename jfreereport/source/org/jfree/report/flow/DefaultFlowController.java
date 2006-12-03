/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.flow;

import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.data.CachingReportDataFactory;
import org.jfree.report.data.ExpressionDataRow;
import org.jfree.report.data.ExpressionSlot;
import org.jfree.report.data.GlobalMasterRow;
import org.jfree.report.data.ImportedVariablesDataRow;
import org.jfree.report.data.ParameterDataRow;
import org.jfree.report.data.PrecomputedValueRegistry;
import org.jfree.report.data.PrecomputedValueRegistryBuilder;
import org.jfree.report.data.ReportDataRow;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.SubReport;
import org.jfree.report.util.IntegerCache;
import org.jfree.util.FastStack;

/**
 * Creation-Date: 20.02.2006, 15:30:21
 *
 * @author Thomas Morgner
 */
public class DefaultFlowController implements FlowController
{
  private static final boolean STRICT_ASSERTATIONS = false;

  private static class ReportDataContext
  {
    private FastStack markStack;

    public ReportDataContext(final FastStack markStack)
    {
      this.markStack = markStack;
    }

    public FastStack getMarkStack()
    {
      return markStack;
    }
  }

  private CachingReportDataFactory reportDataFactory;
  private GlobalMasterRow dataRow;
  private boolean advanceRequested;
  private FastStack reportStack;
  private FastStack markStack;
  private FastStack expressionsStack;
  private String exportDescriptor;
  private ReportContext reportContext;
  private ReportJob job;
  private PrecomputedValueRegistry precomputedValueRegistry;

  public DefaultFlowController(final ReportContext reportContext,
                               final ReportJob job)
      throws DataSourceException
  {
    if (job == null)
    {
      throw new NullPointerException();
    }
    if (reportContext == null)
    {
      throw new NullPointerException();
    }

    this.reportContext = reportContext;
    this.job = job;
    this.exportDescriptor = reportContext.getExportDescriptor();
    this.reportDataFactory = new CachingReportDataFactory(job.getDataFactory());
    this.reportStack = new FastStack();
    this.markStack = new FastStack();
    this.expressionsStack = new FastStack();
    this.advanceRequested = false;
    this.dataRow = GlobalMasterRow.createReportRow(reportContext);
    this.dataRow.setParameterDataRow(new ParameterDataRow(job.getParameters()));
    this.precomputedValueRegistry = new PrecomputedValueRegistryBuilder();
  }

  protected DefaultFlowController(final DefaultFlowController fc,
                                  final GlobalMasterRow dataRow)
  {
    this.reportContext = fc.reportContext;
    this.job = fc.job;
    this.exportDescriptor = fc.exportDescriptor;
    this.reportDataFactory = fc.reportDataFactory;
    this.reportStack = (FastStack) fc.reportStack.clone();
    this.markStack = (FastStack) fc.markStack.clone();
    this.expressionsStack = (FastStack) fc.expressionsStack.clone();
    this.advanceRequested = fc.advanceRequested;
    this.dataRow = dataRow;
    this.precomputedValueRegistry = fc.precomputedValueRegistry;
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
            GlobalMasterRow.createReportRow(dataRow, reportContext);
    masterRow.setParameterDataRow(new ParameterDataRow
        (getReportJob().getParameters()));

    final String query = report.getQuery();
    masterRow.setReportDataRow(ReportDataRow.createDataRow
        (reportDataFactory, query, dataRow.getGlobalView()));

    DefaultFlowController fc = new DefaultFlowController(this, masterRow);
    fc.reportStack.push(new ReportDataContext(fc.markStack));
    fc.markStack = new FastStack();
    fc.dataRow = masterRow;
    return fc;
  }

  public FlowController performQuery(final SubReport report)
          throws ReportDataFactoryException, DataSourceException
  {
    final GlobalMasterRow outerRow = dataRow.derive();

    // create a view for the parameters of the report ...
    final GlobalMasterRow masterRow =
            GlobalMasterRow.createReportRow(outerRow, reportContext);

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
    fc.reportStack.push(new ReportDataContext(fc.markStack));
    fc.markStack = new FastStack();
    fc.dataRow = masterRow;
    return fc;
  }

  public FlowController activateExpressions(final Element element,
                                            final ExpressionSlot[] expressions)
          throws DataSourceException
  {
    if (STRICT_ASSERTATIONS)
    {
      final JFreeReport rootReport = element.getRootReport();
      if (rootReport == null)
      {
        throw new IllegalStateException("An element without an assigned report.");
      }
    }

    if (expressions.length == 0)
    {
      DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.expressionsStack.push(IntegerCache.getInteger(0));
      return fc;
    }

    final GlobalMasterRow dataRow = this.dataRow.derive();
    final ExpressionDataRow edr = dataRow.getExpressionDataRow();
    edr.pushExpressions(expressions);

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
    // still reference it. (The caching report data factory takes care of
    // that later.)

    ReportDataContext context = (ReportDataContext) fc.reportStack.pop();
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

  public ReportContext getReportContext()
  {
    return reportContext;
  }

  /**
   * Returns the current expression slots of all currently active expressions.
   *
   * @return
   * @throws org.jfree.report.DataSourceException
   *
   */
  public ExpressionSlot[] getActiveExpressions() throws DataSourceException
  {
    return dataRow.getExpressionDataRow().getSlots();
  }

  public FlowController createPrecomputeInstance() throws DataSourceException
  {
    final DefaultFlowController precompute = new DefaultFlowController(this, dataRow.derive());
    precompute.precomputedValueRegistry = new PrecomputedValueRegistryBuilder();
    return precompute;
  }

  public PrecomputedValueRegistry getPrecomputedValueRegistry()
  {
    return precomputedValueRegistry;
  }
}
