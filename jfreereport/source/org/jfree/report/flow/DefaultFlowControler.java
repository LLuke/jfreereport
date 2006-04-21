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
 * $Id: DefaultFlowControler.java,v 1.1 2006/04/18 11:49:11 taqua Exp $
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
import org.jfree.report.data.ExpressionDataRow;
import org.jfree.report.data.GlobalMasterRow;
import org.jfree.report.data.ParameterDataRow;
import org.jfree.report.data.ReportDataRow;
import org.jfree.report.data.StaticExpressionRuntimeData;
import org.jfree.report.data.ImportedVariablesDataRow;
import org.jfree.report.function.Expression;
import org.jfree.report.function.sys.QueryVariableExpression;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.ReportDefinition;
import org.jfree.report.structure.SubReport;
import org.jfree.report.util.IntegerCache;
import org.jfree.util.Log;

/**
 * Creation-Date: 20.02.2006, 15:30:21
 *
 * @author Thomas Morgner
 */
public class DefaultFlowControler implements FlowControler
{
  private static long lastTime;

  private ReportJob job;
  private GlobalMasterRow dataRow;
  private boolean advanceRequested;
  private Stack markStack;
  private Stack expressionsStack;

  public DefaultFlowControler(final ReportJob job) throws DataSourceException
  {
    if (job == null)
    {
      throw new NullPointerException();
    }

    this.markStack = new Stack();
    this.expressionsStack = new Stack();
    this.dataRow = GlobalMasterRow.createReportRow();
    this.dataRow.setParameterDataRow(new ParameterDataRow(job.getParameters()));
    this.job = job;
    lastTime = System.currentTimeMillis();
  }

  protected DefaultFlowControler(final DefaultFlowControler fc,
                                 final GlobalMasterRow dataRow)
  {
    this.markStack = (Stack) fc.markStack.clone();
    this.expressionsStack = (Stack) fc.expressionsStack.clone();
    this.job = fc.job;
    this.advanceRequested = fc.advanceRequested;
    this.dataRow = dataRow;
  }


  public FlowControler performOperation(FlowControlOperation operation)
          throws DataSourceException
  {
    if (operation == FlowControlOperation.ADVANCE)
    {
      if (dataRow.isAdvanceable())
      {
        DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
        fc.advanceRequested = true;
        return fc;
      }
    }
    else if (operation == FlowControlOperation.MARK)
    {
      DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
      fc.markStack.push(dataRow);
      return fc;
    }
    else if (operation == FlowControlOperation.RECALL)
    {
      DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
      fc.dataRow = (GlobalMasterRow) fc.markStack.pop();
      return fc;
    }
    else if (operation == FlowControlOperation.DONE)
    {
      // do not change the current data row..

      DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
      fc.markStack.pop();
      return fc;
    }
    else if (operation == FlowControlOperation.COMMIT)
    {
      if (isAdvanceRequested())
      {
        DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
        fc.dataRow = dataRow.advance();
        int currentRow = dataRow.getReportDataRow().getReportData().getCurrentRow();
        if (currentRow % 1000 == 0)
        {
          long time = System.currentTimeMillis();
          Log.debug ("" + currentRow + " Time: " + (time - lastTime));
          lastTime = time;
        }
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
  public FlowControler performQuery(final JFreeReport report)
          throws ReportDataFactoryException, DataSourceException
  {
    final String query = report.getQuery();
    final ReportDataFactory dataFactory = job.getDataFactory();
    final ReportData data = dataFactory.queryData(query,
            dataRow.getGlobalView());
    final GlobalMasterRow masterRow =
            GlobalMasterRow.createReportRow(dataRow);
    masterRow.setParameterDataRow(new ParameterDataRow(job.getParameters()));
    masterRow.setReportDataRow(ReportDataRow.createDataRow(data));

    DefaultFlowControler fc = new DefaultFlowControler(this, masterRow);
    fc.markStack.push(dataRow);
    fc.dataRow = masterRow;
    return fc;
  }

  public FlowControler performQuery(final SubReport report)
          throws ReportDataFactoryException, DataSourceException
  {
    // check and rebuild the parameter mapping from the inner to the outer
    // context. Only deep-traversal expressions will be able to see these
    // values (unless they have been defined as local variables).
    final String[] exportedParams = report.getExportParameters();
    final String[] exportedNames = report.getPeerExportParameters();
    final GlobalMasterRow outerRow = dataRow.derive();

    // create a view for the parameters of the report ...
    final GlobalMasterRow masterRow =
            GlobalMasterRow.createReportRow(outerRow);
    masterRow.setParameterDataRow
            (new ParameterDataRow(report, outerRow.getGlobalView()));
    // perform the query ...
    final String query = report.getQuery();
    final ReportDataFactory dataFactory = job.getDataFactory();
    final ReportData data = dataFactory.queryData(query,
            masterRow.getGlobalView());
    // add the resultset ...
    masterRow.setReportDataRow(ReportDataRow.createDataRow(data));

    outerRow.setExportedDataRow(new ImportedVariablesDataRow
            (masterRow, exportedNames, exportedParams));

    DefaultFlowControler fc = new DefaultFlowControler(this, masterRow);
    fc.markStack.push(outerRow);
    fc.dataRow = masterRow;
    return fc;
  }

  public FlowControler activateExpressions(final Element element)
          throws DataSourceException
  {
    final JFreeReport rootReport = element.getRootReport();
    if (rootReport == null)
    {
      throw new IllegalStateException("An element without an assigned report.");
    }
    final ReportDefinition report = element.getReport();
    final Expression[] expressions = element.getExpressions();
    final String[] variables = element.getVariables();
    if (expressions.length == 0 && variables.length == 0)
    {
      DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
      fc.expressionsStack.push(IntegerCache.getInteger(0));
      return fc;
    }

    final StaticExpressionRuntimeData sdd = new StaticExpressionRuntimeData();
    sdd.setData(dataRow.getReportDataRow().getReportData());
    sdd.setDeclaringParent(element);
    sdd.setConfiguration(job.getConfiguration());
    sdd.setResourceBundleFactory(report.getResourceBundleFactory());
    sdd.setOutputProcessorMetaData(job.getMetaData());

    final GlobalMasterRow dataRow = this.dataRow.derive();
    final ExpressionDataRow edr = dataRow.getExpressionDataRow();
    edr.pushExpressions(expressions, sdd);

    final Expression[] qVarsExpressions = new Expression[variables.length];
    for (int i = 0; i < qVarsExpressions.length; i++)
    {
      qVarsExpressions[i] = new QueryVariableExpression(variables[i]);
    }
    edr.pushExpressions(qVarsExpressions, sdd);

    DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
    final Integer exCount = IntegerCache.getInteger
            (expressions.length + qVarsExpressions.length);
    fc.expressionsStack.push(exCount);
    return fc;
  }

  public FlowControler deactivateExpressions() throws DataSourceException
  {
    final Integer counter = (Integer) this.expressionsStack.peek();
    final int counterRaw = counter.intValue();
    if (counterRaw == 0)
    {
      DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
      fc.expressionsStack.pop();
      return fc;
    }

    final GlobalMasterRow dataRow = this.dataRow.derive();
    final ExpressionDataRow edr = dataRow.getExpressionDataRow();

    DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
    fc.expressionsStack.pop();
    edr.popExpressions(counterRaw);
    return fc;
  }

  public FlowControler performReturnFromQuery() throws DataSourceException
  {
    DefaultFlowControler fc = new DefaultFlowControler(this, dataRow);
    fc.dataRow = (GlobalMasterRow) fc.markStack.pop();
    fc.dataRow = fc.dataRow.derive();
    fc.dataRow.setExportedDataRow(null);
    return fc;
  }

  public ReportJob getReportJob()
  {
    return job;
  }
}
