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
 * ------------------------------
 * ShapeContentFactoryModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeContentFactoryModule.java,v 1.12 2003/06/27 14:25:23 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version, extracted from OperationFactoryModule
 */
package com.jrefinery.report.targets.base.content;

import java.awt.Shape;
import java.awt.geom.Dimension2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.ShapeTransform;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.StringUtil;

/**
 * The ShapeContentFactoryModule is used to create a content wrapper for java.awt.Shape
 * objects. This FactoryModules is able to handle all elements with the content type
 * "shape/*".
 *
 * @author Thomas Morgner
 */
public class ShapeContentFactoryModule implements ContentFactoryModule
{
  /**
   * creates a new ShapeContentFactoryModule.
   */
  public ShapeContentFactoryModule()
  {
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise. Returns true, if the content type is a subtype of
   * "shape".
   *
   * @param contentType  the content type.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent(final String contentType)
  {
    return (StringUtil.startsWithIgnoreCase(contentType, "shape/"));
  }


  /**
   * Creates content for an element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the OutputTarget.
   */
  public Content createContentForElement(final Element e, final ElementLayoutInformation bounds,
                                         final LayoutSupport ot)
      throws ContentCreationException
  {
    final Shape value = (Shape) e.getValue();
    if (value == null)
    {
      return null;
    }

    final Dimension2D iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
        bounds.getPreferredSize());
    if (iBounds.getWidth() == 0 && iBounds.getHeight() == 0)
    {
      return null;
    }

    final Shape s = ShapeTransform.transformShape(value,
        e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE),
        e.getStyle().getBooleanStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO),
        iBounds);
    return new ShapeContent(s);
  }
}
