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
 * MfCmdFrameRegion.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmd.java,v 1.2 2003/03/14 20:06:04 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfLogRegion;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * The FrameRgn function draws a border around the
 * specified region by using the specified brush.
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
  public void replay (WmfFile file)
  {
    MfLogBrush brush = file.getBrushObject (brushObjectNr);
    MfLogRegion regio = file.getRegionObject (regionObjectNr);

    MfDcState state = file.getCurrentState ();
    state.setLogRegion (regio);
    state.setLogBrush (brush);

    Graphics2D graph = file.getGraphics2D ();
    Rectangle rec = scaleRect (regio.getBounds ());

    if (brush.isVisible ())
    {
      Dimension dim = getScaledDimension ();
      // upper side
      Rectangle2D rect = new Rectangle2D.Double ();
      rect.setFrame (rec.x, rec.y, rec.width, dim.height);
      state.preparePaint ();
      graph.fill (rect);

      // lower side
      rect.setFrame (rec.x, rec.y - dim.height, rec.width, dim.height);
      graph.fill (rect);

      // east
      rect.setFrame (rec.x, rec.y, dim.width, rec.height);
      graph.fill (rect);

      // west
      rect.setFrame (rec.width - dim.width, rec.y, dim.width, rec.height);
      graph.fill (rect);
      state.postPaint ();
    }
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdFrameRegion ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.FRAME_REGION;
  }

  /**
   * Reads the command data from the given record and adjusts the internal
   * parameters according to the data parsed.
   * <p>
   * After the raw record was read from the datasource, the record is parsed
   * by the concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord (MfRecord record)
  {
    int height = record.getParam (0);
    int width = record.getParam (1);
    int regio = record.getParam (2);
    int brush = record.getParam (3);
    setBrush (brush);
    setRegion (regio);
    setDimension (width, height);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    MfRecord record = new MfRecord(4);
    Dimension dim = getDimension();
    record.setParam(0, dim.height);
    record.setParam(1, dim.width);
    record.setParam(2, getRegion());
    record.setParam(3, getBrush());
    return record;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[FRAME_REGION] region=");
    b.append (getRegion ());
    b.append (" brush=");
    b.append (getBrush ());
    b.append (" dimension=");
    b.append (getDimension ());
    return b.toString ();
  }

  public void setDimension (int width, int height)
  {
    this.width = width;
    this.height = height;
    scaleXChanged ();
    scaleYChanged ();
  }

  public void setDimension (Dimension dim)
  {
    setDimension(dim.width, dim.height);
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_width = getScaledX (width);
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
    scaled_height = getScaledY (height);
  }

  public Dimension getDimension ()
  {
    return new Dimension (width, height);
  }

  public Dimension getScaledDimension ()
  {
    return new Dimension (scaled_width, scaled_height);
  }

  public int getBrush ()
  {
    return brushObjectNr;
  }

  public void setBrush (int brush)
  {
    this.brushObjectNr = brush;
  }

  public int getRegion ()
  {
    return regionObjectNr;
  }

  public void setRegion (int region)
  {
    regionObjectNr = region;
  }

}
