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

package org.jfree.report.modules.misc.autotable.flow;

import org.jfree.layouting.util.AttributeMap;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.data.ExpressionSlot;
import org.jfree.report.data.PrecomputedValueRegistry;
import org.jfree.report.data.ReportDataRow;
import org.jfree.report.expressions.Expression;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.LayoutExpressionRuntime;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.ReportContext;
import org.jfree.report.flow.FlowControlOperation;
import org.jfree.report.flow.layoutprocessor.ElementLayoutController;
import org.jfree.report.flow.layoutprocessor.LayoutController;
import org.jfree.report.flow.layoutprocessor.LayoutControllerUtil;
import org.jfree.report.flow.layoutprocessor.LayoutControllerFactory;
import org.jfree.report.modules.misc.autotable.AutoTableElement;
import org.jfree.report.modules.misc.autotable.AutoTableModule;
import org.jfree.report.structure.Element;

/**
 * Creation-Date: Dec 9, 2006, 6:05:58 PM
 *
 * @author Thomas Morgner
 */
public class AutoTableLayoutController extends ElementLayoutController
{
  public static final int HANDLING_HEADER = 0;
  public static final int HANDLING_DATA = 1;
  public static final int HANDLING_FOOTER = 2;

  private int currentColumn;
  private int processingState;
  private int expressionsCount;
  private int columnCount;
  private AttributeMap tableAttributeMap;

  public AutoTableLayoutController()
  {
  }

  /**
   * Calling initialize after the first advance must result in a
   * IllegalStateException.
   *
   * @param flowController
   * @param initialNode
   * @throws org.jfree.report.DataSourceException
   *
   * @throws org.jfree.report.ReportDataFactoryException
   *
   * @throws org.jfree.report.ReportProcessingException
   *
   */
  public void initialize(final Object node,
                         final FlowController flowController,
                         final LayoutController parent)
      throws DataSourceException, ReportDataFactoryException, ReportProcessingException
  {
    if (node instanceof AutoTableElement == false)
    {
      throw new ReportProcessingException("Element type is no auto-table.");
    }

    super.initialize(node, flowController, parent);
  }

  protected LayoutController startElement(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final FlowController flowController = getFlowController();
    final ReportDataRow reportDataRow =
        flowController.getMasterRow().getReportDataRow();
    final int columnCount = reportDataRow.getColumnCount();

    final Expression[] expressions = getElement().getExpressions();
    FlowController fc = performElementPrecomputation
        (expressions, flowController);

    final Element element = getElement();
    final LayoutExpressionRuntime ler =
        LayoutControllerUtil.getExpressionRuntime(fc, element);
    final AttributeMap attributeMap =
        LayoutControllerUtil.processAttributes(element, target, ler);
    target.startElement(attributeMap);

    AutoTableLayoutController derived = (AutoTableLayoutController) clone();
    derived.setProcessingState(OPENED);
    derived.setFlowController(fc);
    derived.expressionsCount = expressions.length;
    derived.tableAttributeMap = attributeMap;
    derived.columnCount = columnCount;
    return derived;
  }

  protected LayoutController finishElement(final ReportTarget target)
      throws ReportProcessingException, DataSourceException
  {
    final Element e = getElement();
    // Step 1: call End Element
    target.endElement(tableAttributeMap);

    FlowController fc = getFlowController();
    final PrecomputedValueRegistry pcvr =
        fc.getPrecomputedValueRegistry();
    // grab all values from the expressionsdatarow


    // Step 2: Remove the expressions of this element
    if (expressionsCount != 0)
    {
      final ExpressionSlot[] activeExpressions = fc.getActiveExpressions();
      for (int i = 0; i < activeExpressions.length; i++)
      {
        final ExpressionSlot slot = activeExpressions[i];
        pcvr.addFunction(slot.getName(), slot.getValue());
      }
      fc = fc.deactivateExpressions();
    }

    if (isPrecomputing() == false)
    {
      pcvr.finishElement(new ElementPrecomputeKey(e));
    }

    final LayoutController parent = getParent();
    if (parent != null)
    {
      return parent.join(fc);
    }

    AutoTableLayoutController derived = (AutoTableLayoutController) clone();
    derived.setProcessingState(FINISHED);
    derived.setFlowController(fc);
    return derived;
  }

  protected LayoutController processContent(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    switch(processingState)
    {
      case HANDLING_HEADER: return processHeader(target);
      case HANDLING_FOOTER: return processFooter(target);
      case HANDLING_DATA: return processData(target);
    }
    throw new ReportProcessingException("No such state.");
  }

  private LayoutController processData(final ReportTarget target)
      throws ReportProcessingException, DataSourceException, ReportDataFactoryException
  {
    // the auto-table is responsible for the iteration over the table.
    final AutoTableElement node = (AutoTableElement) getElement();
    if (node.getContentCount() == 0)
    {
      throw new ReportProcessingException
          ("An Auto-Table must have at least one defined column.");
    }

    if (currentColumn == 0)
    {
      // Start a new table-header section ..
      final AttributeMap elementMap = LayoutControllerUtil.createEmptyMap
          (AutoTableModule.AUTOTABLE_NAMESPACE, "data-row");
      target.startElement(elementMap);
    }

    if (currentColumn < columnCount)
    {
      // now delegate the processing to the section handler for the header ..
      final FlowController flowController = getFlowController();
      final ReportContext reportContext = flowController.getReportContext();
      final LayoutControllerFactory layoutControllerFactory =
          reportContext.getLayoutControllerFactory();

      final int idx = currentColumn % node.getContentCount();
      final AutoTableLayoutController derived = (AutoTableLayoutController) clone();
      return layoutControllerFactory.create
          (flowController, node.getContentCell(idx), derived);
    }

    // close the table-header section ..
    final AttributeMap elementMap = LayoutControllerUtil.createEmptyMap
        (AutoTableModule.AUTOTABLE_NAMESPACE, "data-row");
    target.endElement(elementMap);

    final FlowController flowController =
        getFlowController().performOperation(FlowControlOperation.ADVANCE);
    final FlowController cfc = tryRepeatingCommit(flowController);
    if (cfc != null)
    {
      // Go back to the beginning. We have made a commit, so the cursor points
      // to the next row of data ..
      AutoTableLayoutController derived = (AutoTableLayoutController) clone();
      derived.setFlowController(cfc);
      derived.currentColumn = 0;
      return derived;
    }


    // Advance is impossible, that means we reached the end of the group or
    // the end of the table ..
    AutoTableLayoutController derived = (AutoTableLayoutController) clone();
    derived.currentColumn = 0;
    derived.processingState = HANDLING_FOOTER;
    return derived;
  }

  private LayoutController processFooter(final ReportTarget target)
      throws ReportProcessingException, DataSourceException, ReportDataFactoryException
  {
    final AutoTableElement node = (AutoTableElement) getElement();
    if (node.getFooterCount() == 0)
    {
      AutoTableLayoutController derived = (AutoTableLayoutController) clone();
      derived.currentColumn = 0;
      derived.processingState = -1;
      derived.setProcessingState(ElementLayoutController.FINISHING);
      return derived;
    }

    if (currentColumn == 0)
    {
      // Start a new table-header section ..
      final AttributeMap elementMap = LayoutControllerUtil.createEmptyMap
          (AutoTableModule.AUTOTABLE_NAMESPACE, "footer-row");
      target.startElement(elementMap);
    }

    if (currentColumn < columnCount)
    {
      // now delegate the processing to the section handler for the header ..
      final FlowController flowController = getFlowController();
      final ReportContext reportContext = flowController.getReportContext();
      final LayoutControllerFactory layoutControllerFactory =
          reportContext.getLayoutControllerFactory();

      final int idx = currentColumn % node.getFooterCount();
      final AutoTableLayoutController derived = (AutoTableLayoutController) clone();
      return layoutControllerFactory.create
          (flowController, node.getFooterCell(idx), derived);
    }

    // close the table-header section ..
    final AttributeMap elementMap = LayoutControllerUtil.createEmptyMap
        (AutoTableModule.AUTOTABLE_NAMESPACE, "footer-row");
    target.endElement(elementMap);

    AutoTableLayoutController derived = (AutoTableLayoutController) clone();
    derived.currentColumn = 0;
    derived.processingState = -1;
    derived.setProcessingState(ElementLayoutController.FINISHING);
    return derived;
  }

  private LayoutController processHeader(final ReportTarget target)
      throws ReportProcessingException, DataSourceException, ReportDataFactoryException
  {
    final AutoTableElement node = (AutoTableElement) getElement();
    if (node.getHeaderCount() == 0)
    {
      AutoTableLayoutController derived = (AutoTableLayoutController) clone();
      derived.currentColumn = 0;
      derived.processingState = HANDLING_DATA;
      return derived;
    }

    if (currentColumn == 0)
    {
      // Start a new table-header section ..
      final AttributeMap elementMap = LayoutControllerUtil.createEmptyMap
          (AutoTableModule.AUTOTABLE_NAMESPACE, "header-row");
      target.startElement(elementMap);
    }

    if (currentColumn < columnCount)
    {
      // now delegate the processing to the section handler for the header ..
      final FlowController flowController = getFlowController();
      final ReportContext reportContext = flowController.getReportContext();
      final LayoutControllerFactory layoutControllerFactory =
          reportContext.getLayoutControllerFactory();

      final int idx = currentColumn % node.getHeaderCount();
      final AutoTableLayoutController derived = (AutoTableLayoutController) clone();
      return layoutControllerFactory.create
          (flowController, node.getHeaderCell(idx), derived);
    }

    // close the table-header section ..
    final AttributeMap elementMap = LayoutControllerUtil.createEmptyMap
        (AutoTableModule.AUTOTABLE_NAMESPACE, "header-row");
    target.endElement(elementMap);

    AutoTableLayoutController derived = (AutoTableLayoutController) clone();
    derived.currentColumn = 0;
    derived.processingState = HANDLING_DATA;
    return derived;
  }

  /**
   * Joins with a delegated process flow. This is generally called from a child
   * flow and should *not* (I mean it!) be called from outside. If you do,
   * you'll suffer.
   *
   * @param flowController
   * @return
   */
  public LayoutController join(FlowController flowController)
  {
    AutoTableLayoutController derived = (AutoTableLayoutController) clone();
    derived.setFlowController(flowController);
    derived.currentColumn += 1;
    return derived;
  }

  public int getCurrentColumn ()
  {
    return currentColumn;
  }
}
