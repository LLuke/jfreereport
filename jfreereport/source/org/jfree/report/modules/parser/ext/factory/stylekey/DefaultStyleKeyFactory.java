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
 * ---------------------------
 * DefaultStyleKeyFactory.java
 * ---------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultStyleKeyFactory.java,v 1.3 2003/08/24 15:08:21 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.stylekey;

import org.jfree.report.ShapeElement;
import org.jfree.report.style.BandStyleSheet;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.Log;

/**
 * A default implementation of the {@link StyleKeyFactory} interface.
 * This implementation contains all stylekeys from the ElementStyleSheet,
 * the BandStyleSheet and the ShapeElement stylesheet.
 * <p>
 * If available, the excel stylesheets will also be loaded.
 *
 * @author Thomas Morgner
 */
public class DefaultStyleKeyFactory extends AbstractStyleKeyFactory
{
  /**
   * Creates a new factory.
   */
  public DefaultStyleKeyFactory()
  {
    loadFromClass(ElementStyleSheet.class);
    loadFromClass(BandStyleSheet.class);
    loadFromClass(ShapeElement.class);

    try
    {
      loadFromClass(Thread.currentThread().getContextClassLoader().
          loadClass("org.jfree.report.modules.output.table.xls.ExcelProcessor"));
    }
    catch (Exception e)
    {
      Log.debug("Loading excel stylekeys failed. Keys " +
          "from that module will not be available.");
    }
/*
    addKey(ElementStyleSheet.ALIGNMENT);
    addKey(ElementStyleSheet.BOLD);
    addKey(ElementStyleSheet.BOUNDS);
    addKey(ElementStyleSheet.FONT);
    addKey(ElementStyleSheet.FONTSIZE);
    addKey(ElementStyleSheet.ITALIC);
    addKey(ElementStyleSheet.KEEP_ASPECT_RATIO);
    addKey(ElementStyleSheet.MAXIMUMSIZE);
    addKey(ElementStyleSheet.MINIMUMSIZE);
    addKey(ElementStyleSheet.PAINT);
    addKey(ElementStyleSheet.PREFERREDSIZE);
    addKey(ElementStyleSheet.SCALE);
    addKey(ElementStyleSheet.STRIKETHROUGH);
    addKey(ElementStyleSheet.STROKE);
    addKey(ElementStyleSheet.UNDERLINED);
    addKey(ElementStyleSheet.VALIGNMENT);
    addKey(ElementStyleSheet.VISIBLE);
    addKey(ElementStyleSheet.DYNAMIC_HEIGHT);
    addKey(ElementStyleSheet.LINEHEIGHT);
    addKey(ElementStyleSheet.EMBEDDED_FONT);
    addKey(ElementStyleSheet.FONTENCODING);
    addKey(ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE);

    addKey(BandStyleSheet.DISPLAY_ON_FIRSTPAGE);
    addKey(BandStyleSheet.DISPLAY_ON_LASTPAGE);
    addKey(BandStyleSheet.PAGEBREAK_AFTER);
    addKey(BandStyleSheet.PAGEBREAK_BEFORE);
    addKey(BandStyleSheet.REPEAT_HEADER);
    addKey(ShapeElement.DRAW_SHAPE);
    addKey(ShapeElement.FILL_SHAPE);
*/
  }


}
