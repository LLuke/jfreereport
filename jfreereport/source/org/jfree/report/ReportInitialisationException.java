/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ----------------------------------
 * ReportInitialisationException.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ReportInitialisationException.java,v 1.1 2003/07/07 22:43:59 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report;

import org.jfree.util.StackableException;

/**
 * An exception that is thrown when a report fails to initialise.
 *
 * @author Thomas Morgner.
 */
public class ReportInitialisationException extends StackableException
{
  /**
   * Constructs an <code>Exception</code> with no specified detail message.
   */
  public ReportInitialisationException()
  {
  }

  /**
   * Constructs an <code>Exception</code> with the specified detail message.
   *
   * @param s  the detail message.
   */
  public ReportInitialisationException(final String s)
  {
    super(s);
  }

  /**
   * Constructs an <code>Exception</code> with the specified detail message.
   *
   * @param s  the detail message.
   * @param e  the parent exception.
   */
  public ReportInitialisationException(final String s, final Exception e)
  {
    super(s, e);
  }
}
