/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------------------------
 * ObjectFactoryException.java
 * ---------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.util.StackableException;

/**
 * An exception that is thrown, if the creation of an Object failed in the
 * ObjectFactory implementation.
 * 
 * @author Thomas Morgner.
 */
public class ObjectFactoryException extends StackableException
{
  /**
   * Constructs a new exception with <code>null</code> as its detail message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link #initCause}.
   */
  public ObjectFactoryException()
  {
  }

  /**
   * Constructs a new exception with the specified detail message.  The
   * cause is not initialized, and may subsequently be initialized by
   * a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for
   *        later retrieval by the {@link #getMessage()} method.
   */
  public ObjectFactoryException(String message)
  {
    super(message);
  }

  /**
   * Creates a new exception.
   * 
   * @param message  the message.
   * @param cause  the cause of the exception.
   */
  public ObjectFactoryException(String message, Exception cause)
  {
    super(message, cause);
  }
}
