/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * InvalidStyleSheetCollectionException.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: InvalidStyleSheetCollectionException.java,v 1.4 2003/06/29 16:59:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 19-Jun-2003 : Initial version
 *
 */

package org.jfree.report.style;

import org.jfree.util.StackableRuntimeException;

/**
 * Informs the caller, that an invalid state was encountered while handling a
 * stylesheetcollection operation.
 * <p>
 * The InvalidStyleSheetCollectionException is thrown, if the stylesheet collection
 * encountered an invalid stylesheet collection for one of its children or when the
 * StyleSheetCollectionHandler detected an invalid StyleSheetCollection on its
 * child.
 *
 * @author Thomas Morgner
 */
public class InvalidStyleSheetCollectionException extends StackableRuntimeException
{
  /**
   * DefaultConstructor. Initializes this exception without an message or parent
   * exception.
   */
  public InvalidStyleSheetCollectionException()
  {
  }

  /**
   * Creates a new InvalidStyleSheetCollectionException with the given message
   * and parent exception.
   *
   * @param s the exception message.
   * @param e the parent exception.
   */
  public InvalidStyleSheetCollectionException(final String s, final Exception e)
  {
    super(s, e);
  }

  /**
   * Creates a new InvalidStyleSheetCollectionException with the give message.
   *
   * @param s the exception message.
   */
  public InvalidStyleSheetCollectionException(final String s)
  {
    super(s);
  }
}
