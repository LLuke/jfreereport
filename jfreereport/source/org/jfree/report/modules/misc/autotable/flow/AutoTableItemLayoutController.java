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
import org.jfree.report.DataFlags;
import org.jfree.report.modules.misc.autotable.AutoTableCellContent;
import org.jfree.report.data.ExpressionSlot;
import org.jfree.report.data.PrecomputedValueRegistry;
import org.jfree.report.data.ReportDataRow;
import org.jfree.report.expressions.Expression;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.LayoutExpressionRuntime;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.layoutprocessor.ElementLayoutController;
import org.jfree.report.flow.layoutprocessor.LayoutController;
import org.jfree.report.flow.layoutprocessor.LayoutControllerUtil;
import org.jfree.report.structure.Element;

/**
 * Creation-Date: Dec 9, 2006, 8:20:51 PM
 *
 * @author Thomas Morgner
 */
public class AutoTableItemLayoutController extends ElementLayoutController
{
  private AttributeMap attributeMap;
  private int expressionsCount;

  public AutoTableItemLayoutController()
  {
  }

  protected LayoutController startElement(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final FlowController flowController = getFlowController();
    final Expression[] expressions = getElement().getExpressions();
    FlowController fc = performElementPrecomputation
        (expressions, flowController);

    final Element element = getElement();
    final LayoutExpressionRuntime ler =
        LayoutControllerUtil.getExpressionRuntime(fc, element);
    final AttributeMap attributeMap =
        LayoutControllerUtil.processAttributes(element, target, ler);
    target.startElement(attributeMap);

    AutoTableItemLayoutController derived = (AutoTableItemLayoutController) clone();
    derived.setProcessingState(OPENED);
    derived.setFlowController(fc);
    derived.attributeMap = attributeMap;
    derived.expressionsCount = expressions.length;
    return derived;
  }

  protected AutoTableLayoutController findTableParent ()
  {
    LayoutController parent = getParent();
    while (parent != null)
    {
      if (parent instanceof AutoTableLayoutController)
      {
        return (AutoTableLayoutController) parent;
      }

      parent = parent.getParent();
    }
    return null;
  }

  protected LayoutController processContent(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final AutoTableCellContent content = (AutoTableCellContent) getElement();
    final FlowController flowController = getFlowController();
    final ReportDataRow reportDataRow =
        flowController.getMasterRow().getReportDataRow();

    final AutoTableLayoutController table = findTableParent();
    if (table == null)
    {
      throw new ReportProcessingException("Invalid state: have no auto-table as context.");
    }
    final int currentColumn = table.getCurrentColumn();

    if ("name".equals(content.getItem()))
    {
      final String columnName = reportDataRow.getColumnName(currentColumn);
      target.processText(columnName);
    }
    else if ("value".equals(content.getItem()))
    {
      final DataFlags flags = reportDataRow.getFlags(currentColumn);
      target.processContent(flags);
    }
    else
    {
      throw new ReportProcessingException("Invalid definition: Content-Item with no valid type");
    }

    AutoTableItemLayoutController derived = (AutoTableItemLayoutController) clone();
    derived.setProcessingState(ElementLayoutController.FINISHING);
    derived.setFlowController(flowController);
    return derived;

  }

  protected LayoutController finishElement(final ReportTarget target)
      throws ReportProcessingException, DataSourceException
  {
    final Element e = getElement();
    // Step 1: call End Element
    target.endElement(attributeMap);

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

    AutoTableItemLayoutController derived = (AutoTableItemLayoutController) clone();
    derived.setProcessingState(FINISHED);
    derived.setFlowController(fc);
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
    AutoTableItemLayoutController derived = (AutoTableItemLayoutController) clone();
    derived.setProcessingState(FINISHING);
    derived.setFlowController(flowController);
    return derived;
  }
}
