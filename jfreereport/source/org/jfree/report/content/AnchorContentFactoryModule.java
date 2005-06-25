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
 * $Id: AnchorContentFactoryModule.java,v 1.3 2005/03/03 22:59:58 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jul-2005: Added JavaDoc comments.
 */
package org.jfree.report.content;

import org.jfree.report.Anchor;
import org.jfree.report.AnchorElement;
import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictPoint;

/**
 * The anchor content factory-module is responsible for creating the AnchorContent from
 * an element containing Anchors. The factory module expects either an Anchor element in
 * the given element or converts the content of the element into a String and uses this
 * string as Anchor value.
 *
 * @author Thomas Morgner
 * @see Anchor
 */
public class AnchorContentFactoryModule implements ContentFactoryModule
{
  /**
   * Creates a new FactoryModule.
   */
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

  /**
   * Creates an anchor content wrapper for the given object. If the object is an instance
   * of Anchor, the content is generated directly, else the object's string representation
   * is used to construct a new Anchor object.
   *
   * @param o the anchor or an arbitary object that should be converted into an Anchor.
   * @param position the position of the anchor
   * @return the created content
   */
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
