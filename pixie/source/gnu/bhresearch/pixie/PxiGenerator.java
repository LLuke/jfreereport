// WviGenerator.java - generate Pxi a file.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.


package gnu.bhresearch.pixie;

import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.util.Vector;
import java.util.Stack;
import gnu.bhresearch.pixie.image.*;
import gnu.bhresearch.pixie.command.*;
import gnu.bhresearch.quant.Debug;
import gnu.bhresearch.quant.Assert;
import gnu.bhresearch.quant.Range;

/**
 * Generate a Pxi file. This class hides the details of the low level output
 * format from the main program.
 *
 * <P>The "quick" versions of commands usually do some kind of optimisation.
 * Most coordinates are stored in relative form.
 */
public abstract class PxiGenerator 
{
  private Vector framePauses;

  /** 
   * Constructor. 
   */
  public PxiGenerator()
  {
    framePauses = new Vector ();
  }

//  /** 
//   * Number of records written so far. 
//   */
//  public abstract int getRecordCount();
//
//  /** 
//   * Number of frames. 
//   */
//  public abstract int getFrameCount();
//
//  /** 
//   * Current nesting level of defineObject commands. 
//   */
//  public abstract int getDefineObjectLevel();
//
//  /** 
//   * Size in pixels. 
//   */
//  public void setLogicalSize( Rectangle box )
//  {
//    setLogicalSize( box.width, box.height );
//  }
//
//  /** 
//   * Size in pixels. 
//   */
//  public abstract void setLogicalSize( int logWidth, int logHeight );
//
//  /** 
//   * Version number. 
//   */
//  public abstract void setHeaderVersion( int version ) throws IOException;

  public abstract Dimension getMaximumSize ();

  /** 
   * Pause between given frame and the next. 
   */
  public void setFramePause( int frame, int pause )
  {
    framePauses.setElementAt( new Integer( pause ), frame );
  }

  public void addFramePause (int pause)
  {
    framePauses.addElement(new Integer (pause));
  }

  /** 
   * Pause between given frame and the next. 
   */
  public int getFramePause( int frame )
  {
    return ((Integer)framePauses.elementAt( frame )).intValue();
  }

  /** Return approx bounding box of the text. */
  public Rectangle getTextBox(Font curFont, String text, int x, int y, boolean centered )
  {
    int size = curFont.getSize();
    int width = text.length() * size / 2;
    Rectangle box = new Rectangle();
    box.x = x;
    box.y = y - size;
    box.width = width;
    box.height = size + size/3;
    if (centered)
      box.x -= box.width/2;
    return box;
  }

  private final static String fontMap[] = 
  {
		// Windows fonts.
		"Arial",				"Helvetica",
		"Times New Roman",		"TimesRoman",
		"Courier New",			"Courier",
		"MS Sans Serif",		"Dialog",
		"WingDings",			"ZapfDingBats",

		// X fonts.
		"adobe-helvetica",		"Helvetica",
		"adobe-times",			"TimesRoman",
		"adobe-courier",		"Courier",
		"b&h-lucida",			"Dialog",
		"b&h-lucidatypewriter",	"DialogInput",
		"itc-zapfdingbats",		"ZapfDingBats"
	};

  /** Return the Java name of a font. */
  public String mapFont( String face )
  {
    for (int i = 0; i < fontMap.length; i += 2)
    {
      if (fontMap[i].equalsIgnoreCase( face ))
      {
        return fontMap[i+1];
      }
    }  
    return face;
  }

  /** Is this a standard Java font? */
  public boolean isKnownFont( String face )
  {
    for (int i = 0; i < fontMap.length; i++)
    {
      if (fontMap[i].equals( face ))
      {
        return true;
      }
    }
    return false;
  }
  
  public abstract void generateComment (Comment cmd);
  public abstract void generateFilledCurve (FilledCurve cmd);
  public abstract void generateFilledOval (FilledOval cmd);
  public abstract void generateFilledPolygon (FilledPolygon cmd);
  public abstract void generateFilledRectangle (FilledRectangle cmd);
  public abstract void generateFillText (FillText cmd);

  public abstract void generateSetColor (SetColor cmd);
  public abstract void generateSetFont (SetFont cmd);
  public abstract void generateStrokeOval (StrokeOval cmd);
  public abstract void generateStrokePolygon (StrokePolygon cmd);
  public abstract void generateStrokeRectangle (StrokeRectangle cmd);
  public abstract void generateStrokeCurve (StrokeCurve cmd);

  public abstract void startPixieFrame (int pause);
  public abstract void endPixieFrame ();
  
  public abstract void startObjectDef ();
  public abstract void endObjectDef ();
  
  public abstract void generateHotspot (Hotspot cmd);
  public abstract void generateObjectRef (PixieObjectRef ref);
  
  public abstract void setPixieHeader (PixieHeader header);
  public abstract PixieHeader getPixieHeader ();
}
