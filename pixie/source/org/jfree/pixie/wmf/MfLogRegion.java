/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------
 * MfLogRegion.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: MfLogRegion.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

import java.awt.Rectangle;

/**
 * A Wmf logical region definition.
 */
public class MfLogRegion implements WmfObject
{
  private int x;
  private int y;
  private int w;
  private int h;

  public int getType ()
  {
    return OBJ_REGION;
  }

  public MfLogRegion ()
  {
  }

  public void setBounds (final int x, final int y, final int w, final int h)
  {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  public Rectangle getBounds ()
  {
    return new Rectangle (x, y, w, h);
  }
}
