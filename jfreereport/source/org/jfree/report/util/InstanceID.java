/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.util;

import java.io.Serializable;

/**
 * This class can be used as ID to mark instances of objects. This allows to track and
 * identify objects and their clones.
 *
 * @author Thomas Morgner
 */
public final class InstanceID implements Serializable
{
  /**
   * DefaultConstructor.
   */
  public InstanceID ()
  {
  }

  /**
   * Returns a simple string representation of this object to make is identifiable by
   * human users.
   *
   * @return the string representation.
   */
  public String toString ()
  {
    return "InstanceID[" + hashCode() + "]";
  }
}
