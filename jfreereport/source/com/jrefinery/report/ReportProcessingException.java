/**
 * =============================================================
 * JFreeReport - a Java report printing API;
 * =========================================
 * Version 0.50;
 * (C) Copyright 2000, Simba Management Limited;
 * Contact: David Gilbert (david.gilbert@bigfoot.com);
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307, USA.
 *
 * ReportProcessingException.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ReportProcessingException.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
 * Changes
 * -------------------------
 * 18-Apr-2002 : Created the Exception to better support errorhandling. The exception
 *               is thrown if the report does not proceed while paginating. This is
 *               used to detect infinite loops on buggy report definitions.
 *
 */
package com.jrefinery.report;

public class ReportProcessingException extends Exception
{
  public ReportProcessingException ()
  {
  }

  public ReportProcessingException (String message)
  {
    super (message);
  }


}
