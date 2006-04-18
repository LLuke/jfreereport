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
 * ---------------------
 * AbstractFunction.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractFunction.java,v 1.9 2005/02/23 21:04:47 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Added property support and removed the get/set Field/Group
 *               functions.
 * 10-May-2002 : Support for ReportListenerInterface added. All old eventFunctions are
 *               marked deprecated. The name-attribute must not be null, or the validity check
 *               will fail.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 27-Aug-2002 : Documentation and removed the deprecated functions
 * 31-Aug-2002 : Documentation update and removed isInitializedFunction
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.function;

import java.io.Serializable;

/**
 * Base class for implementing new report functions.  Provides empty implementations of
 * all the methods in the Function interface.
 * <p/>
 * The function is initialized when it gets added to the report. The method
 * <code>initialize</code> gets called to perform the required initializations. At this
 * point, all function properties must have been set to a valid state and the function
 * must be named. If the initialisation fails, a FunctionInitializeException is thrown and
 * the function get not added to the report.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractFunction extends AbstractExpression
        implements Function, Serializable
{
  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  protected AbstractFunction ()
  {
  }

  /**
   * Creates an named function.
   *
   * @param name the name of the function.
   */
  protected AbstractFunction (final String name)
  {
    setName(name);
  }
}
