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

package org.jfree.report.flow.layoutprocessor;

import org.jfree.report.DataSourceException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.flow.FlowControlOperation;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.LayoutExpressionRuntime;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.ReportContext;
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

  public SectionLayoutController()
  {
  }

  protected LayoutController startElement(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final Section s = (Section) getNode();

    FlowController fc = getFlowController();
    // Step 3: Add the expressions. Any expressions defined for the subreport
    // will work on the queried dataset.
    fc = startData(target, fc);

    fc = fc.activateExpressions(s);


    if (s.isVirtual() == false)
    {
      LayoutExpressionRuntime ler =
          LayoutControllerUtil.getExpressionRuntime(fc, s);
      target.startElement(s, ler);
    }

    SectionLayoutController derived = (SectionLayoutController) clone();
    derived.setProcessingState(OPENED);
    derived.setFlowController(fc);
    return derived;
  }

  protected FlowController startData(final ReportTarget target,
                                     final FlowController fc)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final Section s = (Section) getNode();
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

    final Section section = (Section) getNode();
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
    final Element e = (Element) getNode();
    // Step 1: call End Element
    FlowController fc = getFlowController();
    if (e.isVirtual() == false)
    {
      LayoutExpressionRuntime ler =
          LayoutControllerUtil.getExpressionRuntime(fc, e);
      target.endElement(e, ler);
    }

    // Step 2: Remove the expressions of this element
    fc = fc.deactivateExpressions();

    // unwind the stack ..
    final Section s = (Section) e;
    fc = finishData(target, fc);

    if (s.isRepeat())
    {

      // ok, the user wanted us to repeat. So we repeat if the group in which
      // we are in, is not closed (and at least one advance has been fired
      // since the last repeat request [to prevent infinite loops]) ...
      final boolean advanceRequested = fc.isAdvanceRequested();
      final boolean advanceable = fc.getMasterRow().isAdvanceable();
      if (advanceable && advanceRequested)
      {
        // we check against the commited target; But we will not use the
        // commited target if the group is no longer active...
        final FlowController cfc =
            fc.performOperation(FlowControlOperation.COMMIT);
        final boolean groupActive =
            LayoutControllerUtil.isGroupActive(cfc, s);
        if (groupActive)
        {
          // Go back to the beginning ...
          SectionLayoutController derived = (SectionLayoutController) clone();
          derived.setProcessingState(NOT_STARTED);
          derived.setFlowController(cfc);
          derived.setIndex(0);
          return derived;
        }
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
    final Section s = (Section) getNode();
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