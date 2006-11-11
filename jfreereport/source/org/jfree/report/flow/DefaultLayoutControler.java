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
 * DefaultLayoutControler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultLayoutControler.java,v 1.6 2006/07/30 13:09:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.DataFlags;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.expressions.Expression;
import org.jfree.report.data.DefaultDataFlags;
import org.jfree.report.structure.ContentElement;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Group;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.Section;
import org.jfree.report.structure.SubReport;

/**
 * Creation-Date: 21.02.2006, 18:14:36
 *
 * @author Thomas Morgner
 */
public class DefaultLayoutControler implements LayoutControler
{
  public DefaultLayoutControler()
  {
  }

  public LayoutPosition process(final ReportTarget processor,
                                final LayoutPosition position)
          throws DataSourceException, ReportDataFactoryException,
          ReportProcessingException
  {
    final DefaultLayoutPosition pos = (DefaultLayoutPosition) position;
    final Node n = pos.getNode();

    if (n instanceof Element)
    {
      Element e = (Element) n;

      if (pos.isElementOpen() == false)
      {
        return startElement(pos, e, processor);
      }
      else
      {
        return endElement(pos, e, processor);
      }
    }

    // nodes are easy ...
    FlowControler fc = pos.getFlowControler();
    LayoutExpressionRuntime ler = pos.getExpressionRuntime(fc);
    processor.processNode(n, ler);
    return skipPosition(pos, pos.getFlowControler());
  }

  private DefaultLayoutPosition endElement(final DefaultLayoutPosition pos, final Element e,
                                    final ReportTarget processor)
          throws
          DataSourceException, ReportProcessingException
  {
    // Step 1: call End Element
    FlowControler fc = pos.getFlowControler();
    if (e.isVirtual() == false)
    {
      LayoutExpressionRuntime ler = pos.getExpressionRuntime(fc);
      processor.endElement(e, ler);
    }

    // Step 2: Remove the expressions of this element
    fc = fc.deactivateExpressions();

    // Step 3: Perform the flow operations which are defined at the end.....
    // Step 2a: Perform queries, if necessary, and do the SubReporting
    if (e instanceof JFreeReport)
    {
      processor.endReport((JFreeReport) e);
      // do something fancy ... query the data, for instance.
      // this implies that we create a new global datarow.
      fc = fc.performReturnFromQuery();
    }
    else if (e instanceof SubReport)
    {
      fc = fc.performReturnFromQuery();
    }
    else if (e instanceof Section)
    {
      final Section s = (Section) e;
      fc = processFlowOperations(fc, s.getOperationAfter());
    }

    return advancePosition(pos, fc);
  }

  private DefaultLayoutPosition startElement(final DefaultLayoutPosition pos,
                                      final Element e,
                                      final ReportTarget processor)
          throws ReportDataFactoryException, DataSourceException,
          ReportProcessingException
  {
    // Step 1: Activate the enable expression ...
    FlowControler fc = pos.getFlowControler();
    if (fc == null)
    {
      throw new NullPointerException("Assertation: FlowControler is null");
    }

    if (isElementEnabled(pos, fc, e) == false)
    {
      return skipPosition(pos, fc);
    }

    // Step 2a: Perform queries, if necessary, and do the SubReporting
    if (e instanceof JFreeReport)
    {
      // do something fancy ... query the data, for instance.
      // this implies that we create a new global datarow.
      processor.startReport((JFreeReport) e);
      fc = fc.performQuery((JFreeReport) e);
    }
    else if (e instanceof SubReport)
    {
      fc = fc.performQuery((SubReport) e);
    }

    // Step 2b: Check the flow control. This is only done if this element
    // is no subreport element. This enforces a clean design of the report
    // definition itself and does not leave space for any ambugities ..
    else if (e instanceof Section)
    {
      final Section s = (Section) e;
      fc = processFlowOperations(fc, s.getOperationBefore());
    }

    // Step 3: Add the expressions. Any expressions defined for the subreport
    // will work on the queried dataset.
    fc = fc.activateExpressions(e);

    if (e.isVirtual() == false)
    {
      LayoutExpressionRuntime ler = pos.getExpressionRuntime(fc);
      processor.startElement(e, ler);
    }
    if (e instanceof ContentElement)
    {
      return processContentElement(pos, fc, processor);
    }
    return advanceForStartElement(pos, e, fc);
  }

  protected DefaultLayoutPosition processContentElement
          (final DefaultLayoutPosition pos,
           final FlowControler fc,
           final ReportTarget processor)
          throws DataSourceException, ReportProcessingException
  {
    final LayoutExpressionRuntime er = pos.getExpressionRuntime(fc);
    final ContentElement node = (ContentElement) pos.getNode();
    final Expression ex = node.getValueExpression();
    final Object value;
    try
    {
      ex.setRuntime(er);
      value = ex.computeValue();
    }
    finally
    {
      ex.setRuntime(null);
    }

    if (value instanceof DataFlags)
    {
      processor.processContentElement
              (node, (DataFlags) value, er);
      return advanceForStartElement(pos, node, fc);
    }
    else if (value instanceof Node)
    {
      // we explictly allow structural content here.
      // As this might be a very expensive thing, if we
      // keep it in a single state, we continue on a separate state.
      Node valueNode = (Node) value;

      // actually, this is the same as if the element were a
      // child element of a section. The only difference is
      // that there can be only one child, and that there is no
      // direct parent-child direction.
      return pos.deriveChildPosition(fc, valueNode);
    }
    else
    {
      processor.processContentElement
              (node, new DefaultDataFlags(ex.getName(), value, true), er);
      return advanceForStartElement(pos, node, fc);
    }
  }

  public FlowControler processFlowOperations(FlowControler fc,
                                             FlowControlOperation[] ops)
          throws DataSourceException
  {
    for (int i = 0; i < ops.length; i++)
    {
      FlowControlOperation op = ops[i];
      fc = fc.performOperation(op);
    }
    return fc;
  }

  private boolean isElementEnabled(final DefaultLayoutPosition pos,
                                   final FlowControler fc,
                                   final Element e) throws DataSourceException
  {
    if (e.isEnabled() == false)
    {
      return false;
    }

    if (e instanceof Section)
    {
      final Section s = (Section) e;
      final Expression dc = s.getDisplayCondition();
      if (dc == null)
      {
        return true;
      }

      dc.setRuntime(pos.getExpressionRuntime(fc));
      final boolean retval = Boolean.TRUE.equals(dc.computeValue());
      dc.setRuntime(null);
      return retval;
    }

    return true;
  }


  /**
   * Ignore the contents of the current element. Skip to the next element on
   * the same level. If there is no next element, return the parent ...
   *
   * @param position
   * @param flowControler
   * @return
   */
  protected DefaultLayoutPosition skipPosition(final DefaultLayoutPosition position,
                                        final FlowControler flowControler)
  {
    final Node n = position.getNode();

    // so this is either a closed element or it is a plain node. Try to
    // go to the next element or (if there is no next element) to the parent ...
    final DefaultLayoutPosition parent = position.getParent();
    if (parent == null)
    {
      // we are finished here ...
      return new DefaultLayoutPosition(flowControler, null);
    }

    final Node parentNode = parent.getNode();
    if (parentNode instanceof Section)
    {
      // ok, this thing can have children. So look for our current position
      // and then advance to the next one.
      final Section parentSection = (Section) parentNode;
      int nodeIndex = findNodeInParent(parentSection, n);
      if (nodeIndex >= 0 && ((nodeIndex + 1) < parentSection.getNodeCount()))
      {
        final Node nextNode = parentSection.getNode(nodeIndex + 1);
        return position.deriveSilblingPosition(flowControler, nextNode);
      }
    }

    // closing down. How about a repeat operation?
    return parent.derive(flowControler);
  }

  protected DefaultLayoutPosition advanceForStartElement(final DefaultLayoutPosition pos,
                                                  final Element element,
                                                  final FlowControler fc)
  {
    if (fc == null)
    {
      throw new NullPointerException();
    }
    // this is a starting element and may have children. Advance to the
    // first child, if possible.
    if (element instanceof Section)
    {
      Section s = (Section) element;
      if (s.getNodeCount() > 0)
      {
        final DefaultLayoutPosition parent = pos.createOpenParent(fc);
        return parent.deriveChildPosition(fc, s.getNode(0));
      }
    }

    // oh, we dont have children, maybe because this is no section or
    // maybe because the section is empty. Anyway, mark the thing as opened.
    // we will close it in the next iteration.
    return pos.createOpenParent(fc);
  }

  /**
   * Traverse the element tree and dive into the elements. This is called, after
   * an element has been closed. It searches for silbling elements and if there
   * are no more silblings, it activates the parent element.
   *
   * @param position
   * @param fc
   * @return
   */
  protected DefaultLayoutPosition advancePosition(final DefaultLayoutPosition position,
                                                  FlowControler fc)
          throws DataSourceException
  {
    final Node n = position.getNode();

    // Sometimes we want sections to repeat. This is a requirement for groups,
    // which must be iterated until all the group data has been processed.
    if (n instanceof Section == false)
    {
      return skipPosition(position, fc);
    }

    final Section s = (Section) n;
    if (s.isRepeat() == false)
    {
      return skipPosition(position, fc);
    }

    // ok, the user wanted us to repeat. So we repeat if the group in which
    // we are in, is not closed (and at least one advance has been fired
    // since the last repeat request [to prevent infinite loops]) ...
    final boolean advanceRequested = fc.isAdvanceRequested();
    final boolean advanceable = fc.getMasterRow().isAdvanceable();
    if (advanceable && advanceRequested)
    {
      // we check against the commited target; But we will not use the
      // commited target if the group is no longer active...
      final FlowControler cfc =
              fc.performOperation(FlowControlOperation.COMMIT);
      final boolean groupActive = isGroupActive(position, cfc);
      if (groupActive)
      {
        return position.createRepeatPosition(cfc);
      }
    }
    return skipPosition(position, fc);
  }

  /**
   * Checks, whether the current group should continue. If there is no group, we
   * assume that we should continue.
   *
   * @param position
   * @return
   */
  private boolean isGroupActive(final DefaultLayoutPosition position,
                                final FlowControler fc)
          throws DataSourceException
  {
    final Node node = position.getNode();
    final Section nodeParent = node.getParent();
    if (nodeParent == null)
    {
      return true;
    }
    Group group = nodeParent.getGroup();
    if (group == null)
    {
      return true;
    }

    // maybe we can move this state into the layoutstate itself so that
    // we do not have to rebuild that crap all the time.
    LayoutExpressionRuntime ler = position.getExpressionRuntime(fc);

    // OK, now we are almost complete.
    do
    {
      ler.setDeclaringParent(group);

      final Expression groupingExpression = group.getGroupingExpression();
      if (groupingExpression == null)
      {
        // stay in the group. This is the same behaviour as if we check
        // for field equality on an empty field list.
        return true;
      }
      groupingExpression.setRuntime(ler);
      final Object result;
      try
      {
        result = groupingExpression.computeValue();
      }
      finally
      {
        groupingExpression.setRuntime(null);
      }
      if (Boolean.FALSE.equals(result))
      {
        return false;
      }
      Section parent = group.getParent();
      if (parent == null)
      {
        group = null;
      }
      else
      {
        group = parent.getGroup();
      }
    }
    while (group != null);
    return true;
  }

  private int findNodeInParent(Section parentSection, Node n)
  {
    final Node[] nodes = parentSection.getNodeArray();
    for (int i = 0; i < nodes.length; i++)
    {
      Node node = nodes[i];
      if (node == n)
      {
        return i;
      }
    }
    return -1;
  }

  public LayoutPosition createInitialPosition
          (final FlowControler flowControler,
           final Node initialNode)
  {
    return new DefaultLayoutPosition (flowControler, initialNode);
  }
}
