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
 * MfCmdPaintRegion.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdPaintRgn.java,v 1.3 2003/07/03 16:13:36 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfLogRegion;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * Fills the region with the currently selected brush.
 */
public class MfCmdPaintRgn extends MfCmd
{
  private static final int RECORD_SIZE = 1;
  private static final int POS_REGION = 0;

  private int region;

  public MfCmdPaintRgn ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    final MfLogRegion regio = file.getRegionObject (region);

    final MfDcState state = file.getCurrentState ();
    state.setLogRegion (regio);
    final MfLogBrush brush = state.getLogBrush ();

    final Graphics2D graph = file.getGraphics2D ();
    final Rectangle rec = scaleRect (regio.getBounds ());

    if (brush.isVisible ())
    {
      state.preparePaint ();
      graph.fill (rec);
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
    return new MfCmdPaintRgn ();
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
  public void setRecord (final MfRecord record)
  {
    region = record.getParam (POS_REGION);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    final MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(POS_REGION, getRegion());
    return record;
  }

  public void setRegion (final int region)
  {
    this.region = region;
  }

  public int getRegion ()
  {
    return region;
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.PAINTREGION;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer ();
    b.append ("[PAINT_REGION] region=");
    b.append (getRegion ());
    return b.toString ();
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted. Not used.
   */
  protected void scaleXChanged ()
  {
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted. Not used.
   */
  protected void scaleYChanged ()
  {
  }
}
