/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.text.whitespace;

import org.jfree.util.PublicCloneable;

/**
 * Creation-Date: 11.06.2006, 19:19:44
 *
 * @author Thomas Morgner
 */
public interface WhiteSpaceFilter extends PublicCloneable
{
  public static final int STRIP_WHITESPACE = -1;

  /**
   * Filters the whitespaces. This method returns '-1', if the whitespace
   * should be removed from the stream; otherwise it presents a replacement
   * character. If the codepoint is no whitespace at all, the codepoint is
   * returned unchanged.
   *
   * @param codepoint
   * @return
   */
  public int filter (int codepoint);

  /**
   * Reset the filter to the same state as if the filter had been constructed
   * but not used yet.
   */
  public void reset();
}
