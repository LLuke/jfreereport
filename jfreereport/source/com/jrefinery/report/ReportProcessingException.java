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
 * ------------------------------
 * ReportProcessingException.java
 * ------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportProcessingException.java,v 1.7 2002/12/06 17:18:42 mungady Exp $
 *
 * Changes
 * -------
 * 18-Apr-2002 : Created the Exception to better support errorhandling. The exception
 *               is thrown if the report does not proceed while paginating. This is
 *               used to detect infinite loops on buggy report definitions.
 * 06-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report;

import com.jrefinery.report.util.StackableException;

/**
 * An exception that can be thrown during report processing, if an error occurs.
 *
 * @author Thomas Morgner
 */
public class ReportProcessingException extends StackableException
{
  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public ReportProcessingException (String message, Exception ex)
  {
    super (message, ex);
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public ReportProcessingException (String message)
  {
    super (message);
  }
}
