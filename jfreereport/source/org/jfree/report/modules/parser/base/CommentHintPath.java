/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * CommentHintPath.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CommentHintPath.java,v 1.8 2005/02/19 13:30:03 taqua Exp $
 *
 * Changes
 * -------------------------
 * 21-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.parser.base;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The comment hint path is used to mark the parse position and to store the comments for
 * such an parser path.
 *
 * @author Thomas Morgner
 */
public final class CommentHintPath implements Serializable, Cloneable
{
  /**
   * the list of name segments.
   */
  private ArrayList nameElements;

  /**
   * Creates a new empty comment hint path object.
   */
  public CommentHintPath ()
  {
    nameElements = new ArrayList();
  }

  /**
   * Creates a new comment hint path and adds the given object as first name segment.
   *
   * @param name the first name segment.
   */
  public CommentHintPath (final Object name)
  {
    this();
    addName(name);
  }

  /**
   * Creates a new comment hint path and adds the given objects as name segments.
   *
   * @param name the name segments.
   */
  public CommentHintPath (final Object[] name)
  {
    this();
    for (int i = 0; i < name.length; i++)
    {
      addName(name[i]);
    }
  }

  /**
   * Appends the given object as last element to the path.
   *
   * @param name the new path segment.
   */
  public void addName (final Object name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    nameElements.add(name);
  }

  /**
   * Returns a copy of this comment hint path.
   *
   * @return a copy of this path.
   */
  public CommentHintPath getInstance ()
  {
    final CommentHintPath hint = new CommentHintPath();
    hint.nameElements = new ArrayList(nameElements);
    return hint;
  }

  /**
   * Tests, whether this object is equal to the given object.
   *
   * @param o the object that should be compared.
   * @return true, if the other object is equal, false otherwise.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof CommentHintPath))
    {
      return false;
    }

    final CommentHintPath commentHintPath = (CommentHintPath) o;

    if (!nameElements.equals(commentHintPath.nameElements))
    {
      return false;
    }

    return true;
  }

  /**
   * Computes a hashcode for this path.
   *
   * @return a hashcode for this object.
   *
   * @see java.lang.Object#hashCode()
   */
  public int hashCode ()
  {
    return nameElements.hashCode();
  }

  /**
   * Returns a string representation of this object for debugging purposes.
   *
   * @return the object as string.
   *
   * @see java.lang.Object#toString()
   */
  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("CommentHintPath={");
    b.append(nameElements);
    b.append("}");
    return b.toString();
  }

  public Object clone ()
          throws CloneNotSupportedException
  {
    final CommentHintPath hp = (CommentHintPath) super.clone();
    hp.nameElements = (ArrayList) nameElements.clone();
    return hp;
  }
}
