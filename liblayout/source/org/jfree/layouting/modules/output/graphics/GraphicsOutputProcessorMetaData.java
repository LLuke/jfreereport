/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id: GraphicsOutputProcessorMetaData.java,v 1.2 2006/11/13 19:14:05 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.graphics;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.output.AbstractOutputProcessorMetaData;

/**
 * Creation-Date: 02.01.2006, 19:57:08
 *
 * @author Thomas Morgner
 */
public class GraphicsOutputProcessorMetaData
    extends AbstractOutputProcessorMetaData
{
  private boolean iterative;

  public GraphicsOutputProcessorMetaData(final FontStorage storage,
                                         final boolean iterative)
  {
    super(storage);
    this.iterative = iterative;

    setFamilyMapping(FontFamilyValues.CURSIVE, "sans-serif");
    setFamilyMapping(FontFamilyValues.FANTASY, "Verdana");
    setFamilyMapping(FontFamilyValues.MONOSPACE, "monospaced");
    setFamilyMapping(FontFamilyValues.SERIF, "serif");
    setFamilyMapping(FontFamilyValues.SANS_SERIF, "sans-serif");
  }

  public FontFamily getDefaultFontFamily()
  {
    return getFontRegistry().getFontFamily("SansSerif");
  }

  public String getExportDescriptor()
  {
    return "pageable/X-AWT-Graphics";
  }

  public boolean isPagebreakAware()
  {
    return true;
  }

  public boolean isIterative()
  {
    return iterative;
  }
}
