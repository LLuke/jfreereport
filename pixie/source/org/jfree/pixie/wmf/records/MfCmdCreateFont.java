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
 * MfCmdCreateFont.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmd.java,v 1.1 2003/03/09 20:38:23 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfLogFont;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Font;

/**
 * The CreateFontIndirect function creates a logical font that has the specified
 * characteristics. The font can subsequently be selected as the current font for
 * any device context.
 * <p>
 <code>
 typedef struct tagLOGFONT {
 LONG lfHeight;
 LONG lfWidth;
 LONG lfEscapement;
 LONG lfOrientation;
 LONG lfWeight;
 BYTE lfItalic;
 BYTE lfUnderline;
 BYTE lfStrikeOut;
 BYTE lfCharSet;
 BYTE lfOutPrecision;
 BYTE lfClipPrecision;
 BYTE lfQuality;
 BYTE lfPitchAndFamily;
 TCHAR lfFaceName[LF_FACESIZE];
 } LOGFONT, *PLOGFONT;
 </code>
 */
public class MfCmdCreateFont extends MfCmd
{
  private static int FONT_FACE_MAX = 31;
  private static int FIXED_RECORD_SIZE = 9;
  private static int POS_HEIGHT = 0;
  private static int POS_WIDTH = 1;
  private static int POS_ESCAPEMENT = 2;
  private static int POS_ORIENTATION = 3;
  private static int POS_WEIGHT = 4;
  private static int POS_FLAGS1 = 5;
  private static int POS_FLAGS2 = 6;
  private static int POS_PRECISION = 7;
  private static int POS_QUALITY = 8;
  private static int POS_FONTFACE = 9;

  private int height;
  private int width;
  private int scaled_height;
  private int scaled_width;

  private int escapement;
  private int orientation;
  private int weight;
  private boolean italic;
  private boolean underline;
  private boolean strikeout;
  private int charset;
  private int outprecision;
  private int clipprecision;
  private int quality;
  private int pitchAndFamily;
  private String facename;

  public MfCmdCreateFont ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    MfLogFont lfont = new MfLogFont ();
    lfont.setFace (getFontFace ());
    lfont.setSize (getScaledHeight ());
    int style = 0;
    // should be bold ?
    if (getWeight () > 650)
    {
      style = Font.BOLD;
    }
    else
    {
      style = Font.PLAIN;
    }
    if (isItalic ())
    {
      style += Font.ITALIC;
    }
    lfont.setStyle (style);
    lfont.setUnderline (isUnderline ());
    lfont.setStrikeOut (isStrikeout ());
    lfont.setRotation (getEscapement () / 10);
    file.getCurrentState ().setLogFont (lfont);
    file.storeObject (lfont);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdCreateFont ();
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    String fontFace = getFontFace();
    if (fontFace.length() > FONT_FACE_MAX)
    {
      fontFace = fontFace.substring(0, FONT_FACE_MAX);
    }
    MfRecord record = new MfRecord(FIXED_RECORD_SIZE + fontFace.length());
    record.setParam(POS_HEIGHT, getHeight());
    record.setParam(POS_WIDTH, getWidth());
    record.setParam(POS_ESCAPEMENT, getEscapement());
    record.setParam(POS_ORIENTATION, getOrientation());
    record.setParam(POS_WEIGHT, getWeight());

    record.setParam(POS_FLAGS1, formFlags(isUnderline(), isItalic()));
    record.setParam(POS_FLAGS2, formFlags(isStrikeout(), false) + getCharset());
    record.setParam(POS_PRECISION, getOutputPrecision() << 8 + getClipPrecision());
    record.setParam(POS_QUALITY, getQuality() << 8 + getPitchAndFamily());
    record.setStringParam(POS_FONTFACE, fontFace);
    return record;
  }

  public void setRecord (MfRecord record)
  {
    int height = record.getParam (POS_HEIGHT);
    if (height == 0)
    {
      // a default height is requested, we use a default height of 10
      height = 10;
    }
    if (height < 0)
    {
      // windows specifiy font mapper matching, ignored.
      height *= -1;
    }

    int width = record.getParam (POS_WIDTH);
    int escape = record.getParam (POS_ESCAPEMENT);
    int orientation = record.getParam (POS_ORIENTATION);
    int weight = record.getParam (POS_WEIGHT);
    // todo check whether this is correct ...
    int italic = record.getParam (POS_FLAGS1) & 0xFF00;
    int underline = record.getParam (POS_FLAGS1) & 0x00FF;
    // todo check whether this is correct ..
    int strikeout = record.getParam (POS_FLAGS2) & 0xFF00;
    int charset = record.getParam (POS_FLAGS2) & 0x00FF;
    int outprec = record.getParam (POS_PRECISION) & 0xFF00;
    int clipprec = record.getParam (POS_PRECISION) & 0x00FF;
    int quality = record.getParam (POS_QUALITY) & 0xFF00;
    int pitch = record.getParam (POS_QUALITY) & 0x00FF;
    // A fontname must not exceed the length of 32 including the null-terminator
    String facename = record.getStringParam (POS_FONTFACE, 32);

    setCharset (charset);
    setClipPrecision (clipprec);
    setEscapement (escape);
    setFontFace (facename);
    setHeight (height);
    setItalic (italic != 0);
    setOrientation (orientation);
    setOutputPrecision (outprec);
    setPitchAndFamily (pitch);
    setQuality (quality);
    setStrikeout (strikeout != 0);
    setUnderline (underline != 0);
    setWeight (weight);
    setWidth (width);
  }

  private int formFlags (boolean f1, boolean f2)
  {
    int retval = 0;
    if (f1) retval += 0x0100;
    if (f2) retval += 1;
    return (retval);
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.CREATE_FONT_INDIRECT;
  }

  public void setFontFace (String facename)
  {
    this.facename = facename;
  }

  public String getFontFace ()
  {
    return facename;
  }

  public void setPitchAndFamily (int pitchAndFamily)
  {
    this.pitchAndFamily = pitchAndFamily;
  }

  public int getPitchAndFamily ()
  {
    return pitchAndFamily;
  }

  public void setQuality (int quality)
  {
    this.quality = quality;
  }

  public int getQuality ()
  {
    return quality;
  }

  public void setClipPrecision (int clipprecision)
  {
    this.clipprecision = clipprecision;
  }

  public int getClipPrecision ()
  {
    return clipprecision;
  }

  public void setOutputPrecision (int outprecision)
  {
    this.outprecision = outprecision;
  }

  public int getOutputPrecision ()
  {
    return outprecision;
  }

  public void setCharset (int charset)
  {
    this.charset = charset;
  }

  public int getCharset ()
  {
    return charset;
  }

  public void setHeight (int height)
  {
    this.height = height;
    scaleYChanged ();
  }

  public int getHeight ()
  {
    return height;
  }

  public int getScaledHeight ()
  {
    return scaled_height;
  }

  public void setWidth (int width)
  {
    this.width = width;
    scaleXChanged ();
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

  public int getWidth ()
  {
    return width;
  }

  public int getScaledWidth ()
  {
    return scaled_width;
  }

  // in 1/10 degrees
  public void setEscapement (int escapement)
  {
    this.escapement = escapement;
  }

  public int getEscapement ()
  {
    return escapement;
  }

  // in 1/10 degrees
  public void setOrientation (int orientation)
  {
    this.orientation = orientation;
  }

  public int getOrientation ()
  {
    return orientation;
  }

  // 200 = narrow
  // 400 = normal
  // 700 = bold
  public void setWeight (int weight)
  {
    this.weight = weight;
  }

  public int getWeight ()
  {
    return weight;
  }

  public void setItalic (boolean italic)
  {
    this.italic = italic;
  }

  public boolean isItalic ()
  {
    return this.italic;
  }

  public void setUnderline (boolean ul)
  {
    this.underline = ul;
  }

  public boolean isUnderline ()
  {
    return this.underline;
  }

  public void setStrikeout (boolean so)
  {
    this.strikeout = so;
  }

  public boolean isStrikeout ()
  {
    return this.strikeout;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_FONT] face=");
    b.append (getFontFace ());
    b.append (" height=");
    b.append (getHeight ());
    b.append (" width=");
    b.append (getWidth ());
    b.append (" weight=");
    b.append (getWeight ());
    b.append (" italic=");
    b.append (isItalic ());
    b.append (" Strikeout=");
    b.append (isStrikeout ());
    b.append (" Underline=");
    b.append (isUnderline ());
    b.append (" outprecision=");
    b.append (getOutputPrecision ());
    b.append (" escapement=");
    b.append (getEscapement ());
    return b.toString ();
  }
}
