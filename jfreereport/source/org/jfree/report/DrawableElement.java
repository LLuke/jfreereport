/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * --------------------
 * DrawableElement.java
 * --------------------
 * (C)opyright 2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: DrawableElement.java,v 1.4 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------
 * 04-Mar-2003 : Initial version
 *
 */

package org.jfree.report;

/**
 * The element returns a drawable container. Drawable elements always scale without
 * obeying to an aspect ratio. It is the resposibility of the drawable implementation to
 * implement such rules.
 *
 * @author Thomas Morgner
 */
public class DrawableElement extends Element
{
  /**
   * The content type for the element.
   */
  public static final String CONTENT_TYPE = "drawable/generic";

  /**
   * Default constructor.
   */
  public DrawableElement ()
  {
  }

  /**
   * Defines the content-type for this element.
   *
   * @return the content-type as string.
   */
  public String getContentType ()
  {
    return CONTENT_TYPE;
  }
}
