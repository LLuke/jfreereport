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
 * CommentHintPath.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CommentHintPath.java,v 1.1 2003/07/21 20:47:49 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 21.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.base;

import java.io.Serializable;
import java.util.ArrayList;

public final class CommentHintPath implements Serializable, Cloneable
{
  private ArrayList nameElements;

  public CommentHintPath()
  {
    nameElements = new ArrayList();
  }

  public CommentHintPath(Object name)
  {
    this();

    addName (name);
  }

  public CommentHintPath(Object[] name)
  {
    this();
    for (int i = 0; i < name.length; i++)
    {
      addName(name[i]);
    }
  }

  public void addName (Object name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    nameElements.add (name);
  }

  public CommentHintPath getInstance()
  {
    CommentHintPath hint = new CommentHintPath();
    hint.nameElements = new ArrayList(nameElements);
    return hint;
  }

  public boolean equals(Object o)
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

  public int hashCode()
  {
    return nameElements.hashCode();
  }

  public String toString()
  {
    StringBuffer b = new StringBuffer();
    b.append("CommentHintPath={");
    b.append(nameElements);
    b.append("}");
    return b.toString();
  }
}
