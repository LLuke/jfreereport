/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * BeanException.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BeanException.java,v 1.3 2005/03/03 23:00:26 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.beans;

import org.jfree.util.StackableException;

/**
 * The BeanException class signals errors when setting or reading bean
 * properties.
 *
 * @author Thomas Morgner
 */
public class BeanException extends StackableException
{
  /**
   * DefaultConstructor.
   */
  public BeanException ()
  {
  }

  /**
   * Creates a new BeanException with the given message and parent exception.
   *
   * @param message the message text
   * @param ex the parent exception
   */
  public BeanException (final String message, final Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates a new BeanException with the given message.
   *
   * @param message the message.
   */
  public BeanException (final String message)
  {
    super(message);
  }
}
