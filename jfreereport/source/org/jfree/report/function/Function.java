/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * As a special exception, the copyright holders of JFreeReport give you
 * permission to extend JFreeReport with modules that implement the
 * "org.jfree.report.function.Expression" or the
 * "org.jfree.report.function.Function" interface, regardless
 * of the license terms of these implementations, and to copy and distribute the
 * resulting combined work under terms of your choice, provided that
 * every copy of the combined work is accompanied by a complete copy of
 * the source code of JFreeReport (the version of JFreeReport used to produce the
 * combined work), being distributed under the terms of the GNU Lesser
 * General Public License plus this exception.
 *
 * This exception applies to the Java interfaces "Expression" and "Function"
 * and the classes "AbstractExpression" and "AbstractFunction" of the package
 * "org.jfree.report.function".
 *
 * Note that people who make modified versions of JFreeReport are not obligated
 * to grant this special exception for their modified versions; it is
 * their choice whether to do so.  The GNU Lesser General Public License gives
 * permission to release a modified version without this exception; this
 * exception also makes it possible to release a modified version which
 * carries forward this exception.
 *
 * -------------
 * Function.java
 * -------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Function.java,v 1.2 2003/08/24 15:13:22 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner and modified by DG (DG);
 * 10-May-2002 : Functions now extend the ReportListener interface. Initialize is replaced
 *               by reportStarted ()
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package org.jfree.report.function;

import org.jfree.report.event.ReportListener;

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
