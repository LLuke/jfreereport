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
 * MfCmdCreatePen.java
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

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfLogPen;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * The CreatePenIndirect function creates a logical cosmetic pen that has the style,
 * width, and color specified in a structure.
 * <p/>
 * <pre>
 * typedef struct tagLOGPEN {
 * UINT     lopnStyle;
 * POINT    lopnWidth;
 * COLORREF lopnColor;
 * } LOGPEN, *PLOGPEN;
 * </pre>
 */
public class MfCmdCreatePen extends MfCmd
{
  private static final int RECORD_SIZE = 4;
  private static final int POS_STYLE = 0;
  private static final int POS_WIDTH = 1;
  private static final int POS_COLOR = 2;

  private int style;
  private Color color;
  private int width;
  private int scaled_width;

  public MfCmdCreatePen ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    final MfLogPen lpen = new MfLogPen();
    lpen.setStyle(getStyle());
    lpen.setColor(getColor());
    lpen.setWidth(getScaledWidth());

    file.getCurrentState().setLogPen(lpen);
    file.storeObject(lpen);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdCreatePen();
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.CREATE_PEN_INDIRECT;
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    final MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(POS_STYLE, getStyle());
    record.setParam(POS_WIDTH, getWidth());
    record.setLongParam(POS_COLOR, GDIColor.translateColor(getColor()));
    return record;
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
    final int style = record.getParam(POS_STYLE);
    final int width = record.getParam(POS_WIDTH);
    final int color = record.getLongParam(POS_COLOR);

    setStyle(style);
    setWidth(width);
    setColor(new GDIColor(color));
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[CREATE_PEN] style=");
    b.append(getStyle());
    b.append(" width=");
    b.append(getWidth());
    b.append(" color=");
    b.append(getColor());
    return b.toString();
  }


  public int getStyle ()
  {
    return style;
  }

  public void setStyle (final int style)
  {
    this.style = style;
  }

  public int getScaledWidth ()
  {
    return scaled_width;
  }

  public int getWidth ()
  {
    return width;
  }

  public void setWidth (final int width)
  {
    this.width = width;
    scaleXChanged();
  }

  public Color getColor ()
  {
    return color;
  }

  public void setColor (final Color c)
  {
    this.color = c;
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_width = getScaledX(width);
  }

}
