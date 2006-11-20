/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ------------
 * DefaultPageContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultPageContext.java,v 1.3 2006/07/26 16:59:47 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.context;

import java.util.HashMap;
import java.util.HashSet;

import org.jfree.layouting.input.style.PageAreaType;
import org.jfree.layouting.input.style.PseudoPage;

public class DefaultPageContext implements PageContext
{
  private HashSet pseudoPages;
  private HashMap pageStyles;

  public DefaultPageContext()
  {
    this.pageStyles = new HashMap();
    this.pseudoPages = new HashSet();
  }

  public LayoutStyle getAreaDefinition(PageAreaType name)
  {
    return (LayoutStyle) pageStyles.get(name);
  }

  public void setAreaDefinition(PageAreaType area, LayoutStyle style)
  {
    pageStyles.put(area, style);
  }

  /**
   * Returns true, if the given PseudoPage identifier matches the current page
   * state.
   *
   * @return true, if the pseudopage matches, false otherwise.
   */
  public boolean isPseudoPage(PseudoPage page)
  {
    return false;
  }

  public PseudoPage[] getPseudoPages()
  {
    return (PseudoPage[]) pseudoPages.toArray(new PseudoPage[pseudoPages.size()]);
  }

  public void setPseudoPages(final PseudoPage[] pages)
  {
    pseudoPages.clear();

    for (int i = 0; i < pages.length; i++)
    {
      final PseudoPage page = pages[i];
      if (page != null)
      {
        pseudoPages.add(page);
      }
    }
  }

  public LayoutStyle getStyle()
  {
    return (LayoutStyle) pageStyles.get(PageAreaType.CONTENT);
  }
}
