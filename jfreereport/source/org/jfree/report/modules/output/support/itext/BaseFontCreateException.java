/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * BaseFontCreateException.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BaseFontCreateException.java,v 1.7 2005/01/25 00:11:40 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.support.itext;

import org.jfree.util.StackableRuntimeException;

/**
 * The BaseFontCreateException is thrown if there are problemns while creating iText
 * fonts.
 *
 * @author Thomas Morgner
 */
public class BaseFontCreateException extends StackableRuntimeException
{
  /**
   * Creates a new BaseFontCreateException with no message.
   */
  public BaseFontCreateException ()
  {
  }

  /**
   * Creates a new BaseFontCreateException with the given message and base exception.
   *
   * @param s the message for this exception
   * @param e the exception that caused this exception.
   */
  public BaseFontCreateException (final String s, final Exception e)
  {
    super(s, e);
  }

  /**
   * Creates a new BaseFontCreateException with the given message.
   *
   * @param s the message for this exception
   */
  public BaseFontCreateException (final String s)
  {
    super(s);
  }
}
