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
 * AligningMetaBandProducer.java
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
package org.jfree.report.modules.output.pageable.base.operations;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.content.Content;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.content.MultipartContent;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.modules.output.meta.MetaBandProducer;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;

public class AligningMetaBandProducer extends MetaBandProducer
{
  public AligningMetaBandProducer (final LayoutSupport support)
  {
    super(support);
  }

  protected Content createContent (final Element e,
                                   final ElementStyleSheet styleSheet)
          throws ContentCreationException
  {
    final StrictBounds bounds = (StrictBounds)
            styleSheet.getStyleProperty(ElementStyleSheet.BOUNDS);
    final ElementAlignment hAlign = (ElementAlignment)
            styleSheet.getStyleProperty(ElementStyleSheet.ALIGNMENT);
    final ElementAlignment vAlign = (ElementAlignment)
            styleSheet.getStyleProperty(ElementStyleSheet.VALIGNMENT);
    final LayoutSupport support = getLayoutSupport();
    final ElementLayoutInformation eli = new ElementLayoutInformation(bounds);
    final Content content =
             support.getContentFactory().createContentForElement(e, eli, support);
    return alignContent(content, bounds, hAlign, vAlign);
  }

  /**
   * The content is already positioned on the page (but top/left aligned).
   * Now, move the content to the location as defined by the ElementAlignment.
   * <p>
   * HackAttack: This method does not adjust the width and height of the element;
   * we have to trust the code.
   *
   * @param content
   * @param bounds
   * @param hAlign
   * @param vAlign
   * @return
   */
  protected Content alignContent (final Content content,
                                  final StrictBounds bounds,
                                  final ElementAlignment hAlign,
                                  final ElementAlignment vAlign)
  {
    final VerticalBoundsAlignment vba = AlignmentTools.getVerticalLayout(vAlign, bounds);
    final StrictBounds minimumContentSize = content.getMinimumContentSize();
    final StrictBounds cb = vba.align((StrictBounds) minimumContentSize.clone());
    final long verticalShift = cb.getY() - minimumContentSize.getY();
    content.translate(0, verticalShift);
    return alignHorizontalContent(content, bounds, hAlign);
  }
  
  protected Content alignHorizontalContent (final Content content,
                                            final StrictBounds bounds,
                                            final ElementAlignment hAlign)
  {
    final HorizontalBoundsAlignment hba = AlignmentTools.getHorizontalLayout(hAlign, bounds);
    final StrictBounds minimumContentSize = content.getMinimumContentSize();
    final StrictBounds cb = hba.align((StrictBounds) minimumContentSize.clone());
    final long horizontalShift = cb.getX() - minimumContentSize.getX();
    content.translate(horizontalShift, 0);
    if (content instanceof MultipartContent)
    {
      final MultipartContent mp = (MultipartContent) content;
      final int size = mp.getContentPartCount();
      for (int i = 0; i < size; i++)
      {
        alignHorizontalContent(mp.getContentPart(i), bounds, hAlign);
      }
    }
    return content;
  }
}
