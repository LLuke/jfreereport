/**
 * ========================================
 * Pixie : a free Java vector image library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/pixie/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * MfCmdExtFloodFill.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.pixie.wmf.records;

import java.awt.Color;
import java.awt.Point;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

public class MfCmdExtFloodFill extends MfCmd
{
  /* ExtFloodFill style flags */
  public static final int FLOODFILLBORDER = 0;
  public static final int FLOODFILLSURFACE = 1;

  private int filltype;
  private Color color;
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdExtFloodFill ()
  {
  }

  public void replay (final WmfFile file)
  {
    // there is no way to implement flood fill for G2Objects ...
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[EXT_FLOOD_FILL] filltype=");
    b.append(getFillType());
    b.append(" color=");
    b.append(getColor());
    b.append(" target=");
    b.append(getTarget());
    return b.toString();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdExtFloodFill();
  }

  private static final int RECORD_SIZE = 5;
  private static final int POS_FILLTYPE = 0;
  private static final int POS_COLOR = 1;
  private static final int POS_Y = 3;
  private static final int POS_X = 4;

  public void setRecord (final MfRecord record)
  {
    final int filltype = record.getParam(POS_FILLTYPE);
    final int c = record.getLongParam(POS_COLOR);
    final Color color = new GDIColor(c);
    final int y = record.getParam(POS_Y);
    final int x = record.getParam(POS_X);
    setTarget(x, y);
    setColor(color);
    setFillType(filltype);
  }

  /**
   * Writer function
   */
  public MfRecord getRecord ()
  {
    final MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(POS_FILLTYPE, getFillType());
    record.setLongParam(POS_COLOR, GDIColor.translateColor(getColor()));
    final Point target = getTarget();
    record.setParam(POS_Y, (int) target.getY());
    record.setParam(POS_X, (int) target.getX());
    return record;
  }

  public void setFillType (final int filltype)
  {
    this.filltype = filltype;
  }

  public int getFillType ()
  {
    return filltype;
  }

  public int getFunction ()
  {
    return MfType.EXT_FLOOD_FILL;
  }

  public Point getTarget ()
  {
    return new Point(x, y);
  }

  public Point getScaledTarget ()
  {
    return new Point(scaled_x, scaled_y);
  }

  public void setTarget (final int x, final int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged();
    scaleYChanged();
  }

  public void setColor (final Color c)
  {
    this.color = c;
  }

  public Color getColor ()
  {
    return color;
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX(x);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY(y);
  }
}
