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
 * $Id: DefaultLayoutPosition.java,v 1.1 2006/04/21 17:32:04 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import org.jfree.report.data.GlobalMasterRow;
import org.jfree.report.data.ReportDataRow;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.ReportDefinition;
import org.jfree.report.EmptyReportData;

/**
 * The layout position describes the current state in the processing of the
 * layout. It does not contain data information.
 *
 * @author Thomas Morgner
 */
public final class DefaultLayoutPosition implements LayoutPosition
{
  /** The current node, we are dealing with. */
  private Node node;
  /** The parent layout position. This builds a backlinked tree of nodes. */
  private DefaultLayoutPosition parent;
  /** The current flow controler */
  private FlowControler flowControler;
  /** A flag indicating whether the current element has been opened successfully. */
  private boolean elementOpen;
  public static final EmptyReportData EMPTY_REPORT_DATA = new EmptyReportData();

  /**
   * This is the initial layout position.
   *
   * @param flowControler
   * @param node
   */
  public DefaultLayoutPosition(final FlowControler flowControler,
                               final Node node)
  {
    if (flowControler == null)
    {
      throw new NullPointerException();
    }
    // node can be null, if we are at the end of the report ...
    this.node = node;
    this.flowControler = flowControler;
  }

  private DefaultLayoutPosition(final FlowControler flowControler,
                                final Node node,
                                final DefaultLayoutPosition parent)
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
  public DefaultLayoutPosition deriveChildPosition (final FlowControler flowControler,
                                                    final Node node)
  {
    return new DefaultLayoutPosition(flowControler, node, this);
  }

  /**
   * A silbling element should be processed. The previous and the new node share
   * the same parent.
   *
   * @param flowControler
   * @param node
   * @return
   */
  public DefaultLayoutPosition deriveSilblingPosition (final FlowControler flowControler,
                                                       final Node node)
  {
    return new DefaultLayoutPosition(flowControler, node, this.parent);
  }

  /**
   * The current element has been opened successfully. The engine will now
   * process the childs of the current element.
   *
   * @param flowControler
   * @return
   */
  public DefaultLayoutPosition createOpenParent (final FlowControler flowControler)
  {
    if (flowControler == null)
    {
      throw new NullPointerException();
    }

    final DefaultLayoutPosition position = new DefaultLayoutPosition(flowControler, node);
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
  public DefaultLayoutPosition createRepeatPosition(final FlowControler fc)
  {
    final DefaultLayoutPosition position = new DefaultLayoutPosition(fc, node);
    position.elementOpen = false;
    position.parent = parent;
    return position;
  }

  public Node getNode()
  {
    return node;
  }

  public DefaultLayoutPosition getParent()
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

  public DefaultLayoutPosition derive(final FlowControler fc)
  {
    final DefaultLayoutPosition position = new DefaultLayoutPosition(fc, node);
    position.elementOpen = elementOpen;
    position.parent = parent;
    return position;
  }

  public LayoutExpressionRuntime getExpressionRuntime(final FlowControler fc)
  {
    final ReportDefinition report = node.getReport();

    LayoutExpressionRuntime ler = new LayoutExpressionRuntime();
    ler.setConfiguration(fc.getReportJob().getConfiguration());
    ler.setResourceBundleFactory(report.getResourceBundleFactory());

    final GlobalMasterRow masterRow = fc.getMasterRow();
    ler.setDataRow(masterRow.getGlobalView());
    final ReportDataRow reportDataRow = masterRow.getReportDataRow();
    if (reportDataRow == null)
    {
      ler.setData(EMPTY_REPORT_DATA);
      ler.setCurrentRow(-1);
    }
    else
    {
      ler.setData(reportDataRow.getReportData());
      ler.setCurrentRow(reportDataRow.getCursor());
    }
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


  public boolean isFinalPosition()
  {
    return node != null;
  }

  public int compareTo(Object o)
  {
    // todo ..
    return 0;
  }
}
