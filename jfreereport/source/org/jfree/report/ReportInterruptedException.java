/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -------------------------------
 * ReportInterruptedException.java
 * -------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ReportInterruptedException.java,v 1.3 2003/08/24 15:13:21 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */
package org.jfree.report;

/**
 * This exception is thrown when the current thread received the Interrupt-signal while
 * the report is beeing processed. Depending on the ReportProcessor implementation such an
 * signal would abort the report generation.
 *
 * @author Thomas Morgner
 */
public class ReportInterruptedException extends ReportProcessingException
{
  /**
   * Creates an exception.
   *
   * @param message the exception message.
   * @param ex      the parent exception.
   */
  public ReportInterruptedException (final String message, final Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an exception.
   *
   * @param message the exception message.
   */
  public ReportInterruptedException (final String message)
  {
    super(message);
  }
}
