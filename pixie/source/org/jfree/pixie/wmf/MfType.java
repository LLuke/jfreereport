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
 * MfType.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: MfType.java,v 1.1 2003/02/25 20:58:07 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;


/**
 * Predefined types of Windows metafile records.
 */
public class MfType
{

  // NOT YET IMPLEMENTED

  // Needs Bitmap-Implementation to work correctly
  public final static int CREATE_DIB_PATTERN_BRUSH = 0x0142;

  // Needs Bitmap-Implementation to work correctly
  public final static int CREATE_PATTERN_BRUSH = 0x01F9;
  public final static int SET_DIBITS_TO_DEVICE = 0x0d33;

  public final static int ANIMATE_PALETTE = 0x0436;
  public final static int ARC = 0x0817;
  public final static int BIT_BLT = 0x0940;
  public final static int CHORD = 0x0830;
  public final static int CREATE_BRUSH_INDIRECT = 0x02fc;
  public final static int CREATE_FONT_INDIRECT = 0x02fb;
  public final static int CREATE_PALETTE = 0x00f7;
  public final static int CREATE_PEN_INDIRECT = 0x02fa;
  public final static int CREATE_REGION = 0x06ff;
  public final static int DELETE_OBJECT = 0x01f0;
  public final static int ELLIPSE = 0x0418;
  public final static int ESCAPE = 0x0626;
  public final static int EXCLUDE_CLIP_RECT = 0x0415;
  public final static int EXT_FLOOD_FILL = 0x0548;
  public final static int EXT_TEXT_OUT = 0x0a32;
  public final static int FLOOD_FILL = 0x0419;
  public final static int FILL_REGION = 0x0228;
  public final static int FRAME_REGION = 0x0429;
  public final static int INTERSECT_CLIP_RECT = 0x0416;
  public final static int INVERT_REGION = 0x012a;
  public final static int LINE_TO = 0x0213;
  public final static int MOVE_TO = 0x0214;
  public final static int OFFSET_CLIP_RGN = 0x0220;
  public final static int OFFSET_VIEWPORT_ORG = 0x0211;
  public final static int OFFSET_WINDOW_ORG = 0x020f;
  public final static int PAINTREGION = 0x012b;
  public final static int PAT_BLT = 0x061d;
  public final static int PIE = 0x081a;
  public final static int POLYGON = 0x0324;
  public final static int POLYLINE = 0x0325;
  public final static int POLY_POLYGON = 0x0538;
  public final static int REALISE_PALETTE = 0x0035;
  public final static int RECTANGLE = 0x041b;
  public final static int RESIZE_PALETTE = 0x0139;
  public final static int RESTORE_DC = 0x0127;
  public final static int ROUND_RECT = 0x061c;
  public final static int SAVE_DC = 0x001e;
  public final static int SCALE_VIEWPORT_EXT = 0x0412;
  public final static int SCALE_WINDOW_EXT = 0x0400;
  public final static int SELECT_CLIP_REGION = 0x012c;
  public final static int SELECT_OBJECT = 0x012d;
  public final static int SELECT_PALETTE = 0x0234;
  public final static int SET_BK_COLOR = 0x0201;
  public final static int SET_BK_MODE = 0x0102;
  public final static int SET_MAP_MODE = 0x0103;
  public final static int SET_MAPPER_FLAGS = 0x0231;
  public final static int SET_PALETTE_ENTRIES = 0x0037;
  public final static int SET_PIXEL = 0x041f;
  public final static int SET_POLY_FILL_MODE = 0x0106;
  public final static int SET_ROP2 = 0x0104;
  public final static int SET_STRETCH_BLT_MODE = 0x0107;
  public final static int SET_TEXT_ALIGN = 0x012e;
  public final static int SET_TEXT_CHAR_EXTRA = 0x0108;
  public final static int SET_TEXT_COLOR = 0x0209;
  public final static int SET_TEXT_JUSTIFICATION = 0x020a;
  public final static int SET_VIEWPORT_EXT = 0x020e;
  public final static int SET_VIEWPORT_ORG = 0x020d;
  public final static int SET_WINDOW_EXT = 0x020c;
  public final static int SET_WINDOW_ORG = 0x020b;
  public final static int STRETCH_BLT = 0x0b41;
  public final static int STRETCH_DIBITS = 0x0f43;
  public final static int TEXT_OUT = 0x0521;
  public final static int END_OF_FILE = 0x0000;

  public final static int OLD_STRETCH_BLT = 0x0b23;
  public final static int OLD_CREATE_PATTERN_BRUSH = 0x01f9;
  public final static int OLD_BIT_BLT = 0x0922;


  /** Type bit flags. */
  public final static int STATE = 0x01;
  public final static int VECTOR = 0x02;
  public final static int RASTER = 0x04;
  public final static int MAPPING_MODE = 0x08;

  /** All the known types. The last is the default. */
  private static MfType[] ntab =
          {
            new MfType (PAINTREGION, "MfPaintRegion", VECTOR),
            new MfType (ARC, "MfArc", VECTOR),
            new MfType (CHORD, "MfChord", VECTOR),
            new MfType (ELLIPSE, "MfEllipse", VECTOR),
            new MfType (EXCLUDE_CLIP_RECT, "MfExcludeClipRect", STATE),
            new MfType (FLOOD_FILL, "MfFloodFill", VECTOR),
            new MfType (INTERSECT_CLIP_RECT, "MfIntersectClipRect", STATE),
            new MfType (LINE_TO, "MfLineTo", VECTOR),
            new MfType (MOVE_TO, "MfMoveTo", STATE),
            new MfType (OFFSET_CLIP_RGN, "MfOffsetclipRgn", STATE),
            new MfType (OFFSET_VIEWPORT_ORG, "MfOffsetViewportOrg", STATE | MAPPING_MODE),
            new MfType (OFFSET_WINDOW_ORG, "MfOffsetWindowOrg", STATE | MAPPING_MODE),
            new MfType (PAT_BLT, "MfPatBlt", RASTER),
            new MfType (PIE, "MfPie", VECTOR),
            new MfType (REALISE_PALETTE, "MfRealisePalette", STATE),
            new MfType (RECTANGLE, "MfRectangle", VECTOR),
            new MfType (RESIZE_PALETTE, "MfResizePalette", STATE),
            new MfType (RESTORE_DC, "MfRestoreDC", STATE | MAPPING_MODE),
            new MfType (ROUND_RECT, "MfRoundRect", VECTOR),
            new MfType (SAVE_DC, "MfSaveDC", STATE),
            new MfType (SCALE_VIEWPORT_EXT, "MfScaleViewportExt", STATE | MAPPING_MODE),
            new MfType (SCALE_WINDOW_EXT, "MfScaleWindowExt", STATE | MAPPING_MODE),
            new MfType (SET_BK_COLOR, "MfSetBkColor", STATE),
            new MfType (SET_BK_MODE, "MfSetBkMode", STATE),
            new MfType (SET_MAP_MODE, "MfSetMapMode", STATE | MAPPING_MODE),
            new MfType (SET_MAPPER_FLAGS, "MfSetMapperFlags", STATE),
            new MfType (SET_PIXEL, "MfSetPixel", RASTER),
            new MfType (SET_POLY_FILL_MODE, "MfSetPolyFillMode", STATE),
            new MfType (SET_ROP2, "MfSetROP2", STATE),
            new MfType (SET_STRETCH_BLT_MODE, "MfSetStretchBltMode", STATE),
            new MfType (SET_TEXT_ALIGN, "MfSetTextAlign", STATE),
            new MfType (SET_TEXT_CHAR_EXTRA, "MfSetTextCharExtra", STATE),
            new MfType (SET_TEXT_COLOR, "MfSetTextColor", STATE),
            new MfType (SET_TEXT_JUSTIFICATION, "MfSetTextJustification", STATE),
            new MfType (SET_VIEWPORT_EXT, "MfSetViewportExt", STATE | MAPPING_MODE),
            new MfType (SET_VIEWPORT_ORG, "MfSetViewportOrg", STATE | MAPPING_MODE),
            new MfType (SET_WINDOW_EXT, "MfSetWindowExt", STATE | MAPPING_MODE),
            new MfType (SET_WINDOW_ORG, "MfSetWindowOrg", STATE | MAPPING_MODE),
            new MfType (ANIMATE_PALETTE, "MfAnimatePalette", STATE),
            new MfType (BIT_BLT, "MfBitBlt", RASTER),
            new MfType (OLD_BIT_BLT, "MfOldBitBlt", RASTER),
            new MfType (CREATE_BRUSH_INDIRECT, "MfCreateBrush", STATE),
            new MfType (CREATE_FONT_INDIRECT, "MfCreateFont", STATE),
            new MfType (CREATE_PALETTE, "MfCreatePalette", STATE),
            new MfType (OLD_CREATE_PATTERN_BRUSH, "MfOldCreatePatternBrush", STATE),
            new MfType (CREATE_PATTERN_BRUSH, "MfCreatePatternBrush", STATE),
            new MfType (CREATE_PEN_INDIRECT, "MfCreatePen", STATE),
            new MfType (CREATE_REGION, "MfCreateRegion", STATE),
            new MfType (DELETE_OBJECT, "MfDeleteObject", STATE),
//    new MfType( DRAW_TEXT, "MfDrawText", VECTOR ),
            new MfType (ESCAPE, "MfEscape", STATE),
            new MfType (EXT_TEXT_OUT, "MfExtTextOut", VECTOR),
            new MfType (POLYGON, "MfPolygon", VECTOR),
            new MfType (POLY_POLYGON, "MfPolyPolygon", VECTOR),
            new MfType (POLYLINE, "MfPolyline", VECTOR),
            new MfType (SELECT_CLIP_REGION, "MfSelectClipRegion", STATE),
            new MfType (SELECT_OBJECT, "MfSelectObject", STATE),
            new MfType (SELECT_PALETTE, "MfSelectPalette", STATE),
            new MfType (SET_DIBITS_TO_DEVICE, "MfSetDIBitsToDevice", RASTER),
            new MfType (SET_PALETTE_ENTRIES, "MfSetPaletteEntries", STATE),
            new MfType (OLD_STRETCH_BLT, "MfOldStretchBlt", RASTER),
            new MfType (STRETCH_BLT, "MfStretchBlt", RASTER),
            new MfType (STRETCH_DIBITS, "MfStretchDIBits", RASTER),
            new MfType (TEXT_OUT, "MfTextOut", VECTOR),
            new MfType (END_OF_FILE, "MfEndOfFile", STATE),
            new MfType (-1, "MfUnknown", 0)
          };

  /** Map a 16-bit type id onto an object. */
  public static MfType get (int id)
  {
    for (int i = 0; i < ntab.length; i++)
    {
      if (ntab[i].id == id)
        return ntab[i];
    }
    return ntab[ntab.length - 1]; // Not found.
  }

  // Getter functionen
  private int id;
  private int type;
  private String name;

  public int getId ()
  {
    return id;
  }

  public String getName ()
  {
    return name;
  }

  public int getType ()
  {
    return type;
  }

  /**
   * True if this record marks the screen.
   */
  public boolean doesMark ()
  {
    return (type & (VECTOR | RASTER)) != 0;
  }

  /**
   * True if this record affects mapping modes.
   */
  public boolean isMappingMode ()
  {
    return (type & MAPPING_MODE) != 0;
  }

  private MfType (int id, String name, int type)
  {
    this.id = id;
    this.name = name;
    this.type = type;
  }

}
