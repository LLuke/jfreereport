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
 * ---------
 * Anchor.java
 * ---------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: Anchor.java,v 1.2 2005/02/23 19:31:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-02-01 : Initial version
 */
package org.jfree.report;

import java.io.Serializable;

/**
 * An anchor is a possible target for external hyperlinks.
 * <p/>
 * In HTML anchors would be produced by using &lt;a name=&quot;anchorname&quot;&gt;. This
 * class is immutable.
 *
 * @author Thomas Morgner
 * @see AnchorElement
 */
public class Anchor implements Serializable
{
  /**
   * The name of the anchor. Should be unique.
   */
  private String name;

  /**
   * Creates a new anchor with the given name.
   *
   * @param name the name of the anchor.
   * @throws NullPointerException if the given name is null.
   */
  public Anchor (final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
  }

  /**
   * Returns the name of the anchor.
   *
   * @return the name
   */
  public String getName ()
  {
    return name;
  }

  /**
   * Checks, whether the given object is an anchor with the same name as this one.
   *
   * @param o the other object.
   * @return true, if the object is equal to this one, false otherwise.
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof Anchor))
    {
      return false;
    }

    final Anchor anchor = (Anchor) o;

    if (!name.equals(anchor.name))
    {
      return false;
    }

    return true;
  }

  /**
   * Computes a hashcode for this anchor.
   *
   * @return the hashcode.
   */
  public int hashCode ()
  {
    return name.hashCode();
  }
}
