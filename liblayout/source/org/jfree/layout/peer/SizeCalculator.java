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

package org.jfree.layout.peer;

/**
 * The interface for an class that is able to calculate the width of a given string, and the
 * height of a line of text.  The calculations rely on state information (e.g. font size,
 * graphics device, etc) maintained by the calculator.
 * <p>
 * Every {@link LayoutSupport} can create an instance of
 * a class that implements this interface,
 * via the {@link LayoutSupport#createTextSizeCalculator}
 * method.
 *
 * @author Thomas Morgner
 */
public interface SizeCalculator
{
  /**
   * Calculates the width of a <code>String<code> in the current <code>Graphics</code> context.
   *
   * @param text the text.
   * @param lineStartPos the start position of the substring to be measured.
   * @param endPos the position of the last character to be measured.
   *
   * @return the width of the string in Java2D units.
   */
  public float getStringWidth(String text, int lineStartPos, int endPos);

  /**
   * Returns the line height.  This includes the font's ascent, descent and leading.
   *
   * @return the line height.
   */
  public float getLineHeight();

}
