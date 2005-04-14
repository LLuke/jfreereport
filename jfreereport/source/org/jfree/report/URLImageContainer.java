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
 * URLImageContainer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: URLImageContainer.java,v 1.5 2005/03/04 12:08:16 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.03.2004 : Initial version
 *
 */

package org.jfree.report;

import java.net.URL;

/**
 * An image container, that references a remote image. The image is not
 * required to be loadable as long as this container returns all necessary
 * information correctly.
 * <p>
 * Some output targets will not display anything, if the image is not loadable.
 *
 * @author Thomas Morgner
 */
public interface URLImageContainer extends ImageContainer
{
  /**
   * Returns the source URL, if available.
   *
   * @return the source URL of the image.
   */
  public URL getSourceURL ();

  /**
   * Returns the source URL as string. This could also be a relative URL
   * which is not readable by the report processor, the source URL string
   * is copied as is - without being interpreted by the output target.
   *
   * @return the source URL as string.
   */
  public String getSourceURLString ();

  /**
   * Defines, whether the given URLs are readable. If there is no java.net.URL
   * sourceURL, then this method must return false.
   *
   * @return true, if the source URL is loadable, false otherwise.
   */
  public boolean isLoadable ();
}
