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
package org.jfree.layout.style;

public class ElementPosition
{
  /**
   * The box's position (and possibly size) is specified with the 'left',
   * 'right', 'top', and 'bottom' properties. These properties specify offsets
   * with respect to the box's containing block. Absolutely positioned boxes
   * are taken out of the normal flow. This means they have no impact on the
   * layout of later siblings. Also, though absolutely positioned boxes have
   * margins, they do not collapse with any other margins.
   */
  public static final ElementPosition ABSOLUTE = new ElementPosition("ABSOLUTE");

  /**
   * The box's position is calculated according to the 'absolute' model, but in
   * addition, the box is fixed with respect to some reference. In the case of
   * continuous media, the box is fixed with respect to the viewport (and doesn't
   * move when scrolled). In the case of paged media, the box is fixed with
   * respect to the page, even if that page is seen through a viewport
   * (in the case of a print-preview, for example). Authors may wish to specify
   * 'fixed' in a media-dependent way. For instance, an author may want a box
   * to remain at the top of the viewport on the screen, but not at the top of
   * each printed page. The two specifications may be separated by using an
   * \@media rule, as in:
   */
  public static final ElementPosition FIXED = new ElementPosition("FIXED");


  /**
   * The box's position is calculated according to the normal flow (this is
   * called the position in normal flow). Then the box is offset relative to
   * its normal position. When a box B is relatively positioned, the position
   * of the following box is calculated as though B were not offset.
   */
  public static final ElementPosition RELATIVE = new ElementPosition("RELATIVE");

  /**
   * The box is a normal box, laid out according to the normal flow. The 'left'
   * and 'top' properties do not apply.
   */
  public static final ElementPosition STATIC = new ElementPosition("STATIC");

  private final String myName;

  private ElementPosition (final String name)
  {
    myName = name;
  }

  public String toString ()
  {
    return myName;
  }

  public int hashCode ()
  {
    return myName.hashCode();
  }
}
