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
 * -----------------------------
 * ContentCreationException.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ContentCreationException.java,v 1.3 2004/05/07 08:02:49 mungady Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version
 */
package org.jfree.report.content;

import org.jfree.util.StackableException;

/**
 * A ContentCreationException is thrown whenever a content could not be created.
 *
 * @author Thomas Morgner
 */
public class ContentCreationException extends StackableException
{
  /**
   * Creates a ContentCreationException with no message and no parent.
   */
  public ContentCreationException ()
  {
  }

  /**
   * Creates an ContentCreationException.
   *
   * @param message the exception message.
   * @param ex      the parent exception.
   */
  public ContentCreationException (final String message, final Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an ContentCreationException.
   *
   * @param message the exception message.
   */
  public ContentCreationException (final String message)
  {
    super(message);
  }
}
