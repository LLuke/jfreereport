/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text.font;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 11.06.2006, 18:30:42
 *
 * @author Thomas Morgner
 */
public class DefaultKerningProducer implements KerningProducer
{
  private static class DefaultKerningProducerState implements State
  {
    private int lastCodePoint;
    private FontMetrics fontMetrics;

    public DefaultKerningProducerState()
    {
    }

    public int getLastCodePoint()
    {
      return lastCodePoint;
    }

    public void setLastCodePoint(final int lastCodePoint)
    {
      this.lastCodePoint = lastCodePoint;
    }

    public FontMetrics getFontMetrics()
    {
      return fontMetrics;
    }

    public void setFontMetrics(final FontMetrics fontMetrics)
    {
      this.fontMetrics = fontMetrics;
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
      DefaultKerningProducer dp = new DefaultKerningProducer(fontMetrics);
      dp.lastCodePoint = lastCodePoint;
      return dp;
    }
  }

  private int lastCodePoint;
  private FontMetrics fontMetrics;

  public DefaultKerningProducer(FontMetrics fontMetrics)
  {
    if (fontMetrics == null)
    {
      throw new NullPointerException();
    }
    this.fontMetrics = fontMetrics;
  }

  public int getKerning(final int codePoint)
  {
    if (codePoint == START_OF_TEXT || codePoint == END_OF_TEXT)
    {
      lastCodePoint = 0;
      return 0;
    }

    final double d = fontMetrics.getKerning(lastCodePoint, codePoint);
    lastCodePoint = codePoint;
    return (int) StrictGeomUtility.toInternalValue(d);
  }

  public State saveState() throws StateException
  {
    DefaultKerningProducerState dps = new DefaultKerningProducerState();
    dps.setLastCodePoint(lastCodePoint);
    dps.setFontMetrics(fontMetrics);
    return dps;
  }
}
