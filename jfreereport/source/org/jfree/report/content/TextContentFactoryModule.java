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
 * -----------------------------
 * TextContentFactoryModule.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TextContentFactoryModule.java,v 1.10 2005/02/19 13:29:52 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version, extracted from OperationFactoryModule
 */
package org.jfree.report.content;

import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.layout.SizeCalculatorException;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictDimension;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.report.util.geom.StrictPoint;

/**
 * The TextContentFactoryModule creates plain text content from the given element. The
 * content type of the used element should be "text/plain".
 *
 * @author Thomas Morgner
 */
public class TextContentFactoryModule implements ContentFactoryModule
{
  /**
   * creates a new TextContentFactoryModule.
   */
  public TextContentFactoryModule ()
  {
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise. This implementation is able to handle the type
   * &quot;text/plain&quot;.
   *
   * @param contentType the content type.
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent (final String contentType)
  {
    return contentType.equalsIgnoreCase("text/plain");
  }

  /**
   * Creates a content wrapper for the element. If the text given is null or the text's
   * length is zero, this method will return the EmptyContent reference.
   *
   * @param e      the element.
   * @param bounds the bounds.
   * @param ot     the output target.
   * @return the content, never null.
   *
   * @throws ContentCreationException if there is a problem creating the content.
   */
  public Content createContentForElement (final Element e, final ElementLayoutInformation bounds,
                                          final LayoutSupport ot)
          throws ContentCreationException
  {
    final Object value = e.getValue();
    if (value == null)
    {
      return EmptyContent.getDefaultEmptyContent();
    }
    final String text = String.valueOf(value);
    if (text.length() == 0)
    {
      return EmptyContent.getDefaultEmptyContent();
    }

    final StrictPoint point = bounds.getAbsolutePosition();

    // TextElement has a defined width (Max(MinSize, PrefSize).
    // and a maximum height (Min(MaxSize, PrefSize).

    final StrictDimension prefSize = bounds.getPreferredSize();
    long width = bounds.getMinimumSize().getWidth();
    long height = bounds.getMaximumSize().getHeight();
    if (prefSize != null)
    {
      width = Math.max(prefSize.getWidth(), width);
      height = Math.min(prefSize.getHeight(), height);
    }
    final StrictDimension dim = new StrictDimension(width, height);

    if (dim.getWidth() == 0 && dim.getHeight() == 0)
    {
      return EmptyContent.getDefaultEmptyContent();
    }

    final FontDefinition f = e.getStyle().getFontDefinitionProperty();
    final StrictBounds tBounds = new StrictBounds
            (point.getX(), point.getY(), dim.getWidth(), dim.getHeight());

    final Float lineHeight = (Float) e.getStyle().getStyleProperty(ElementStyleSheet.LINEHEIGHT);
    try
    {
      final String reservedLiteral = (String) e.getStyle().getStyleProperty
              (ElementStyleSheet.RESERVED_LITERAL, "..");

      final boolean trimTextContent = e.getStyle().getBooleanStyleProperty
              (ElementStyleSheet.TRIM_TEXT_CONTENT);

      final TextContent tc = new TextContent
              (text, StrictGeomUtility.toInternalValue(lineHeight.floatValue()), tBounds,
                      ot.createTextSizeCalculator(f), reservedLiteral, trimTextContent);
      return tc;
    }
    catch (SizeCalculatorException se)
    {
      throw new ContentCreationException("Failed to create the TextSizeCalculator", se);
    }
  }
}
