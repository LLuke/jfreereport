/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * CSVMetaBandProducer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CSVMetaBandProducer.java,v 1.5 2005/02/19 13:30:01 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 01.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.csv;

import org.jfree.report.Element;
import org.jfree.report.TextElement;
import org.jfree.report.content.Content;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.base.TableMetaBandProducer;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.geom.StrictBounds;

public class CSVMetaBandProducer extends TableMetaBandProducer
{
  public CSVMetaBandProducer (final LayoutSupport support)
  {
    super(support);
  }

  /**
   * Checks, whether the element contains text content and if so, creates the data
   * element.
   *
   * @param e the element.
   * @return the table cell data element for the element.
   */
  protected MetaElement createTextCell (final Element e, final long x, final long y)
  {
    // we only handle plain text elements at the moment ...
    if (e.getContentType().equals(TextElement.CONTENT_TYPE) == false)
    {
      return null;
    }
    if (e.isVisible() == false)
    {
      return null;
    }

    final StrictBounds bounds = (StrictBounds)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

    final Content content = new RawContent(bounds, e.getValue());
    final ElementStyleSheet style = new MetaElementStyleSheet("meta-" + e.getName());
    style.setStyleProperty(ElementStyleSheet.BOUNDS,
            createElementBounds(e.getStyle(), x, y));
    final MetaElement element = new MetaElement(content, style);
    return element;
  }

  protected MetaElement createDrawableCell (final Element e,
                                            final long x, final long y)
  {
    return null;
  }

  protected MetaElement createImageCell (final Element e,
                                         final long x, final long y)
  {
    return null;
  }
}
