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
 * ------------------------------
 * EmptyReportException.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: EmptyReportException.java,v 1.1 2003/07/07 22:43:59 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12.06.2003 : Initial version
 *
 */

package org.jfree.report;

/**
 * The EmptyReportException is thrown, it the report processing generated
 * no content.
 *
 * @author Thomas Morgner.
 */
public class EmptyReportException extends ReportProcessingException
{
  /**
   * Creates an EmptyReportException.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public EmptyReportException(final String message, final Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an EmptyReportException.
   *
   * @param message  the exception message.
   */
  public EmptyReportException(final String message)
  {
    super(message);
  }
}
