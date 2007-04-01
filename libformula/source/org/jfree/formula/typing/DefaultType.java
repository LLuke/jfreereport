/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.typing;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Creation-Date: 02.11.2006, 09:37:54
 *
 * @author Thomas Morgner
 */
public abstract class DefaultType implements Type
{
  private HashSet flags;
  private HashMap properties;
  private boolean locked;

  protected DefaultType()
  {
  }

  public boolean isLocked()
  {
    return locked;
  }

  public void lock()
  {
    this.locked = true;
  }

  public void addFlag(String name)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (flags == null)
    {
      flags = new HashSet();
    }
    flags.add(name);
  }

  public boolean isFlagSet(String name)
  {
    if (flags == null)
    {
      return false;
    }
    return flags.contains(name);
  }

  public void setProperty(String name, Object value)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (properties == null)
    {
      properties = new HashMap();
    }
    properties.put(name, value);
  }

  public Object getProperty(String name)
  {
    // The type system has no properties yet. This is done later, when we
    // deal with real meta-data
    if (properties == null)
    {
      return null;
    }
    return properties.get(name);
  }
}
