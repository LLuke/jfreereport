/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------------------------
 * ReportInitialisationException.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportInitialisationException.java,v 1.5 2003/02/05 15:38:13 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report;

import com.jrefinery.util.StackableException;

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
  public ReportInitialisationException(String s)
  {
    super(s);
  }

  /**
   * Constructs an <code>Exception</code> with the specified detail message.
   *
   * @param s  the detail message.
   * @param e  the parent exception.
   */
  public ReportInitialisationException(String s, Exception e)
  {
    super(s, e);
  }
}
