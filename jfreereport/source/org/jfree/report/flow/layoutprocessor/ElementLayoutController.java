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
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.structure.Node;
import org.jfree.util.Log;

/**
 * Creation-Date: 24.11.2006, 13:56:30
 *
 * @author Thomas Morgner
 */
public abstract class ElementLayoutController
    implements LayoutController, Cloneable
{
  public static final int NOT_STARTED = 0;
  public static final int OPENED = 1;
  public static final int WAITING_FOR_JOIN = 2;
  public static final int FINISHING = 3;
  public static final int JOINING = 4;
  public static final int FINISHED = 5;

  private int processingState;
  private FlowController flowController;
  private Node node;
  private LayoutController parent;

  protected ElementLayoutController()
  {
    this.processingState = NOT_STARTED;
  }

  public LayoutController getParent()
  {
    return parent;
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
  public void initialize(final Node node,
                         final FlowController flowController,
                         final LayoutController parent)
      throws DataSourceException, ReportDataFactoryException, ReportProcessingException
  {

    if (processingState != NOT_STARTED)
    {
      throw new IllegalStateException();
    }

    this.node = node;
    this.flowController = flowController;
    this.parent = parent;
  }

  public final LayoutController advance(ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final int processingState = getProcessingState();
    switch (processingState)
    {
      case NOT_STARTED:
        return startElement(target);
      case OPENED:
        return processContent(target);
      case FINISHING:
        return finishElement(target);
      case JOINING:
        return joinWithParent();
      default:
        throw new IllegalStateException();
    }
  }

  protected abstract LayoutController startElement(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException;
  protected abstract LayoutController processContent(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException;
  protected abstract LayoutController finishElement(final ReportTarget target)
      throws ReportProcessingException, DataSourceException;

  protected LayoutController joinWithParent()
  {
    final LayoutController parent = getParent();
    if (parent == null)
    {
      // skip to the next step ..
      throw new IllegalStateException("There is no parent to join with. " +
              "This should not happen in a sane environment!");
    }

    return parent.join(getFlowController());
  }

  public boolean isAdvanceable()
  {
    return processingState != FINISHED;
  }

  public Node getNode()
  {
    return node;
  }

  public FlowController getFlowController()
  {
    return flowController;
  }

  public int getProcessingState()
  {
    return processingState;
  }

  public void setProcessingState(final int processingState)
  {
    this.processingState = processingState;
  }

  public void setFlowController(final FlowController flowController)
  {
    this.flowController = flowController;
  }

  public void setNode(final Node node)
  {
    this.node = node;
  }

  public void setParent(final LayoutController parent)
  {
    this.parent = parent;
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
