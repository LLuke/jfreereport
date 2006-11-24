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

import org.jfree.report.DataFlags;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.data.DefaultDataFlags;
import org.jfree.report.expressions.Expression;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.LayoutExpressionRuntime;
import org.jfree.report.flow.ReportContext;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.structure.ContentElement;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Node;

/**
 * Creation-Date: 24.11.2006, 15:06:56
 *
 * @author Thomas Morgner
 */
public class ContentElementLayoutController extends ElementLayoutController
{

  public ContentElementLayoutController()
  {
  }

  protected LayoutController startElement(final ReportTarget target)
      throws DataSourceException, ReportProcessingException
  {
    final Element e = (Element) getNode();
    FlowController fc = getFlowController();
    // Step 3: Add the expressions. Any expressions defined for the subreport
    // will work on the queried dataset.
    fc = fc.activateExpressions(e);

    if (e.isVirtual() == false)
    {
      LayoutExpressionRuntime ler =
          LayoutControllerUtil.getExpressionRuntime(fc, e);
      target.startElement(e, ler);
    }
    ContentElementLayoutController derived = (ContentElementLayoutController) clone();
    derived.setProcessingState(OPENED);
    derived.setFlowController(fc);
    return derived;
  }

  protected LayoutController processContent(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {

    final Node node = getNode();
    final FlowController flowController = getFlowController();
    final LayoutExpressionRuntime er =
        LayoutControllerUtil.getExpressionRuntime(flowController, node);
    final ContentElement element = (ContentElement) node;
    final Expression ex = element.getValueExpression();
    final Object value;

    if (ex != null)
    {
      try
      {
        ex.setRuntime(er);
        value = ex.computeValue();
      }
      finally
      {
        ex.setRuntime(null);
      }
    }
    else
    {
      value = null;
    }

    // This should be a very rare case indeed ..
    if (value instanceof DataFlags)
    {
      target.processContentElement
          (element, (DataFlags) value, er);

      ContentElementLayoutController derived = (ContentElementLayoutController) clone();
      derived.setProcessingState(FINISHING);
      derived.setFlowController(flowController);
      return derived;
    }

    if (value instanceof Node)
    {
      // we explictly allow structural content here.
      // As this might be a very expensive thing, if we
      // keep it in a single state, we continue on a separate state.
      final Node valueNode = (Node) value;
      valueNode.updateParent(node.getParent());
      final ReportContext reportContext = flowController.getReportContext();
      final LayoutControllerFactory layoutControllerFactory =
          reportContext.getLayoutControllerFactory();

      // actually, this is the same as if the element were a
      // child element of a section. The only difference is
      // that there can be only one child, and that there is no
      // direct parent-child direction.

      final ContentElementLayoutController derived =
          (ContentElementLayoutController) clone();
      derived.setProcessingState(WAITING_FOR_JOIN);
      derived.setFlowController(flowController);

      return layoutControllerFactory.create
          (flowController, valueNode, derived);
    }

    if (ex != null)
    {
      // todo: How can libformula maintain the 'DataFlags'.

      target.processContentElement
          (element, new DefaultDataFlags(ex.getName(), value, true), er);
    }

    ContentElementLayoutController derived = (ContentElementLayoutController) clone();
    derived.setProcessingState(FINISHING);
    derived.setFlowController(flowController);
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
    ContentElementLayoutController derived = (ContentElementLayoutController) clone();
    derived.setProcessingState(FINISHING);
    derived.setFlowController(flowController);
    return derived;
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

    final LayoutController parent = getParent();
    if (parent != null)
    {
      return parent.join(fc);
    }
    
    ContentElementLayoutController derived = (ContentElementLayoutController) clone();
    derived.setProcessingState(FINISHED);
    derived.setFlowController(fc);
    return derived;
  }
}