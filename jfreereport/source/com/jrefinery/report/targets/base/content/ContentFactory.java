/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * -------------------
 * ContentFactory.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ContentFactory.java,v 1.3 2003/02/27 10:35:38 mungady Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.layout.LayoutSupport;

/**
 * The content factory is resopnsible for creating content from a given element
 * and the rawdata contained in the element's datasource.
 *
 * @author Thomas Morgner
 */
public interface ContentFactory
{
  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType  the content type.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent(String contentType);

  /**
   * Creates content for an element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the Content creation.
   */
  public Content createContentForElement(Element e, ElementLayoutInformation bounds,
                                         LayoutSupport ot)
      throws ContentCreationException;

}
