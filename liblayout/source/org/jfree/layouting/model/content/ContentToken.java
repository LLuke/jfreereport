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
 * ContentToken.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.model.content;

/**
 * This is the base for all specified content in CSS files.
 * This both may lookup content from elsewhere or may hold
 * static content as string.
 */
public interface ContentToken
{
  /** Plain text */
  public static final int STRING_CONTENT = 1;
  /**
   * The defined child elements, if any.
   * This is the default for all elements.
   */
  public static final int CONTENTS_CONTENT = 2;

  /**
   * Images, drawables or something for which we can create an InputFeed.
   * (Hey, forget that with the input feed for now, lets concentrate on
   * image handling first).
   */
  public static final int RESOURCE_CONTENT = 3;

  /**
   * Something I cannot interpret. Maybe the output target is smarter than
   * I am. We pass this through without touching it.
   */
  public static final int EXTERNAL_CONTENT = 4;
  public static final int OPEN_QUOTE = 5;
  public static final int CLOSE_QUOTE = 6;

  public int getType();
}
