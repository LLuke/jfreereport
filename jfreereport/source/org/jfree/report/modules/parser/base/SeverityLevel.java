/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * ${NAME}.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: SeverityLevel.java,v 1.4 2003/11/07 18:33:56 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 25-Aug-2003 : Initial version
 *  
 */
package org.jfree.report.modules.parser.base;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.jfree.report.util.ObjectStreamResolveException;

/**
 * An enumeration class to represent the severity level of an operation result.
 * 
 * @author Thomas Morgner
 */
public final class SeverityLevel implements Serializable
{
  /** Represents warning messages. */
  public static final SeverityLevel WARNING =
      new SeverityLevel("WARNING");
  /** Represents error messages. */
  public static final SeverityLevel ERROR =
      new SeverityLevel("ERROR");
  /** Represents fatal parse error messages. */
  public static final SeverityLevel FATAL_ERROR =
      new SeverityLevel("FATAL_ERROR");
  /** Represents informational messages. */
  public static final SeverityLevel INFO =
      new SeverityLevel("INFO");

  /** a string representation of this severity level. */
  private final String myName; // for debug only

  /**
   * Creates a new the severity level instance.
   * @param name the name of the severity level.
   */
  private SeverityLevel(final String name)
  {
    myName = name;
  }

  /**
   * Returns the string representation of this severity level object. 
   * @see java.lang.Object#toString()
   * 
   * @return a string representing this object.
   */
  public String toString()
  {
    return myName;
  }

  /**
   * Compares the severity level for equality with the given object. 
   * @see java.lang.Object#equals(java.lang.Object)
   * 
   * @param o the other object that should be compared.
   * @return true, if both objects are equal, false otherwise.
   */
  public boolean equals(final Object o)
  {
    if (this == o)
    { 
      return true;
    }
    
    if (!(o instanceof SeverityLevel))
    { 
      return false;
    }

    final SeverityLevel severityLevel = (SeverityLevel) o;

    if (!myName.equals(severityLevel.myName))
    { 
      return false;
    }

    return true;
  }

  /**
   * Computes an hashcode for this level object.  
   * @see java.lang.Object#hashCode()
   * 
   * @return the hashcode.
   */
  public int hashCode()
  {
    return myName.hashCode();
  }

  /**
   * Replaces the automatically generated instance with one of the 
   * enumeration instances.
   *
   * @return the resolved element
   * @throws ObjectStreamException if the element could not be resolved.
   */
  protected Object readResolve() throws ObjectStreamException
  {
    if (this.equals(SeverityLevel.ERROR))
    {
      return SeverityLevel.ERROR;
    }
    if (this.equals(SeverityLevel.WARNING))
    {
      return SeverityLevel.WARNING;
    }
    if (this.equals(SeverityLevel.FATAL_ERROR))
    {
      return SeverityLevel.FATAL_ERROR;
    }
    if (this.equals(SeverityLevel.INFO))
    {
      return SeverityLevel.INFO;
    }
    // unknown element alignment...
    throw new ObjectStreamResolveException();
  }

}
