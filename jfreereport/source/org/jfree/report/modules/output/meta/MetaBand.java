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
 * MetaBand.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 14.02.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.meta;

import org.jfree.report.content.Content;
import org.jfree.report.style.ElementStyleSheet;

public class MetaBand extends MetaElement
{
  private MetaElement[] elements;
  private boolean spooled;

  public MetaBand(Content elementContent, ElementStyleSheet style, MetaElement[] elements, boolean spool)
  {
    super(elementContent, style);
    this.elements = elements;
    this.spooled = spool;
  }

  public MetaElement getElementAt (int i)
  {
    return elements[i];
  }

  public int getElementCount ()
  {
    return elements.length;
  }

  public MetaElement[] toArray ()
  {
    MetaElement[] newElements = new MetaElement[elements.length];
    System.arraycopy(elements, 0, newElements, 0, elements.length);
    return newElements;
  }

  public boolean isSpooled()
  {
    return spooled;
  }


  public String toString ()
  {
    StringBuffer s = new StringBuffer();
    s.append("MetaBand={size=");
    s.append(elements.length);
    s.append(", spooled=");
    s.append(spooled);
    s.append(", bounds=");
    s.append(getBounds());

    for (int i = 0; i < elements.length; i++)
    {
      s.append(",\n");
      s.append("{");
      s.append(elements[i]);
      s.append("}");

    }
    s.append("}");
    return s.toString();
  }
}
