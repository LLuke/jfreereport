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
 * LayoutPosition.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import org.jfree.report.structure.Node;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.ReportDefinition;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.JFreeReport;
import org.jfree.report.data.GlobalMasterRow;

/**
 * The layout position describes the current state in the processing of the
 * layout. It does not contain data information.
 *
 * @author Thomas Morgner
 */
public final class LayoutPosition
{
  /** The current node, we are dealing with. */
  private Node node;
  /** The parent layout position. This builds a backlinked tree of nodes. */
  private LayoutPosition parent;
  /** The current flow controler */
  private FlowControler flowControler;
  /** A flag indicating whether the current element has been opened successfully. */
  private boolean elementOpen;

  /**
   * This is the initial layout position.
   *
   * @param flowControler
   * @param node
   */
  public LayoutPosition(final FlowControler flowControler,
                        final Node node)
  {
    if (flowControler == null) throw new NullPointerException();
    // node can be null, if we are at the end of the report ...
    this.node = node;
    this.flowControler = flowControler;
  }

  private LayoutPosition(final FlowControler flowControler,
                         final Node node,
                         final LayoutPosition parent)
  {
    this(flowControler, node);
    this.parent = parent;
  }

  /**
   * Lets dive into the current node's childs. The current layout position will
   * be the parent node.
   *
   * @param flowControler
   * @param node
   * @return
   */
  public LayoutPosition deriveChildPosition (final FlowControler flowControler,
                                             final Node node)
  {
    return new LayoutPosition(flowControler, node, this);
  }

  /**
   * A silbling element should be processed. The previous and the new node share
   * the same parent.
   *
   * @param flowControler
   * @param node
   * @return
   */
  public LayoutPosition deriveSilblingPosition (final FlowControler flowControler,
                                                final Node node)
  {
    return new LayoutPosition(flowControler, node, this.parent);
  }

  /**
   * The current element has been opened successfully. The engine will now
   * process the childs of the current element.
   *
   * @param flowControler
   * @return
   */
  public LayoutPosition createOpenParent (final FlowControler flowControler)
  {
    if (flowControler == null)
      throw new NullPointerException();

    final LayoutPosition position = new LayoutPosition(flowControler, node);
    position.elementOpen = true;
    position.parent = parent;
    return position;
  }

  /**
   * Restart the current element - this is the common iteration call for
   * groups and detail bands.
   *
   * @return
   * @param fc
   */
  public LayoutPosition createRepeatPosition(final FlowControler fc)
  {
    final LayoutPosition position = new LayoutPosition(fc, node);
    position.elementOpen = false;
    position.parent = parent;
    return position;
  }

  public Node getNode()
  {
    return node;
  }

  public LayoutPosition getParent()
  {
    return parent;
  }

  public FlowControler getFlowControler()
  {
    return flowControler;
  }

  public boolean isElementOpen()
  {
    return elementOpen;
  }

  public LayoutPosition derive(final FlowControler fc)
  {
    final LayoutPosition position = new LayoutPosition(fc, node);
    position.elementOpen = elementOpen;
    position.parent = parent;
    return position;
  }

  public LayoutExpressionRuntime getExpressionRuntime(final FlowControler fc)
  {
    final ReportDefinition report = node.getReport();
    final JFreeReport rootReport = node.getRootReport();

    LayoutExpressionRuntime ler = new LayoutExpressionRuntime();
    ler.setConfiguration(rootReport.getConfiguration());
    ler.setResourceBundleFactory(report.getResourceBundleFactory());

    final GlobalMasterRow masterRow = fc.getMasterRow();
    ler.setDataRow(masterRow.getGlobalView());
    ler.setData(masterRow.getReportDataRow().getReportData());
    ler.setOutputMetaData(fc.getReportJob().getMetaData());
    if (node instanceof Element)
    {
      ler.setDeclaringParent((Element) node);
    }
    else
    {
      ler.setDeclaringParent(node.getParent());
    }
    return ler;
  }
}
