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

/**
 * This class filters linebreaks in addition to the normal whitespace
 * collapsing.
 *
 * @author Thomas Morgner
 */
public class CollapseWhiteSpaceFilter extends PreserveBreaksWhiteSpaceFilter
{
  public CollapseWhiteSpaceFilter()
  {
  }

  protected boolean isLinebreak(int codepoint)
  {
    // we do not detect any linebreaks. They will be handled as if they were
    // ordinary whitespaces
    return false;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

}
