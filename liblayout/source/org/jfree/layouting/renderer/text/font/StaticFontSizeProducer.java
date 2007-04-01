/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text.font;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Creates a monospaced font from any given font by always returning the maximum
 * character width and height for that font. Grapheme clusters have no effect on
 * that font size producer.
 *
 * @author Thomas Morgner
 */
public class StaticFontSizeProducer implements FontSizeProducer
{
  private static class StaticFontSizeProducerState implements State
  {
    private int maxWidth;
    private int maxHeight;
    private int baseLine;

    public StaticFontSizeProducerState
            (final int maxWidth, final int maxHeight, final int baseLine)
    {
      this.maxWidth = maxWidth;
      this.maxHeight = maxHeight;
      this.baseLine = baseLine;
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(LayoutProcess layoutProcess)
            throws StateException
    {
      return new StaticFontSizeProducer(maxWidth, maxHeight, baseLine);
    }
  }

  private int maxWidth;
  private int maxHeight;
  private int baseLine;
 // private FontMetrics fontMetrics;

  public StaticFontSizeProducer(FontMetrics fontMetrics)
  {
    if (fontMetrics == null)
    {
      throw new NullPointerException();
    }
    //this.fontMetrics = fontMetrics;
    this.maxHeight = (int) (0x7FFFFFFF &
            StrictGeomUtility.toInternalValue(fontMetrics.getMaxHeight()));
    this.maxWidth = (int) (0x7FFFFFFF &
            StrictGeomUtility.toInternalValue(fontMetrics.getMaxCharAdvance()));
    this.baseLine = (int) (0x7FFFFFFF & StrictGeomUtility.toInternalValue
            (fontMetrics.getMaxHeight() - fontMetrics.getMaxDescent()));
  }

  public StaticFontSizeProducer(final int maxWidth,
                                final int maxHeight,
                                final int baseLine)
  {
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
    this.baseLine = baseLine;
  }

  public GlyphMetrics getCharacterSize(final int codePoint,
                                          final GlyphMetrics dimension)
  {
    if (dimension == null)
    {
      GlyphMetrics retval = new GlyphMetrics ();
      retval.setWidth(maxWidth);
      retval.setHeight(maxHeight);
      retval.setBaselinePosition(baseLine);
      return retval;
    }

    dimension.setWidth(maxWidth);
    dimension.setHeight(maxHeight);
    dimension.setBaselinePosition(baseLine);
    return dimension;
  }

  public State saveState() throws StateException
  {
    return new StaticFontSizeProducerState(maxWidth, maxHeight, baseLine);
  }
}
