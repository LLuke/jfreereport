/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: ContentType.java,v 1.6 2005/01/24 23:58:10 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.content;

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
 * @see org.jfree.report.content.Content
 *
 * @author Thomas Morgner
 */
public final class ContentType
{
  /** Anchor content type. */
  public static final ContentType ANCHOR = new ContentType("Anchor");

  /** Text content type. */
  public static final ContentType TEXT = new ContentType("Text");

  /** Shape content type. */
  public static final ContentType SHAPE = new ContentType("Shape");

  /** Image content type. */
  public static final ContentType IMAGE = new ContentType("Image");

  /** Drawable content type. */
  public static final ContentType DRAWABLE = new ContentType("Drawable");

  /** Container content type. */
  public static final ContentType CONTAINER = new ContentType("Container");

  /**
   * Raw content type. This denotes a user defined content type, usually
   * a intermediate product while producing the physical content.
   */
  public static final ContentType RAW = new ContentType("Raw");

  /** The content name (for debug purposes). */
  private final String myName;

  /**
   * Creates a new content type.
   * <p>
   * This constructor is private, so you can't use it directly.
   *
   * @param name  the name.
   */
  private ContentType(final String name)
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
