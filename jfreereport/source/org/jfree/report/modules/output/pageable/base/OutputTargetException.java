/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * --------------------------
 * OutputTargetException.java
 * --------------------------
 * (C)opyright 2000-2002, by Object Refinery Limited.
 *
 * $Id: OutputTargetException.java,v 1.2 2003/08/24 15:03:52 taqua Exp $
 *
 * Changes
 * -------
 * 16-May-2002 : Version 1 (DG);
 * 31-Aug-2002 : Changed PrintStackTrace implementation to reveal the parent exception
 * 07-Feb-2003 : OutputTargetException now extendes SizeCalculator exception, the size
 *               calculator is now a separated from the pageable output classes.
 */
package org.jfree.report.modules.output.pageable.base;

import org.jfree.report.layout.SizeCalculatorException;

/**
 * An OutputTargetException is thrown if a element could not be printed in the target or
 * an TargetInternalError occurred, that made proceeding impossible.
 *
 * @author David Gilbert
 */
public class OutputTargetException extends SizeCalculatorException
{
  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public OutputTargetException(final String message, final Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public OutputTargetException(final String message)
  {
    super(message);
  }
}
