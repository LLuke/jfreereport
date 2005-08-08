/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * LocalImageContainer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: LocalImageContainer.java,v 1.4 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.03.2004 : Initial version
 *
 */

package org.jfree.report;

import java.awt.Image;

/**
 * The LocalImageContainer makes the image available as 'java.awt.Image' instance.
 * This way, the image can be included in the local content creation process.
 *
 * @author Thomas Morgner
 */
public interface LocalImageContainer extends ImageContainer
{
  /**
   * Returns the image instance for this image container.
   * This method might return <code>null</code>, if the image is
   * not available.
   *
   * @return the image data.
   */
  public Image getImage ();

  /**
   * Returns the name of this image reference. The name returned should be unique.
   *
   * @return the name.
   */
  public String getName ();

  /**
   * Checks, whether this image has a assigned identity. Two identities should be equal,
   * if the image contents are equal.
   *
   * @return true, if that image contains contains identity information, false otherwise.
   */
  public boolean isIdentifiable ();

  /**
   * Returns the identity information.
   *
   * @return the image identifier.
   */
  public Object getIdentity ();
}
