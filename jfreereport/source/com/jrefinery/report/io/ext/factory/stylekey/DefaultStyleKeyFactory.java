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
 * ---------------------------
 * DefaultStyleKeyFactory.java
 * ---------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultStyleKeyFactory.java,v 1.5 2003/04/28 22:21:44 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.excel.ExcelProcessor;

/**
 * A default implementation of the {@link StyleKeyFactory} interface.
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
    addKey(BandStyleSheet.DISPLAY_ON_FIRSTPAGE);
    addKey(BandStyleSheet.DISPLAY_ON_LASTPAGE);
    addKey(BandStyleSheet.PAGEBREAK_AFTER);
    addKey(BandStyleSheet.PAGEBREAK_BEFORE);
    addKey(BandStyleSheet.REPEAT_HEADER);
    addKey(ShapeElement.DRAW_SHAPE);
    addKey(ShapeElement.FILL_SHAPE);
    addKey(ExcelProcessor.DATA_FORMAT_STRING);
  }
}
