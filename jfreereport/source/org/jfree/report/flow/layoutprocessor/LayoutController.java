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
 * LayoutControler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LayoutController.java,v 1.1 2006/11/20 21:10:40 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow.layoutprocessor;

import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.structure.Node;

/**
 * The layout controller iterates over the report layout. It uses a flow
 * controller to query the data.
 *
 * @author Thomas Morgner
 */
public interface LayoutController
{
  /**
   * Calling initialize after the first advance must result in a
   * IllegalStateException.
   *
   * @param flowController
   * @param initialNode
   * @throws DataSourceException
   * @throws ReportDataFactoryException
   * @throws ReportProcessingException
   */
  public void initialize(final Node node,
                         final FlowController flowController,
                         final LayoutController parent)
      throws DataSourceException, ReportDataFactoryException,
      ReportProcessingException;

  /**
   * Advances the processing position.
   *
   * @param target
   * @return
   * @throws DataSourceException
   * @throws ReportDataFactoryException
   * @throws ReportProcessingException
   */
  public LayoutController advance (ReportTarget target)
      throws DataSourceException, ReportDataFactoryException,
      ReportProcessingException;

  /**
   * Joins with a delegated process flow. This is generally called from a
   * child flow and should *not* (I mean it!) be called from outside. If you
   * do, you'll suffer.
   *
   * @param flowController
   * @return
   */
  public LayoutController join (FlowController flowController);

  public boolean isAdvanceable ();
}
