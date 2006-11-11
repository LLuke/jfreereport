/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
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
 * $Id: Function.java,v 1.8 2006/04/18 11:28:39 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner and modified by DG (DG);
 * 10-May-2002 : Functions now extend the ReportListener interface. Initialize is replaced
 *               by reportStarted ()
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package org.jfree.report.expressions;

import org.jfree.report.DataSourceException;

/**
 * The interface for report functions.  A report function separates the business
 * logic from presentation of the result.
 * <p/>
 * Since JFreeReport 0.9 functions are considered immutable. During the
 * advancement process, the function returns a new instance with the updated
 * state.
 *
 * @author Thomas Morgner
 */
public interface Function extends Expression
{
  /**
   * When the advance method is called, the function is asked to perform the
   * next step of its computation.
   * <p/>
   * The original function must not be altered during that step (or more
   * correctly, calling advance on the original expression again must not return
   * a different result).
   *
   * @return a copy of the function containing the new state.
   */
  public Function advance() throws DataSourceException;
}
