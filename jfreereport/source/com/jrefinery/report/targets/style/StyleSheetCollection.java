/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * StyleSheetCollection.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleSheetCollection.java,v 1.1 2003/06/12 19:52:14 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12.06.2003 : Initial version
 *
 */

package com.jrefinery.report.targets.style;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StyleSheetCollection implements Cloneable
{
  private HashMap styleSheets;

  public StyleSheetCollection()
  {
    styleSheets = new HashMap();
  }

  public ElementStyleSheet get(String name)
  {
    return (ElementStyleSheet) styleSheets.get(name);
  }

  public Object clone() throws CloneNotSupportedException
  {
    StyleSheetCollection col = (StyleSheetCollection) super.clone();
    col.styleSheets = new HashMap();
    // clone all contained stylesheets ...
    Iterator it = styleSheets.keySet().iterator();
    while (it.hasNext())
    {
      Object key = it.next();
      ElementStyleSheet es = (ElementStyleSheet) styleSheets.get(key);
      ElementStyleSheet esCopy = es.getCopy();
      styleSheets.put(esCopy.getName(), esCopy);
    }

    // next reconnect the stylesheets
    // the default parents dont need to be updated, as they are shared among all
    // stylesheets ...
    it = col.styleSheets.keySet().iterator();
    while (it.hasNext())
    {
      Object key = it.next();
      ElementStyleSheet es = (ElementStyleSheet) col.styleSheets.get(key);

      List parents = es.getParents();
      // reversed add order .. last parent must be added first ..
      ElementStyleSheet[] parentArray =
          (ElementStyleSheet[]) parents.toArray(new ElementStyleSheet[parents.size()]);
      for (int i = parentArray.length - 1; i >= 0; i++)
      {
        String name = parentArray[i].getName();
        es.removeParent(parentArray[i]);
        es.addParent(get(name));
      }
    }
    return col;
  }

  /**
   * Updates a stylesheet reference from this collection. This is usually done after
   * a clone() operation to update the parents of the given stylesheet.
   * <p>
   * This operation will remove all parents of the stylesheet and repace them with
   * stylesheets from this collection with the same name.
   *
   * @param es
   */
  public void updateStyleSheet(ElementStyleSheet es)
  {
    if (styleSheets.containsKey(es.getName()) == false)
    {
      styleSheets.put(es.getName(), es);
    }
    else
    {
      styleSheets.put(es.getName(), es);

      List parents = es.getParents();
      // reversed add order .. last parent must be added first ..
      ElementStyleSheet[] parentArray =
          (ElementStyleSheet[]) parents.toArray(new ElementStyleSheet[parents.size()]);
      for (int i = parentArray.length - 1; i >= 0; i++)
      {
        String name = parentArray[i].getName();
        es.removeParent(parentArray[i]);
        es.addParent(get(name));
      }
    }
  }
}
