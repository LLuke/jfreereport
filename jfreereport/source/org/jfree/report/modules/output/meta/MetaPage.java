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
 * MetaPage.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
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

import org.jfree.report.util.Log;

public class MetaPage
{
  private MetaBand[] bands;
  private boolean empty;

  public MetaPage(MetaBand[] bands)
  {
    this.bands = bands;
    this.empty = computeEmpty(bands);
    Log.debug (this);
  }

  public static boolean computeEmpty (MetaElement[] elements)
  {
    for (int i = 0; i < elements.length; i++)
    {

      if (elements[i] instanceof MetaBand)
      {
        MetaBand b = (MetaBand) elements[i];
        if (b.isSpooled() == false)
        {
          return false;
        }
      }
    }
    return true;
  }

  public MetaBand[] getBands()
  {
    return bands;
  }

  public boolean isEmpty()
  {
    return empty;
  }

  public String toString ()
  {
    StringBuffer s = new StringBuffer();
    s.append("MetaPage={size=");
    s.append(bands.length);

    for (int i = 0; i < bands.length; i++)
    {
      s.append(",\n");
      s.append("{");
      s.append(bands[i]);
      s.append("}");
    }
    s.append("}");
    return s.toString();
  }
}
