/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * AnchorContentFactoryModule.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.content;

import org.jfree.report.Anchor;
import org.jfree.report.AnchorElement;
import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictPoint;

public class AnchorContentFactoryModule implements ContentFactoryModule
{
  public AnchorContentFactoryModule ()
  {
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType the content type.
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent (final String contentType)
  {
    return contentType.equals(AnchorElement.CONTENT_TYPE);
  }

  /**
   * Creates content for an element.
   *
   * @param e      the element.
   * @param bounds the bounds.
   * @param ot     the output target.
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the Content creation.
   */
  public Content createContentForElement (final Element e,
                                          final ElementLayoutInformation bounds,
                                          final LayoutSupport ot)
          throws ContentCreationException
  {
    final StrictPoint absPos = bounds.getAbsolutePosition();
    final Object o = e.getValue();
    return createAnchor(o, absPos);
  }

  public static Content createAnchor (final Object o, final StrictPoint position)
  {
    if (o instanceof Anchor)
    {
      return new AnchorContent((Anchor) o, position);
    }
    else if (o != null)
    {
      final Anchor a = new Anchor(String.valueOf(o));
      return new AnchorContent(a, position);
    }
    else
    {
      return EmptyContent.getDefaultEmptyContent();
    }
  }
}
