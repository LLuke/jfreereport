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
 * ContentValues.java
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
package org.jfree.layouting.input.style.keys.content;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 01.12.2005, 17:50:36
 *
 * @author Thomas Morgner
 */
public class ContentValues extends CSSConstant
{
  public static final ContentValues OPEN_QUOTE = new ContentValues("open-quote");
  public static final ContentValues CLOSE_QUOTE = new ContentValues("close-quote");
  public static final ContentValues NO_OPEN_QUOTE = new ContentValues("no-open-quote");
  public static final ContentValues NO_CLOSE_QUOTE = new ContentValues("no-close-quote");

  public static final ContentValues CONTENTS = new ContentValues("contents");
  public static final ContentValues NORMAL = new ContentValues("normal");
  public static final ContentValues NONE = new ContentValues("none");
  public static final ContentValues INHIBIT = new ContentValues("inhibit");

  public static final ContentValues FOOTNOTE = new ContentValues("footnote");
  public static final ContentValues ENDNOTE = new ContentValues("endnote");
  public static final ContentValues SECTIONNOTE = new ContentValues("sectionote");
  public static final ContentValues LISTITEM = new ContentValues("list-item");
  public static final ContentValues DOCUMENT_URL = new ContentValues("document-url");

  private ContentValues (final String name)
  {
    super (name);
  }
}
