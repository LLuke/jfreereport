package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfLogFont;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Font;

/**
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

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfLogFont lfont = new org.jfree.pixie.wmf.MfLogFont ();
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

  public MfCmd getInstance ()
  {
    return new MfCmdCreateFont ();
  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    String fontFace = getFontFace();
    if (fontFace.length() > 31)
    {
      fontFace = fontFace.substring(0, 31);
    }
    org.jfree.pixie.wmf.MfRecord record = new org.jfree.pixie.wmf.MfRecord(9 + fontFace.length());
    record.setParam(0, getHeight());
    record.setParam(1, getWidth());
    record.setParam(2, getEscapement());
    record.setParam(3, getOrientation());
    record.setParam(4, getWeight());

    record.setParam(5, formFlags(isUnderline(), isItalic()));
    record.setParam(6, formFlags(isStrikeout(), false) + getCharset());
    record.setParam(7, getOutputPrecision() << 8 + getClipPrecision());
    record.setParam(8, getQuality() << 8 + getPitchAndFamily());
    record.setStringParam(9, fontFace);
    return record;
  }

  private int formFlags (boolean f1, boolean f2)
  {
    int retval = 0;
    if (f1) retval += 0x0100;
    if (f2) retval += 1;
    return (retval);
  }
  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int height = record.getParam (0);
    if (height < 0)
      height *= -1;

    int width = record.getParam (1);
    int escape = record.getParam (2);
    int orientation = record.getParam (3);
    int weight = record.getParam (4);
    int italic = record.getParam (5) & 0x00FF;
    int underline = record.getParam (5) & 0xFF00;
    int strikeout = record.getParam (6) & 0xFF00;
    int charset = record.getParam (6) & 0x00FF;
    int outprec = record.getParam (7) & 0xFF00;
    int clipprec = record.getParam (7) & 0x00FF;
    int quality = record.getParam (8) & 0xFF00;
    int pitch = record.getParam (8) & 0x00FF;
    // A fontname must not exceed the length of 32 including the null-terminator
    String facename = record.getStringParam (9, 32);

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

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.CREATE_FONT_INDIRECT;
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

  protected void scaleXChanged ()
  {
    scaled_width = getScaledX (width);
  }

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
