/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * ContentType.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ContentType.java,v 1.4 2002/12/16 17:31:01 mungady Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.content;

/**
 * A class for representing a content type.  The four predefined content types are:
 *
 * <ul>
 * <li><code>TEXT</code>;</li>
 * <li><code>SHAPE</code>;</li>
 * <li><code>IMAGE</code>;</li>
 * <li><code>CONTAINER</code>;</li>
 * </ul>
 *
 * @see com.jrefinery.report.targets.base.content.Content
 *
 * @author Thomas Morgner
 */
public class ContentType
{
  /** Text content type. */
  public static final ContentType TEXT = new ContentType("Text");

  /** Shape content type. */
  public static final ContentType SHAPE = new ContentType("Shape");

  /** Image content type. */
  public static final ContentType IMAGE = new ContentType("Image");

  /** Container content type. */
  public static final ContentType CONTAINER = new ContentType("Container");

  /** The content name (for debug purposes). */
  private final String myName;

  /**
   * Creates a new content type.
   * <p>
   * This constructor is private, so you can't use it directly.
   *
   * @param name  the name.
   */
  private ContentType(String name)
  {
    myName = name;
  }

  /**
   * Returns the name of the content type.
   *
   * @return the name of the content type.
   */
  public String toString()
  {
    return myName;
  }

}
