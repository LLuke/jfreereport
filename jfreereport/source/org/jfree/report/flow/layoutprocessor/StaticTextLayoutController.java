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
 * $Id: StaticTextLayoutController.java,v 1.2 2006/12/03 20:24:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.flow.layoutprocessor;

import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.LayoutExpressionRuntime;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.structure.Node;
import org.jfree.util.Log;

/**
 * Creation-Date: 24.11.2006, 15:06:56
 *
 * @author Thomas Morgner
 */
public class StaticTextLayoutController implements LayoutController, Cloneable
{
  public static final int NOT_STARTED = 0;
  public static final int FINISHED = 2;

  private int state;
  private Node node;
  private FlowController flowController;
  private LayoutController parent;

  public StaticTextLayoutController()
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

    this.node = (Node) node;
    this.flowController = flowController;
    this.parent = parent;
  }

  /**
   * Advances the processing position.
   *
   * @param target
   * @return
   * @throws org.jfree.report.DataSourceException
   *
   * @throws org.jfree.report.ReportDataFactoryException
   *
   * @throws org.jfree.report.ReportProcessingException
   *
   */
  public LayoutController advance(ReportTarget target)
      throws DataSourceException, ReportDataFactoryException, ReportProcessingException
  {
    if (state == NOT_STARTED)
    {
      final LayoutExpressionRuntime expressionRuntime =
          LayoutControllerUtil.getExpressionRuntime(getFlowController(), getNode());
      target.processNode(getNode(), expressionRuntime);

      if (getParent() != null)
      {
        return getParent().join(getFlowController());
      }
      else
      {
        StaticTextLayoutController derived = (StaticTextLayoutController) clone();
        derived.state = FINISHED;
        return derived;
      }
    }
    else
    {
      throw new IllegalStateException();
    }
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
    throw new UnsupportedOperationException("Static text does not have childs.");
  }

  public boolean isAdvanceable()
  {
    return state != FINISHED;
  }

  public Node getNode()
  {
    return node;
  }

  public FlowController getFlowController()
  {
    return flowController;
  }

  public LayoutController getParent()
  {
    return parent;
  }

  public Object clone ()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      Log.error("Clone not supported: " , e);
      throw new IllegalStateException("Clone must be supported.");
    }
  }
}
