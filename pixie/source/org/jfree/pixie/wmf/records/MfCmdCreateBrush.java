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
 * MfCmdCreateBrush.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdArc.java,v 1.1 2003/03/09 20:38:23 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.BrushConstants;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Color;

/**
 * The CreateBrushIndirect function creates a logical brush that has the
 * specified style, color, and pattern.
 * <p>
 * The style is one of the BS_* constants defined in {@link org.jfree.pixie.wmf.BrushConstants}.
 * The hatch is one of the HS_* constants defined in {@link org.jfree.pixie.wmf.BrushConstants}.
 */
public class MfCmdCreateBrush extends MfCmd
{
  private static final int PARAM_STYLE = 0;
  private static final int PARAM_COLOR = 1;
  private static final int PARAM_HATCH = 3;

  private static final int RECORD_SIZE = 4;


  private int style;
  private Color color;
  private int hatch;

  public MfCmdCreateBrush ()
  {
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_BRUSH] style=");
    b.append (getStyle ());
    b.append (" color=");
    b.append (getColor ());
    b.append (" hatch=");
    b.append (getHatch ());
    return b.toString ();
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    MfLogBrush lbrush = new MfLogBrush ();
    lbrush.setStyle (getStyle ());
    lbrush.setColor (getColor ());
    lbrush.setHatchedStyle (getHatch ());

    file.getCurrentState ().setLogBrush (lbrush);
    file.storeObject (lbrush);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdCreateBrush ();
  }

  public void setStyle (int style)
  {
    this.style = style;
  }

  public int getStyle ()
  {
    return style;
  }

  public void setHatch (int hatch)
  {
    if (hatch != BrushConstants.BS_DIBPATTERN &&
        hatch != BrushConstants.BS_DIBPATTERN8X8 &&
        hatch != BrushConstants.BS_DIBPATTERNPT &&
        hatch != BrushConstants.BS_HATCHED &&
        hatch != BrushConstants.BS_HOLLOW &&
        hatch != BrushConstants.BS_INDEXED &&
        hatch != BrushConstants.BS_MONOPATTERN &&
        hatch != BrushConstants.BS_NULL &&
        hatch != BrushConstants.BS_PATTERN &&
        hatch != BrushConstants.BS_PATTERN8X8 &&
        hatch != BrushConstants.BS_SOLID)
    {
      throw new IllegalArgumentException("The specified pattern is invalid");
    }

    this.hatch = hatch;
  }

  public int getHatch ()
  {
    return hatch;
  }

  public void setColor (Color c)
  {
    this.color = c;
  }

  public Color getColor ()
  {
    return color;
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.CREATE_BRUSH_INDIRECT;
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
    int style = record.getParam (PARAM_STYLE);
    int color = record.getLongParam (PARAM_COLOR);
    int hatch = record.getParam (PARAM_HATCH);
    setStyle (style);
    setColor (new GDIColor (color));
    setHatch (hatch);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(PARAM_STYLE, getStyle());
    record.setLongParam(PARAM_COLOR, GDIColor.translateColor(getColor()));
    record.setParam(PARAM_HATCH, getHatch());
    return record;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted. This method is not used.
   */
  protected void scaleXChanged ()
  {
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted. The method is not used.
   */
  protected void scaleYChanged ()
  {
  }

}
