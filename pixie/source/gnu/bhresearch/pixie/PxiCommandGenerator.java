// WviGenerator.java - generate Pxi a file.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.


package gnu.bhresearch.pixie;

import gnu.bhresearch.pixie.command.Comment;
import gnu.bhresearch.pixie.command.FillText;
import gnu.bhresearch.pixie.command.FilledCurve;
import gnu.bhresearch.pixie.command.FilledOval;
import gnu.bhresearch.pixie.command.FilledPolygon;
import gnu.bhresearch.pixie.command.FilledRectangle;
import gnu.bhresearch.pixie.command.Hotspot;
import gnu.bhresearch.pixie.command.ObjectStore;
import gnu.bhresearch.pixie.command.PixieFrame;
import gnu.bhresearch.pixie.command.PixieObject;
import gnu.bhresearch.pixie.command.PixieObjectRef;
import gnu.bhresearch.pixie.command.SetColor;
import gnu.bhresearch.pixie.command.SetFont;
import gnu.bhresearch.pixie.command.StrokeCurve;
import gnu.bhresearch.pixie.command.StrokeOval;
import gnu.bhresearch.pixie.command.StrokePolygon;
import gnu.bhresearch.pixie.command.StrokeRectangle;
import gnu.bhresearch.pixie.image.PixieHeader;
import gnu.bhresearch.pixie.image.PixieImage;
import gnu.bhresearch.quant.Assert;
import gnu.bhresearch.quant.Range;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

/**
 Generate a Pxi file. This class hides the details of the low level output
 format from the main program.

 <P>The "quick" versions of commands usually do some kind of optimisation.
 Most coordinates are stored in relative form.
 */
public class PxiCommandGenerator extends PxiGenerator
{
  private Vector frames;

  private PixieObject currentObject;

  private ObjectStore store;
  private PixieHeader header;
  private Stack defineObjectStack;

  private int logWidth;
  private int logHeight;

  private boolean usedHotspot;
  private boolean usedObject;

  private SetFont curFont;
  private SetFont pendingFont;
  private SetColor curColor;
  private SetColor pendingColor;

  private int prevX; // needed for relative coordinates
  private int prevY; // needed for relative coordinates
  private int maxX;
  private int maxY;

  /** Constructor. */
  public PxiCommandGenerator ()
  {
    frames = new Vector ();
    defineObjectStack = new Stack ();
    store = new ObjectStore ();
  }

//  /** Number of records written so far. */
//  public int getRecordCount()
//  {
//    return currentFrame.getCommandCount ();
//  }

  public void setPixieHeader (PixieHeader header)
  {
    this.header = header;
  }

  public PixieHeader getPixieHeader ()
  {
    return header;
  }

//  /** Number of frames. */
//  public int getFrameCount()
//  {
//    return frames.size();
//  }
//
  /** Current nesting level of defineObject commands. */
  private int getDefineObjectLevel ()
  {
    return defineObjectStack.size ();
  }

  public Dimension getMaximumSize ()
  {
    return new Dimension (maxX, maxY);
  }

  public void setMaxX (int x)
  {
    if (maxX < x)
      maxX = x;
  }

  public void setMaxY (int y)
  {
    if (maxY < y)
      maxY = y;
  }

  /** Size in pixels. */
  public void setLogicalSize (int logWidth, int logHeight)
  {
    Assert.assert (Range.in (0, logWidth, Constants.MAX_LOG_WIDTH));
    Assert.assert (Range.in (0, logHeight, Constants.MAX_LOG_HEIGHT));
    this.logWidth = logWidth;
    this.logHeight = logHeight;
  }

  /**
   * Version number.
   */
  public void setHeaderVersion (short version)
          throws IOException
  {
    header.setVersion (version);
  }

  private void clearRenderState ()
  {
    curFont = null;
    pendingFont = null;
    curColor = null;
    pendingColor = null;
    prevX = 0;
    prevY = 0;
  }

  /**
   * Start a new frame.
   */
  public void startPixieFrame (int pause)
  {
    // Can't nest frames in other frames or objects.
    Assert.assert (currentObject == null);

    // Can't reuse whole frames.
    Assert.assert (getDefineObjectLevel () == 0);

    currentObject = new PixieFrame ();
    frames.addElement (currentObject);
    defineObjectStack.push (currentObject);
    addFramePause (pause);
    clearRenderState ();
  }

  /**
   * Start a new frame.
   */
  public void endPixieFrame ()
  {
    // Ensure correct nesting.
    Assert.assert (currentObject != null);
    System.out.println ("End Frame: " + getDefineObjectLevel ());
    Assert.assert (getDefineObjectLevel () == 1);

    defineObjectStack.pop ();
    setMaxX (currentObject.getWidth ());
    setMaxY (currentObject.getHeight ());
    currentObject = null;

  }

  /**
   * Start recording a re-usable graphics object.
   */
  public void startObjectDef ()
  {
    if (currentObject != null)
    {
      Assert.assert (getDefineObjectLevel () > 0);
      defineObjectStack.push (currentObject);
    }
    currentObject = new PixieObject ();
    clearRenderState ();
  }

  /**
   * End recording a re-usable graphics object.
   */
  public void endObjectDef ()
  {
    Assert.assert (currentObject != null);

    store.addObject (currentObject);
    if (defineObjectStack.size () > 0)
    {
      currentObject = (PixieObject) defineObjectStack.pop ();
    }
    else
    {
      currentObject = null;
    }
  }

  /**
   * Use a previously recorded graphics object.
   */
  public void generateObjectRef (PixieObjectRef idObj)
  {
    currentObject.addCommand (idObj);
    clearRenderState ();
  }

  /** Output a byte-array comment. */
  public void generateComment (Comment cmd)
  {
    currentObject.addCommand (cmd);
  }

  public void generateSetColor (SetColor color)
  {
    if (color == null)
      throw new NullPointerException ("Color given is null");

    curColor = color;
    // If a color change is pending, and the new color is different
    // to the last one output, output it now.
    if (pendingColor != null)
    {
      if (curColor.equals (pendingColor))
      {
        return;
      }
    }
    if (curColor.getColor () == null)
      throw new NullPointerException ();
    System.out.println ("Color changed to " + curColor.getColor ());
    currentObject.addCommand (curColor);
    pendingColor = curColor;
  }

  public void generateSetFont (SetFont font)
  {
    if (font == null)
      throw new NullPointerException ("Font given is null");

    curFont = font;
    // If a color change is pending, and the new color is different
    // to the last one output, output it now.
    if (pendingFont != null)
    {
      if (curFont.equals (pendingFont))
      {
        return;
      }
    }
    currentObject.addCommand (curFont);
    pendingFont = curFont;
  }


  /**
   * Output text if not empty.
   */
  public void generateFillText (FillText text)
  {
    if (text.getText ().length () == 0)
      return;
    currentObject.addCommand (text);
  }

  /**
   * Output a hotspot.
   */
  public void generateHotspot (Hotspot hotspot)
  {
    currentObject.addCommand (hotspot);
  }

  /**
   * Output a filled curve.
   */
  public void generateFilledCurve (FilledCurve curve)
  {
    System.out.println (curve);
    currentObject.addCommand (curve);
  }

  /**
   * Output a stroked curve.
   */
  public void generateStrokeCurve (StrokeCurve curve)
  {
    currentObject.addCommand (curve);
  }

  /**
   * Output a filled Oval.
   */
  public void generateFilledOval (FilledOval oval)
  {
    currentObject.addCommand (oval);
  }

  /**
   * Output a stroked Oval.
   */
  public void generateStrokeOval (StrokeOval oval)
  {
    currentObject.addCommand (oval);
  }

  /**
   * Output a filled Polygon.
   */
  public void generateFilledPolygon (FilledPolygon poly)
  {
    currentObject.addCommand (poly);
  }

  /**
   * Output a stroked Polygon.
   */
  public void generateStrokePolygon (StrokePolygon poly)
  {
    currentObject.addCommand (poly);
  }

  /**
   * Output a filled Rectangle.
   */
  public void generateFilledRectangle (FilledRectangle rectangle)
  {
    currentObject.addCommand (rectangle);
  }

  /**
   * Output a stroked Rectangle.
   */
  public void generateStrokeRectangle (StrokeRectangle rectangle)
  {
    currentObject.addCommand (rectangle);
  }

  public PixieImage getImage ()
  {
    PixieImage image = new PixieImage ();
    image.setHeader (header);
    image.setObjectStore (store);
    for (int i = 0; i < frames.size (); i++)
    {
      image.addFrame ((PixieFrame) frames.elementAt (i), getFramePause (i));
    }
    return image;
  }

}
