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
 * ---------------------------------
 * ObjectStreamResolveException.java
 * ---------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * $Id: ObjectStreamResolveException.java,v 1.3 2004/05/07 08:14:23 mungady Exp $
 *
 * Changes
 * -------
 * 05-Feb-2003 : Initial version.
 *
 */
package org.jfree.report.util;

import java.io.ObjectStreamException;

/**
 * The <code>ObjectStreamResolveException</code> this thrown, when the object resolving
 * operation for serialized objects failed.
 *
 * @author Thomas Morgner
 */
public class ObjectStreamResolveException extends ObjectStreamException
{
  /**
   * Create an ObjectStreamException with the specified argument.
   *
   * @param classname the detailed message for the exception
   */
  public ObjectStreamResolveException (final String classname)
  {
    super(classname);
  }

  /**
   * Create an ObjectStreamException.
   */
  public ObjectStreamResolveException ()
  {
  }
}
