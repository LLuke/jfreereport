/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -----------------------------
 * TextContentFactoryModule.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextContentFactoryModule.java,v 1.2 2003/08/24 15:13:21 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version, extracted from OperationFactoryModule
 */
package org.jfree.report.content;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.layout.SizeCalculatorException;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.ui.FloatDimension;

/**
 * The TextContentFactoryModule creates plain text content from the given element.
 * The content type of the used element should be "text/plain".
 *
 * @author Thomas Morgner
 */
public class TextContentFactoryModule implements ContentFactoryModule
{
  /**
   * creates a new TextContentFactoryModule.
   */
  public TextContentFactoryModule()
  {
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise. This implementation is able to handle the type
   * &quot;text/plain&quot;.
   *
   * @param contentType  the content type.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent(final String contentType)
  {
    return contentType.equalsIgnoreCase("text/plain");
  }

  /**
   * Creates a content wrapper for the element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem creating the content.
   */
  public Content createContentForElement(final Element e, final ElementLayoutInformation bounds,
                                         final LayoutSupport ot)
      throws ContentCreationException
  {
    final String text = (String) e.getValue();
    if (text == null)
    {
      return null;
    }
    if (text.length() == 0)
    {
      return null;
    }

    final Point2D point = bounds.getAbsolutePosition();

    // TextElement has a defined width (Max(MinSize, PrefSize).
    // and a maximum height (Min(MaxSize, PrefSize).

    final Dimension2D prefSize = bounds.getPreferredSize();
    double width = bounds.getMinimumSize().getWidth();
    double height = bounds.getMaximumSize().getHeight();
    if (prefSize != null)
    {
      width = Math.max(prefSize.getWidth(), width);
      height = Math.min(prefSize.getHeight(), height);
    }
    final Dimension2D dim = new FloatDimension((float) width, (float) height);

    if (dim.getWidth() == 0 && dim.getHeight() == 0)
    {
      return null;
    }

    final FontDefinition f = e.getStyle().getFontDefinitionProperty();
    final Rectangle2D tBounds = new Rectangle2D.Float((float) point.getX(),
        (float) point.getY(),
        (float) dim.getWidth(),
        (float) dim.getHeight());

    final Float lh = (Float) e.getStyle().getStyleProperty(ElementStyleSheet.LINEHEIGHT);
    try
    {
      final String reservedLiteral = (String) e.getStyle().getStyleProperty
          (ElementStyleSheet.RESERVED_LITERAL, "..");

      final TextContent tc = new TextContent(text, lh.floatValue(), tBounds,
          ot.createTextSizeCalculator(f), reservedLiteral);
      return tc;
    }
    catch (SizeCalculatorException se)
    {
      throw new ContentCreationException("Failed to create the TextSizeCalculator", se);
    }
  }
}
