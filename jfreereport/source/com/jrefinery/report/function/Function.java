/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -------------------
 * ReportFunction.java
 * -------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Function.java,v 1.2 2002/05/14 21:35:04 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner and modified by DG (DG);
 * 10-May-2002 : Functions now extend the ReportListener interface. Initialize is replaced
 *               by reportStarted ()
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.event.ReportListener;

import javax.swing.table.TableModel;
import java.util.Properties;

/**
 * The interface for report functions.  A report function separates the business logic from
 * presentation of the result.  The function is called whenever JFreeReport changes its state
 * while generating the report. The working model for the functions is based on cloning the
 * state of the function on certain checkpoints to support the ReportState implementation of
 * JFreeReport.
 * <p>
 * Although functions support the ReportListener interface, they are not directly added to
 * an report. An report FunctionCollection is used to control the functions. Functions are
 * required to be cloneable.
 * <p>
 * Todo: Give a better overview how functions are integrated into JFreeReport.
 */
public interface Function extends ReportListener, Cloneable
{

  /**
   * Returns the name of the function (every function is required to have a unique name).
   */
  public String getName ();

  /**
   * Sets the name of the function. The name must not be null and must be unique within
   * the function group.
   *
   * @param name The name.
   */
  public void setName (String name);

  /**
   * Return the current function value. The value depends on the
   * function. A page counting function for instance will return the
   * current page number.
   */
  public Object getValue ();

  /**
   * Clones the function in its current state.  This is used for recording the report state at
   * page boundaries.
   */
  public Object clone () throws CloneNotSupportedException;

  /**
   * Set the function properties. This is the only way to feed
   * parameters to an function
   */
  public void setProperties (Properties p);

  /**
   * return false, if not all parameters are set, or some other
   * error occures during initialisation. A valid function has an valid (not null) name.
   */
  public void initialize () throws FunctionInitializeException;
}
