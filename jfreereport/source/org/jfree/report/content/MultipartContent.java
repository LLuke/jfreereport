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
 * MultipartContent.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: MultipartContent.java,v 1.2 2004/05/07 08:02:49 mungady Exp $
 *
 * Changes 
 * -------------------------
 * 27.03.2004 : Initial version
 *  
 */

package org.jfree.report.content;

public interface MultipartContent extends Content
{
  /**
   * Returns the number of sub-content items for this item. <P> Only subclasses of {@link
   * org.jfree.report.content.ContentContainer} will return non-zero results.
   *
   * @return the number of sub-content items.
   */
  public int getContentPartCount ();

  /**
   * Returns a sub-content item.
   *
   * @param part the sub-content index (zero-based).
   * @return the subcontent (possibly null).
   */
  public Content getContentPart (int part);
}
