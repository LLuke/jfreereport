/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -----------------------------
 * TextContentFactoryModule.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextContentFactoryModule.java,v 1.9 2003/05/02 12:40:31 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version, extracted from OperationFactoryModule
 */
package com.jrefinery.report.targets.base.content;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.base.layout.SizeCalculatorException;
import com.jrefinery.report.targets.style.ElementStyleSheet;
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
  public boolean canHandleContent(String contentType)
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
  public Content createContentForElement(Element e, ElementLayoutInformation bounds, 
                                         LayoutSupport ot)
    throws ContentCreationException
  {
    String text = (String) e.getValue();
    if (text == null)
    {
      return null;
    }
    if (text.length() == 0)
    {
      return null;
    }
    
    Point2D point = bounds.getAbsolutePosition();

    // TextElement has a defined width (Max(MinSize, PrefSize).
    // and a maximum height (Min(MaxSize, PrefSize).

    Dimension2D prefSize = bounds.getPreferredSize();
    double width = bounds.getMinimumSize().getWidth();
    double height = bounds.getMaximumSize().getHeight();
    if (prefSize != null)
    {
      width = Math.max(prefSize.getWidth(), width);
      height = Math.min (prefSize.getHeight(), height);
    }
    Dimension2D dim = new FloatDimension((float) width, (float) height);

    if (dim.getWidth() == 0 || dim.getHeight() == 0)
    {
      return null;
    }

    FontDefinition f = e.getStyle().getFontDefinitionProperty();
    Rectangle2D tBounds = new Rectangle2D.Float((float) point.getX(),
                                                  (float) point.getY(),
                                                  (float) dim.getWidth(),
                                                  (float) dim.getHeight());

    Float lh = (Float) e.getStyle().getStyleProperty(ElementStyleSheet.LINEHEIGHT);
    try
    {
      TextContent tc = new TextContent(text, lh.floatValue(), tBounds, 
                                       ot.createTextSizeCalculator(f));
      return tc;
    }
    catch (SizeCalculatorException se)
    {
      throw new ContentCreationException("Failed to create the TextSizeCalculator", se);
    }
  }
}
