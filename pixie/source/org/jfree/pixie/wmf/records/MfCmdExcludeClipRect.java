/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * MfCmdExcludeClipRect.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdEllipse.java,v 1.2 2003/03/14 20:06:06 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Rectangle;

/**
 * top, left, right and bottom define the points of the region to be deleted
 * from the clipping region, the resultant clipping region is the original
 * region minus this region.
 */
public class MfCmdExcludeClipRect extends MfCmd
{
  private static final int RECORD_SIZE = 4;
  private static final int POS_TOP = 2;
  private static final int POS_LEFT = 3;
  private static final int POS_RIGHT = 1;
  private static final int POS_BOTTOM = 0;

  private int x;
  private int y;
  private int width;
  private int height;
  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;

  public MfCmdExcludeClipRect ()
  {
  }

  public void replay (WmfFile file)
  {
    // Not implemented!
    // no clipping is implemented at all ...
  }

  public MfCmd getInstance ()
  {
    return new MfCmdExcludeClipRect ();
  }

  public void setRecord (MfRecord record)
  {
    int bottom = record.getParam (POS_BOTTOM);
    int right = record.getParam (POS_RIGHT);
    int top = record.getParam (POS_TOP);
    int left = record.getParam (POS_LEFT);
    setBounds (left, top, right - left, bottom - top);

  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    Rectangle rc = getBounds();
    MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(POS_BOTTOM, (int)(rc.getY() + rc.getHeight()));
    record.setParam(POS_RIGHT, (int)(rc.getX() + rc.getWidth()));
    record.setParam(POS_TOP, (int)(rc.getY()));
    record.setParam(POS_LEFT, (int)(rc.getX()));
    return record;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[EXCLUDE_CLIP_RECT] bounds=");
    b.append (getBounds ());
    return b.toString ();
  }

  public Rectangle getBounds ()
  {
    return new Rectangle (x, y, width, height);
  }

  public Rectangle getScaledBounds ()
  {
    return new Rectangle (scaled_x, scaled_y, scaled_width, scaled_height);
  }

  public void setBounds (int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    scaleXChanged ();
    scaleYChanged ();
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
    scaled_width = getScaledX (width);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
    scaled_height = getScaledY (height);
  }

  public int getFunction ()
  {
    return MfType.EXCLUDE_CLIP_RECT;
  }
}
