// Pixie.java - View .PXI Pixie Burry Holms Research Vector Image Binary files.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

//package gnu.bhresearch.pixie;

import gnu.bhresearch.pixie.Constants;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 View .PXI Pixie Burry Holms Research Vector Image Binary files.

 <P>This is the applet that displays pixie vector images and animations on
 a web page.

 <P>This version is pretty minimal. It only handles v3.00 files, that is to
 say, single frame, no hotspots or object tables. This code has been
 manually optimised for space, so it may be obscure.

 <P>See getAppletInfo() for copyright and version.
 */
public class Pixie extends Applet implements Runnable
{
  public String getAppletInfo ()
  {
    return
            "Pixie: View .PXI vector images. V1.13 (30th Mar 1998)\nCopyright © David R Harris 1997. All rights reserved.";
  }
  /*
  public String[][] getParameterInfo() {
  String info[][] = {
  { "Src",			"String",	"URL of image" },
  { "BGColor",		"#rrggbb",	"Background color" }
  };
  return info;
  }
  */

  public static void main (String[] args)
  {
    Pixie p = new Pixie ();
    Frame f = new Frame ();
    f.add (p);
    f.setSize (1024, 768);
    f.show ();
    p.run ();
  }

  /** Initialise - start download thread. */
  public void init ()
  {
    new Thread (this).start ();
  }

  /** Update feedback message. */
  private final void setMessage (String message)
  {
    this.message = message;
    //		showStatus( message );
    repaint ();
  }

  /** Highest file version that we understand. */
  private final static short FILE_VERSION = 300;

  private byte[] pixieBody = null;
  private int frame = 0;
  private int pixieWidth = 0;
  private int pixieHeight = 0;
  private float progress = 0;

  /** Fetch image filename and load it. */
  public void run ()
  {
    String path = null;
    //		if (getColorModel().getPixelSize() <= 8)
    //			path = getParameter( "SRC8" );
    //		if (path == null)
    //			path = getParameter( "SRC" );

    try
    {
      URL url = new File ("./pixie/res/PixiLogo.pxi").toURL ();
      loadPixie (url);
    }
    catch (Exception e)
    {
      // Try to notify main thread if anything at all fails.
      e.printStackTrace ();
      setMessage (path + ": " + e);
    }

  }

  private DataInputStream in;

  /** Load a pixie file from an URL. */
  private void loadPixie (URL url) throws Exception
  {
    URLConnection connection = url.openConnection ();
    connection.setUseCaches (true);
    in = new DataInputStream (connection.getInputStream ());

    // Verify file format via magic numbers and version number.
    if (in.readInt () != Constants.MAGIC)
      throw new Exception ("Bad Pixie header");
    int v = in.readShort ();
    if (v != FILE_VERSION)
      throw new Exception ("Bad Pixie version " + v);

    pixieWidth = readUnsignedVInt ();
    pixieHeight = readUnsignedVInt ();

    // Skip unused fields.
    readUnsignedVInt ();
    readUnsignedVInt ();

    readUnsignedVInt ();	// Frame table size always 1.
    frame = readUnsignedVInt ();
    readUnsignedVInt ();	// Skip end of control commands.

    // Object table size always 0.
    readUnsignedVInt ();

    // Skip unused fields.
    readUnsignedVInt ();
    readUnsignedVInt ();

    // Block-read the rest.
    int byteCount = readUnsignedVInt ();
    pixieBody = new byte[byteCount];
    int maxBlockSize = byteCount / 20 + 1;
    flushVInt ();
    Graphics fg = getGraphics ();

    int bytesDone = 0;
    while (bytesDone < byteCount)
    {
      int blockSize = byteCount - bytesDone;
      if (blockSize > maxBlockSize)
        blockSize = maxBlockSize;
      in.readFully (pixieBody, bytesDone, blockSize);
      bytesDone += blockSize;

      // Update progress monitor.
      if (fg != null)
      {
        progress = bytesDone / (float) byteCount;
        fg.setColor (getForeground ());
        fg.fillRect (0, size ().height - 4,
                (int) (progress * size ().width), 4);
      }
    }

    if (fg != null)
      fg.dispose ();
    in.close ();
    setMessage (null);
  }

  public void paint (Graphics g)
  {
    update (g);
  }

  private volatile Image fgImage = null;
  private String message = "Loading Pixie Graphic...";
  private int imageWidth = 0;
  private int imageHeight = 0;
  private int bgColor = 0;

  public void update (Graphics g)
  {
    int iWidth = size ().width;
    int iHeight = size ().height;

    if (fgImage == null)
    {
      // Postpone initialisation until we are sure we have the
      // right size, but only do it once.
      imageWidth = iWidth;
      imageHeight = iHeight;
      fgImage = createImage (iWidth, iHeight);

      try
      {	// Background color.
        // Parse color from "#rrggbb".
        bgColor = Integer.parseInt (
                getParameter ("BGColor").substring (1), 16);
      }
      catch (Exception e)
      {
        bgColor = getBackground ().getRGB ();
      }
      setBackground (new Color (bgColor));
      setForeground (new Color (~bgColor));
    }

    if (message != null || scaleXy[0] == 0)
    { // Ie if bitmap not valid.
      // Erase to background color.
      Graphics fg = fgImage.getGraphics ();
      fg.setColor (getBackground ());
      fg.fillRect (0, 0, iWidth, imageHeight);
      fg.setColor (getForeground ());

      if (message != null)
      {
        // Display progress/error message.
        //fg.drawRect( 0, 0, iWidth-1, iHeight-1 );
        fg.drawString (message, 1,
                iHeight - 5 - fg.getFontMetrics ().getDescent ());

        // Progress bar.
        fg.fillRect (0, iHeight - 4,
                (int) (progress * iWidth), 4);
      }
      else
      {
        try
        {
          scaleXy[0] = 800 / (float) pixieWidth;
          scaleXy[1] = 600 / (float) pixieHeight;
          System.out.println ("reader.size " + iWidth);
          System.out.println ("reader.size " + iHeight);
          System.out.println ("reader.scale " + scaleXy[0]);
          System.out.println ("reader.scale " + scaleXy[1]);
          draw (fg, frame);
        }
        catch (Exception e)
        {
          setMessage (e.getMessage ());
        }
      }

      fg.dispose ();
    }

    // Draw final image.
    g.drawImage (fgImage, 0, 0, this);
  }

  private Graphics graphics;   // For drawing on.
  private float[] scaleXy = new float[2];		// Logical to pixel scaling.
  private int[] prevXy = new int[2];

  /** Draw a frame. */
  private void draw (Graphics g, int off)
          throws Exception
  {
    graphics = g;
    System.out.println ("Seeking " + off + " = " + pixieBody[off]);
    in = new DataInputStream (
            new ByteArrayInputStream (
                    pixieBody, off, pixieBody.length - off));
    flushVInt ();
    prevXy[0] = prevXy[1] = 0;

    while (true)
    {
      int cmd = readUnsignedVInt ();
      System.out.println ("Command: " + cmd);
      switch (cmd)
      {
        default:	// 0 and other numbers not used.
          throw new Exception ("Unknown record type " + cmd);
        case Constants.CMD_SET_COLOR:
          parseSetColor ();
          break;
        case Constants.CMD_STROKE_CURVE:
          parseFillCurve (false, true);
          break;
        case Constants.CMD_FILL_CURVE:
          parseFillCurve (true, true);
          break;
        case Constants.CMD_STROKE_POLYLINE:
          parseFillCurve (false, false);
          break;
        case Constants.CMD_FILL_POLYGON:
          parseFillCurve (true, false);
          break;
          //case Constants.CMD_USE_OBJECT:
          //	parseUseObject( isBlank );
          //	break;
        case Constants.CMD_COMMENT:
          parseComment ();
          break;
        case Constants.CMD_END:
          //in = oldIn;
          return;
        case Constants.CMD_SET_FONT:
          parseSetFont ();
          break;
        case Constants.CMD_FILL_TEXT:
          parseFillText ();
          break;
          //case Constants.CMD_HOT_SPOT:
          //	parseHotSpot();
          //	break;
        case Constants.CMD_STROKE_ELLIPSE:
          parseFillRectangle (false, false);
          break;
        case Constants.CMD_FILL_ELLIPSE:
          parseFillRectangle (true, false);
          break;
        case Constants.CMD_STROKE_RECTANGLE:
          parseFillRectangle (false, true);
          break;
        case Constants.CMD_FILL_RECTANGLE:
          parseFillRectangle (true, true);
          break;
      }
    }
  }

  /** Ignore following bytes */
  private void parseComment () throws IOException
  {
    readUnsignedVInt ();		// Ignore type.
    int len = readUnsignedVInt ();
    flushVInt ();
    in.skipBytes (len);	// Ignore everything.
  }


  /** Set color used for future drawing. */
  private void parseSetColor () throws IOException
  {
    graphics.setColor (new Color (readUnsignedVInt ()));
  }

  /** Set the font used for future text. */
  private void parseSetFont () throws IOException
  {
    flushVInt ();
    graphics.setFont (new Font (
            in.readUTF (),
            readUnsignedVInt (),
            readLength (1)));
  }

  /** Draw some text. */
  private void parseFillText () throws IOException
  {
    flushVInt ();
    String text = in.readUTF ();
    int x = readScaledVInt (0);
    int y = readScaledVInt (1);
    int flags = readUnsignedVInt ();

    System.out.println ("Text: " + x + " " + y + " " + flags);


    FontMetrics metrics = graphics.getFontMetrics ();
    int textWidth = metrics.stringWidth (text);
    if ((flags & Constants.TEXT_CENTER) != 0)
      x -= textWidth / 2;
    else if ((flags & Constants.TEXT_RIGHT) != 0)
      x -= textWidth;
    graphics.drawString (text, x, y);

    if ((flags & Constants.TEXT_UNDERLINE) != 0)
    {	// Underline.
      y += metrics.getDescent () / 8 + 1;
      graphics.drawLine (x, y, x + textWidth, y);
    }
  }

  /** Draw a rectangle or ellipse. */
  private void parseFillRectangle (boolean isFill, boolean isRect)
          throws IOException
  {
    int x = readScaledVInt (0);
    int y = readScaledVInt (1);
    int width = readLength (0);
    int height = readLength (1);

    System.out.println ("Rec: " + isFill + " " + isRect);
    System.out.println ("Rect: " + x + " " + y + " " + width + " " + height);


    if (isFill)
      if (isRect)
        graphics.fillRect (x, y, width, height);
      else
        graphics.fillOval (x, y, width, height);
    else if (isRect)
      graphics.drawRect (x, y, width, height);
    else
      graphics.drawOval (x, y, width, height);
  }

  private int readLength (int isX) throws IOException
  {
    // A 0 width or height means use the size of the image.
    // Scaling should not result in a zero size.
    int width = readUnsignedVInt ();
    if (width == 0)
      width = (isX == 0) ? pixieWidth : pixieHeight;
    prevXy[isX] += width;
    width = (int) (width * scaleXy[isX] + 0.5f);
    return (width == 0) ? 1 : width;
  }


  // Number of points each bezier expands to.
  private static final int steps = 32;
  private static final float step = 1.0f / steps;

  /** Curves. */
  private void parseFillCurve (boolean isFill, boolean isCurve)
          throws IOException
  {
    // Number of handles.
    int hCnt = readUnsignedVInt ();

    // Read types array. A run length encoded array of boolean.
    boolean ht[] = new boolean[hCnt];
    int outCnt = 0;
    if (isCurve)
    {
      boolean isBezier = false;
      for (int h = 0; h < hCnt;)
      {
        int runLen = readUnsignedVInt ();
        outCnt += isBezier ? runLen * steps : runLen;
        while (runLen-- > 0)
          ht[h++] = isBezier;
        isBezier = !isBezier;
      }
    }
    else
      outCnt = hCnt;

    // Read points and expand beziers.
    int x[] = readPoints (outCnt, ht, 0);
    int y[] = readPoints (outCnt, ht, 1);

    // Remove duplicates.
    int cleanCnt = 1;
    for (int i = 1; i < outCnt; i++)
    {
      if (x[cleanCnt - 1] != x[i] || y[cleanCnt - 1] != y[i])
      {
        x[cleanCnt] = x[i];
        y[cleanCnt] = y[i];
        cleanCnt++;
      }
    }

    System.out.println ("Curve: " + isFill + " " + isCurve);
    for (int i = 0, j = 1; j < cleanCnt; i++, j++)
    {
      System.out.println ("Curve: " + x[i] + " " + y[i]);
    }

    // Draw.
    if (isFill)
      graphics.fillPolygon (x, y, cleanCnt);
    //else if (x[0] == x[cleanCnt-1] && y[0] == y[cleanCnt-1])
    //    graphics.drawPolygon( x, y, cleanCnt );
    else
    {
      // Awt 1.0.2 does not support drawPolyline
      for (int i = 0, j = 1; j < cleanCnt; i++, j++)
        graphics.drawLine (x[i], y[i], x[j], y[j]);
    }
  }

  /** Read points and expand beziers. We avoid Math.round(), even though
   it would giver better results for negative numbers, because it is
   slow. */
  private int[] readPoints (int outCnt, boolean[] ht, int isX)
          throws IOException
  {
    int prev = prevXy[isX];
    float scale = scaleXy[isX];
    int dest[] = new int[outCnt];
    for (int h = 0, dOffset = 0; dOffset < outCnt; h++)
    {
      if (ht[h])
      {
        // Bezier consumes 3 handles.
        float s0 = prev;
        float s1 = prev += readVInt ();
        float s2 = prev += readVInt ();
        float s3 = prev += readVInt ();

        float k1 = -3.0f * (s0 - s1);
        float k2 = 3.0f * ((s0 + s2) - 2.0f * s1);
        float k3 = -s0 + 3.0f * (s1 - s2) + s3;

        for (float t = step; t <= 1.0f; t += step)
          dest[dOffset++] = (int) (scale * (s0 + t * (k1 + t * (k2 + t * k3))) + 0.5f);
      }
      else
      {
        // Simple straight line segment.
        prev += readVInt ();
        dest[dOffset++] = (int) (scale * prev + 0.5f);
      }
    }
    prevXy[isX] = prev;
    return dest;
  }

  /** Load variable length integers from a nibble format -
   4 bits to a nibble. */

  /**	Second half of nibble from a byte. */
  private int nibBuf = 0;
  private boolean nibBufFull = false;

  private void flushVInt ()
  {
    System.out.println ("Flushing");
    nibBufFull = false;
  }

  /** Return integer scaled to output units. */
  private int readScaledVInt (int isX) throws IOException
  {
    //return Math.round( readVInt( true ) * scale );
    return (int) ((prevXy[isX] += readVInt ()) * scaleXy[isX] + 0.5f);
  }

  /** Return signed integer. */
  private int readVInt () throws IOException
  {
    int result = readUnsignedVInt ();
    // Sign is stored in lowest bit.
    return (((result & 0x01) == 0) ? result : -result - 1) >> 1;
  }

  /** Return unsigned integer. The core variable length int routine. */
  private int readUnsignedVInt () throws IOException
  {
    int result = 0;
    //    System.out.println ("RUV: " + nibBufFull);
    while (true)
    {
      // Read the next 4-bit nibble.
      int nibble;
      if (nibBufFull)
      {
        nibBufFull = false;
        // Use previously read nibble.
        nibble = nibBuf;
      }
      else
      {
        nibBufFull = true;
        // Use the low bits of a new byte, and save
        // the high bits for next time.
        nibble = in.readByte ();
        //        System.out.println ("In REadByte: " + nibble);
        nibBuf = nibble >> 4;
      }

      // Extract low 3 bits from nibble.
      result = (result << 3) | (nibble & 0x07);

      // Last nibble?
      if ((nibble & 0x08) == 0)
      {
        //        		System.out.println ("Read Unsigned VInt : " + result);
        return result;
      }
    }
  }
}
