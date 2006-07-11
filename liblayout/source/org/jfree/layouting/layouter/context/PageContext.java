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
 * PageContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PageContext.java,v 1.2 2006/04/17 20:51:17 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.context;

import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.renderer.page.PageAreaType;

/**
 * The page context describes a logical page. The page holds the first and
 * the last value of each counter and string. It also grants access to the
 * global pseudo-page definitions. (Note: This is unclear and unclean and thus
 * not yet implemented. Later it will be, of course...)
 * <p/>
 * The pseudo-page definitions only have a sensible meaning, if the page is
 * not spanned.
 * <p/>
 * Note: The 'page' counter is not managed by this class. That counter is
 * always managed by the layouter itself and *should* reflect the actual
 * physical position (even on spanned pages!)
 */
public interface PageContext
{
  public LayoutStyle getAreaDefinition (PageAreaType name);

  /**
   * Returns true, if the given PseudoPage identifier matches the current page
   * state.
   *
   * @return true, if the pseudopage matches, false otherwise.
   */
  public boolean isPseudoPage (PseudoPage page);
}
