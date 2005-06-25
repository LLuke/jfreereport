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
 * -------------------------
 * ContentFactoryModule.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ContentFactoryModule.java,v 1.5 2005/02/23 21:04:36 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version
 */
package org.jfree.report.content;

import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;

/**
 * The ContentFactoryModule is a delegator for the DefaultContentFactory. This module is
 * able to create a certain content type from the given element's data.
 *
 * @author Thomas Morgner
 * @see DefaultContentFactory
 */
public interface ContentFactoryModule
{
  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType the content type.
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent (String contentType);

  /**
   * Creates content for an element.
   *
   * @param e      the element.
   * @param bounds the bounds.
   * @param ot     the output target.
   * @return the content, never null.
   *
   * @throws ContentCreationException if there is a problem with the Content creation.
   */
  public Content createContentForElement (Element e,
                                          ElementLayoutInformation bounds,
                                          LayoutSupport ot)
          throws ContentCreationException;

}
