/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------------
 * MultilineTextElement.java
 * -------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MultilineTextElement.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed to use the OutputTarget.
 * 10-May-2002 : Removed all compex constructors
 */

package com.jrefinery.report;

import java.awt.Paint;
import java.awt.geom.Rectangle2D;

public class MultilineTextElement extends StringElement
{

  /**
   * Standard constructor - builds a string element using float coordinates.
   */
  public MultilineTextElement ()
  {
  }

  /**
   * Draws the element at its position relative to the supplied band co-ordinates.
   *
   * @param target The output target.
   * @param band The band.
   * @param bandX The x-coordinate of the report band.
   * @param bandY The y-coordinate of the report band.
   */
  public void draw (OutputTarget target, Band band, float bandX, float bandY)
  {
    if (m_paint != null)
    {
      target.setPaint(m_paint);
    }
    else
    {
      target.setPaint (band.getDefaultPaint ());
    }

    // set the font...
    if (getFont (band) != null)
    {
      target.setFont (getFont (band));
    }

    // draw the text...
    Rectangle2D area = getBounds ();
    float x1 = bandX + (float) area.getX ();
    float y1 = bandY + (float) area.getY ();
    float x2 = bandX + (float) area.getMaxX ();
    float y2 = bandY + (float) area.getMaxY ();

    if (x2 > x1)
    {
      target.drawMultiLineText (this.getFormattedText (), x1, y1, x2, y2, getAlignment ());
    }
    else
    {
      target.drawMultiLineText (this.getFormattedText (), bandX, y1, bandX + target.getUsableWidth (), y2, getAlignment ());
    }

  }

}
