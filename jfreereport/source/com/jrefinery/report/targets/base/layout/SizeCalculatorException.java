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
 * -------------------
 * SizeCalculatorException.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SizeCalculatorException.java,v 1.1 2003/02/07 22:40:40 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version
 *
 */
package com.jrefinery.report.targets.base.layout;

import com.jrefinery.report.util.StackableException;

/**
 * The SizeCalculatorException is thrown if a SizeCalculator was not able to compute
 * the dimensions of a given content.
 */
public class SizeCalculatorException extends StackableException
{
  /**
   * Creates a SizeCalculatorException with no message and no parent.
   */
  public SizeCalculatorException()
  {
  }

  /**
   * Creates a SizeCalculatorException.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public SizeCalculatorException(String message, Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates a SizeCalculatorException.
   *
   * @param message  the exception message.
   */
  public SizeCalculatorException(String message)
  {
    super(message);
  }
}
