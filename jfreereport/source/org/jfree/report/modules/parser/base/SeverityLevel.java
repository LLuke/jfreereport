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
 * ${NAME}.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SeverityLevel.java,v 1.1 2003/08/26 17:37:28 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 25.08.2003 : Initial version
 *  
 */
package org.jfree.report.modules.parser.base;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.jfree.report.util.ObjectStreamResolveException;

public final class SeverityLevel implements Serializable
{
  public static final SeverityLevel WARNING =
      new SeverityLevel("WARNING");
  public static final SeverityLevel ERROR =
      new SeverityLevel("ERROR");
  public static final SeverityLevel FATAL_ERROR =
      new SeverityLevel("FATAL_ERROR");
  public static final SeverityLevel INFO =
      new SeverityLevel("INFO");

  private final String myName; // for debug only

  private SeverityLevel(String name)
  {
    myName = name;
  }

  public String toString()
  {
    return myName;
  }

  public boolean equals(Object o)
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

  public int hashCode()
  {
    return myName.hashCode();
  }

  /**
   * Replaces the automatically generated instance with one of the enumeration instances.
   *
   * @return the resolved element
   *
   * @throws java.io.ObjectStreamException if the element could not be resolved.
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
