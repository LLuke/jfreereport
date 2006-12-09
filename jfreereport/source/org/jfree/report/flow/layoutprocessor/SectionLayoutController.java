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
 * $Id: SectionLayoutController.java,v 1.6 2006/12/06 17:26:06 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.flow.layoutprocessor;

import org.jfree.layouting.util.AttributeMap;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.data.ExpressionSlot;
import org.jfree.report.data.PrecomputedValueRegistry;
import org.jfree.report.expressions.Expression;
import org.jfree.report.flow.FlowControlOperation;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.LayoutExpressionRuntime;
import org.jfree.report.flow.ReportContext;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.Section;

/**
 * Creation-Date: 24.11.2006, 13:56:10
 *
 * @author Thomas Morgner
 */
public class SectionLayoutController extends ElementLayoutController
{
  // we store the child instead of the index, as the report can be manipulated
  // it is safer this way ..
  private Node[] nodes;
  private int index;
  private int expressionsCount;
  private AttributeMap attributeMap;

  public SectionLayoutController()
  {
  }

  protected LayoutController startElement(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final Section s = (Section) getElement();

    FlowController fc = getFlowController();
    // Step 3: Add the expressions. Any expressions defined for the subreport
    // will work on the queried dataset.
    fc = startData(target, fc);

    final Expression[] expressions = s.getExpressions();
    fc = performElementPrecomputation(expressions, fc);

    if (s.isVirtual() == false)
    {
      final LayoutExpressionRuntime ler =
          LayoutControllerUtil.getExpressionRuntime(fc, s);
      attributeMap = LayoutControllerUtil.processAttributes(s, target, ler);
      target.startElement(attributeMap);
    }

    SectionLayoutController derived = (SectionLayoutController) clone();
    derived.setProcessingState(OPENED);
    derived.setFlowController(fc);
    derived.expressionsCount = expressions.length;
    derived.attributeMap = attributeMap;
    return derived;
  }

  protected FlowController startData(final ReportTarget target,
                                     final FlowController fc)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final Section s = (Section) getElement();
    return LayoutControllerUtil.processFlowOperations
        (fc, s.getOperationBefore());
  }

  protected LayoutController processContent(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {

    final FlowController flowController = getFlowController();
    final ReportContext reportContext = flowController.getReportContext();
    final LayoutControllerFactory layoutControllerFactory =
        reportContext.getLayoutControllerFactory();

    final Section section = (Section) getElement();
    if (nodes == null)
    {
      // this is the first child ..
      nodes = section.getNodeArray();
    }

    if (index < nodes.length)
    {
      SectionLayoutController derived = (SectionLayoutController) clone();
      derived.setProcessingState(WAITING_FOR_JOIN);
      derived.setFlowController(flowController);
      return layoutControllerFactory.create
          (flowController, nodes[index], derived);
    }
    else
    {
      SectionLayoutController derived = (SectionLayoutController) clone();
      derived.setProcessingState(FINISHING);
      derived.setFlowController(flowController);
      return derived;
    }
  }

  protected LayoutController finishElement(final ReportTarget target)
      throws ReportProcessingException, DataSourceException
  {
    final Element e = getElement();
    // Step 1: call End Element
    FlowController fc = getFlowController();
    if (e.isVirtual() == false)
    {
      target.endElement(attributeMap);
    }

    final PrecomputedValueRegistry pcvr =
        fc.getPrecomputedValueRegistry();
    // Step 2: Remove the expressions of this element
    if (expressionsCount != 0)
    {
      final ExpressionSlot[] activeExpressions = fc.getActiveExpressions();
      for (int i = activeExpressions.length - expressionsCount; i < activeExpressions.length; i++)
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

    // unwind the stack ..
    final Section s = (Section) e;
    fc = finishData(target, fc);

    if (s.isRepeat())
    {
      final FlowController cfc = tryRepeatingCommit(fc);
      if (cfc != null)
      {
        // Go back to the beginning ...
        SectionLayoutController derived = (SectionLayoutController) clone();
        derived.setProcessingState(NOT_STARTED);
        derived.setFlowController(cfc);
        derived.setIndex(0);
        return derived;
      }
    }

    final LayoutController parent = getParent();
    if (parent != null)
    {
      return parent.join(fc);
    }

    // Go back to the beginning ...
    SectionLayoutController derived = (SectionLayoutController) clone();
    derived.setProcessingState(FINISHED);
    derived.setFlowController(fc);
    return derived;
  }

  protected FlowController finishData(final ReportTarget target,
                                      final FlowController fc)
      throws DataSourceException, ReportProcessingException
  {
    final Section s = (Section) getElement();
    return LayoutControllerUtil.processFlowOperations
        (fc, s.getOperationAfter());
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
    int index = getIndex() + 1;
    for (; index < nodes.length; index++)
    {
      Node node = nodes[index];
      if (node instanceof Element == false)
      {
        break;
      }
      Element e = (Element) node;
      if (e.isEnabled())
      {
        break;
      }
    }

    if (index < nodes.length)
    {
      SectionLayoutController derived = (SectionLayoutController) clone();
      derived.setProcessingState(OPENED);
      derived.setFlowController(flowController);
      derived.setIndex(index);
      return derived;
    }

    SectionLayoutController derived = (SectionLayoutController) clone();
    derived.setProcessingState(FINISHING);
    derived.setFlowController(flowController);
    return derived;
  }

  public Node[] getNodes()
  {
    return nodes;
  }

  public int getIndex()
  {
    return index;
  }

  public void setNodes(final Node[] nodes)
  {
    this.nodes = nodes;
  }

  public void setIndex(final int index)
  {
    this.index = index;
  }
}
