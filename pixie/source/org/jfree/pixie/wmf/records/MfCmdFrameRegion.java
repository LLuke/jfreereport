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
 * MfCmdFrameRegion.java
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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfLogRegion;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * The FrameRgn function draws a border around the specified region by using the specified
 * brush.
 */
public class MfCmdFrameRegion extends MfCmd
{
  private int width;
  private int height;
  private int scaled_width;
  private int scaled_height;
  private int brushObjectNr;
  private int regionObjectNr;

  public MfCmdFrameRegion ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    final MfLogBrush brush = file.getBrushObject(brushObjectNr);
    final MfLogRegion regio = file.getRegionObject(regionObjectNr);

    final MfDcState state = file.getCurrentState();
    state.setLogRegion(regio);
    state.setLogBrush(brush);

    final Graphics2D graph = file.getGraphics2D();
    final Rectangle rec = scaleRect(regio.getBounds());

    if (brush.isVisible())
    {
      final Dimension dim = getScaledDimension();
      // upper side
      final Rectangle2D rect = new Rectangle2D.Double();
      rect.setFrame(rec.x, rec.y, rec.width, dim.height);
      state.preparePaint();
      graph.fill(rect);

      // lower side
      rect.setFrame(rec.x, rec.y - dim.height, rec.width, dim.height);
      graph.fill(rect);

      // east
      rect.setFrame(rec.x, rec.y, dim.width, rec.height);
      graph.fill(rect);

      // west
      rect.setFrame(rec.width - dim.width, rec.y, dim.width, rec.height);
      graph.fill(rect);
      state.postPaint();
    }
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdFrameRegion();
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.FRAME_REGION;
  }

  /**
   * Reads the command data from the given record and adjusts the internal parameters
   * according to the data parsed.
   * <p/>
   * After the raw record was read from the datasource, the record is parsed by the
   * concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord (final MfRecord record)
  {
    final int height = record.getParam(0);
    final int width = record.getParam(1);
    final int regio = record.getParam(2);
    final int brush = record.getParam(3);
    setBrush(brush);
    setRegion(regio);
    setDimension(width, height);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    final MfRecord record = new MfRecord(4);
    final Dimension dim = getDimension();
    record.setParam(0, dim.height);
    record.setParam(1, dim.width);
    record.setParam(2, getRegion());
    record.setParam(3, getBrush());
    return record;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[FRAME_REGION] region=");
    b.append(getRegion());
    b.append(" brush=");
    b.append(getBrush());
    b.append(" dimension=");
    b.append(getDimension());
    return b.toString();
  }

  public void setDimension (final int width, final int height)
  {
    this.width = width;
    this.height = height;
    scaleXChanged();
    scaleYChanged();
  }

  public void setDimension (final Dimension dim)
  {
    setDimension(dim.width, dim.height);
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_width = getScaledX(width);
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
    scaled_height = getScaledY(height);
  }

  public Dimension getDimension ()
  {
    return new Dimension(width, height);
  }

  public Dimension getScaledDimension ()
  {
    return new Dimension(scaled_width, scaled_height);
  }

  public int getBrush ()
  {
    return brushObjectNr;
  }

  public void setBrush (final int brush)
  {
    this.brushObjectNr = brush;
  }

  public int getRegion ()
  {
    return regionObjectNr;
  }

  public void setRegion (final int region)
  {
    regionObjectNr = region;
  }

}
