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
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Boot.java,v 1.6 2003/11/23 16:50:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-May-2004 : Initial version
 *
 */
package org.jfree.layout.doc;

import java.util.ArrayList;

import org.jfree.layout.style.BasicStyleSheet;
import org.jfree.layout.style.StyleSheet;

public abstract class AbstractDocumentElement implements DocumentElement
{
  private ArrayList childs;
  private StyleSheet style;
  private DocumentNode parent;
  private String name;

  public AbstractDocumentElement ()
  {
    this (null);
  }

  public AbstractDocumentElement (final String name)
  {
    this.childs = new ArrayList();
    this.name = name;
    this.style = new BasicStyleSheet(name);
  }

  public void addChild (final AbstractDocumentElement child)
  {
    // fixme: Add extended checks to prevent loops ...
    childs.add (child);
    child.setParent(this);
  }

  public DocumentNode getChild (final int i)
  {
    return (DocumentNode) childs.get(i);
  }

  public int getChildCount ()
  {
    return childs.size();
  }

  public DocumentNode[] getChilds ()
  {
    return (DocumentNode[]) childs.toArray(new DocumentNode[childs.size()]);
  }

  public void setName (final String name)
  {
    this.name = name;
  }

  public String getName ()
  {
    return name;
  }

  public StyleSheet getStyle ()
  {
    return style;
  }

  public void setParent (final DocumentNode parent)
  {
    this.parent = parent;
  }

  public DocumentNode getParent ()
  {
    return parent;
  }

  public String toString ()
  {
    return "AbstractDocumentElement{" +
            "name='" + name + "'" +
            ", childCount=" + childs.size() +
            "}";
  }
}
