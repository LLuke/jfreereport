/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------
 * Function.java
 * -------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Function.java,v 1.12 2002/12/02 17:29:11 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner and modified by DG (DG);
 * 10-May-2002 : Functions now extend the ReportListener interface. Initialize is replaced
 *               by reportStarted ()
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package com.jrefinery.report.function;

import com.jrefinery.report.event.ReportListener;

/**
 * The interface for report functions.  A report function separates the business logic from
 * presentation of the result.  The function is called whenever JFreeReport changes its state
 * while generating the report. The working model for the functions is based on cloning the
 * state of the function on certain checkpoints to support the ReportState implementation of
 * JFreeReport.
 * <p>
 * Although functions support the ReportListener interface, they are not directly added to
 * a report. A report FunctionCollection is used to control the functions. Functions are
 * required to be cloneable.
 * <p>
 * Todo: Give a better overview how functions are integrated into JFreeReport.
 *
 * @author Thomas Morgner
 */
public interface Function extends ReportListener, Expression, Cloneable
{
  /**
   * Clones the function.
   *
   * @return a clone of the function.
   *
   * @throws CloneNotSupportedException this should never be thrown.
   */
  public Object clone() throws CloneNotSupportedException;
}
