// PixieSlide.java - View Pixie Burry Holms Reserch Vector Image Binary files.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.


package gnu.bhresearch.viewer;

import gnu.bhresearch.pixie.Constants;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Container;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 PixieSlide - View Pixie Burry Holms Reserch Vector Image Binary files.

 <P>This is the applet that displays Pixie vector images and slide shows on
 a web page. This code has been manually optimised for space, so it may be
 obscure.

 <P>See getAppletInfo() for copyright and version.
 */
public class PixieSlide extends Applet implements Runnable
{
  public String getAppletInfo ()
  {
    return
            "PixieViewer: View Pixie vector image files\nVersion V1.13 (30th Mar 1998)\nCopyright © David R Harris 1997. All rights reserved.";
  }

  /*
  public String[][] getParameterInfo() {
  String info[][] = {
  { "Src",			"String",	"URL of image" },
  { "BGColor",		"#rrggbb",	"Background color" },
  { "GrabFocus",		"",			"Use keyboard" },
  };
  return info;
  }
  */

  /** Initialise - start a thread for download and animation. */
  public void init ()
  {
    //Debug.setType( Debug.WINDOW );
    new Thread (this).start ();
  }

  /** Update feedback message. */
  private final void setMessage (String message)
  {
    this.message = message;
    showStatus (message);
    imageDrawn = false;
    repaint ();
  }

  /** Highest file version that we understand. */
  private final static short FILE_VERSION_MIN = 300;
  private final static short FILE_VERSION_MAX = 304;

  private byte[] pixieBody = null;
  private int[] frameTable = null;
  private int[] framePause = null;
  private int[] objectTable = null;
  private int pixieWidth = 0;
  private int pixieHeight = 0;
  private boolean isPaused = false;
  private float progress = 0;
  private int currentFrame = 0;
  private long nextFrameDue = 0;
  private static final int AUTO_NEXT_FRAME = Constants.HOT_SPOT_MIN - 1;

  /** Fetch image filename and load it, then go into animation loop. */
  public void run ()
  {
    // Fetch image filename and load it.
    String path = null;
    if (getColorModel ().getPixelSize () <= 8)
      path = getParameter ("SRC8");
    if (path == null)
      path = getParameter ("SRC");

    if (path == null)
      setMessage ("No SRC parameter");
    else
    {
      try
      {
        URL url = new URL (getDocumentBase (), path);
        showStatus ("Loading " + url);
        loadPixie (url);

        synchronized (this)
        {
          currentFrame = -1;
          while (true)
          { // Do animation.
            doAction (AUTO_NEXT_FRAME, null);
            while (true)
            {
              long pause = nextFrameDue - System.currentTimeMillis ();
              if (pause <= 0)
                break;

              try
              {
                wait (pause);
              }
              catch (InterruptedException e)
              {
              }
            }
          }
        }
      }
      catch (Exception e)
      {
        // Try to notify main thread if anything at all fails.
        setMessage (path + ": " + e);
      }
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
    if (v < FILE_VERSION_MIN || v >= FILE_VERSION_MAX)
      throw new Exception ("Bad Pixie version " + v);

    pixieWidth = readUnsignedVInt ();
    pixieHeight = readUnsignedVInt ();

    // Skip unused fields.
    readUnsignedVInt ();
    readUnsignedVInt ();

    // Frame table.
    int frameCount = readUnsignedVInt ();
    frameTable = new int[frameCount];
    framePause = new int[frameCount];
    for (int i = 0, prev = 0; i < frameCount; i++)
    {
      frameTable[i] = prev += readUnsignedVInt ();
      framePause[i] = (i == 0) ? -1 : framePause[i - 1];

      // Interpret frame control commands.
      loop:
        while (true)
          switch (readUnsignedVInt ())
          {
            default:
              throw new Exception ("Unknown header command");
            case Constants.CTL_END:
              break loop;
            case Constants.CTL_PAUSE:
              framePause[i] = readVInt ();
              break;
          }
    }

    // Object table
    int objectCount = readUnsignedVInt ();
    objectTable = new int[objectCount];
    for (int i = 0, prev = 0; i < objectCount; i++)
      objectTable[i] = prev += readUnsignedVInt ();

    // Skip unused fields.
    readUnsignedVInt ();
    readUnsignedVInt ();

    // Block-read the rest.
    int byteCount = readUnsignedVInt ();
    pixieBody = new byte[byteCount];
    int initialByteCount = (frameCount > 1) ? frameTable[1] : byteCount;

    // Use a local variable, so that the instance variable 'in' is
    // free for the other thread.
    flushVInt ();
    DataInputStream inCopy = in;
    in = null;
    int maxBlockSize = byteCount / 20 + 1;
    Graphics fg = getGraphics ();

    int bytesDone = 0;
    while (bytesDone < byteCount)
    {
      int blockSize = byteCount - bytesDone;
      if (blockSize > maxBlockSize)
        blockSize = maxBlockSize;
      inCopy.readFully (pixieBody, bytesDone, blockSize);
      bytesDone += blockSize;

      // Update progress bar.
      progress = bytesDone / (float) byteCount;
      if (fg != null)
      {
        fg.setColor (getForeground ());
        fg.fillRect (0, size ().height - 4,
                (int) (progress * size ().width), 4);
      }

      // Show first image as soon as it's loaded.
      if (bytesDone >= initialByteCount)
        if (message != null)
        {
          setMessage (null);
          // Give other thread a chance to update.
          // We don't much care if this isn't long enough.
          Thread.sleep (100);
        }
    }

    if (fg != null)
      fg.dispose ();
    inCopy.close ();
    progress = 0; // Switch off progress bar.
  }

  public void paint (Graphics g)
  {
    update (g);
  }

  private volatile Image fgImage = null;
  private volatile Image bgImage = null;
  private String message = "Loading Pixie Graphic...";
  private int imageWidth = 0;
  private int imageHeight = 0;
  private boolean imageDrawn = false;

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
      fetchParams ();
      fgImage = createImage (iWidth, iHeight);
      bgImage = createImage (iWidth, iHeight);
      imageDrawn = false;
      bgOffset = -1;
    }

    if (!imageDrawn)
    {
      imageDrawn = true;
      // Erase to background color.
      Graphics fg = fgImage.getGraphics ();
      fg.setColor (getBackground ());
      fg.fillRect (0, 0, iWidth, imageHeight);
      fg.setColor (getForeground ());

      if (message != null)
      {
        // Display progress/error message.
        fg.drawString (message, 1,
                iHeight - 5 - fg.getFontMetrics ().getDescent ());
      }
      else
      {
        try
        {
          scaleXy[0] = iWidth / (float) pixieWidth;
          scaleXy[1] = iHeight / (float) pixieHeight;
          hotSpotCount = 0;
          draw (fg, frameTable[currentFrame], true);
        }
        catch (Exception e)
        {
          setMessage (e.getMessage ());
        }
      }

      // Draw progress bar.
      fg.setColor (getForeground ());
      fg.fillRect (0, iHeight - 4,
              (int) (progress * iWidth), 4);

      fg.dispose ();
    }

    // Draw final image.
    g.drawImage (fgImage, 0, 0, this);
    //getToolkit().sync();
  }

  private void fetchParams ()
  {
    try
    {	// Background color.
      // Parse color from "#rrggbb".
      int color = Integer.parseInt (
              getParameter ("BGColor").substring (1), 16);
      setBackground (new Color (color));
      setForeground (new Color (~color));
    }
    catch (Exception e)
    {
    }

    // Don't like to do this because it screws up keyboard
    // navigation.
    if (getParameter ("GrabFocus") != null)
      requestFocus ();
  }


  private Graphics graphics;   // For drawing on.
  private float[] scaleXy = new float[2];		// Logical to pixel scaling.
  private int[] prevXy = new int[2];

  /** Draw a frame. */
  private void draw (Graphics g, int off, boolean isBlank)
          throws Exception
  {
    ByteArrayInputStream inBytes = new ByteArrayInputStream (pixieBody,
            off, pixieBody.length - off);
    DataInputStream oldIn = in;
    in = new DataInputStream (inBytes);
    flushVInt ();

    graphics = g;
    prevXy[0] = prevXy[1] = 0;

    while (true)
    {
      int cmd = readUnsignedVInt ();
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
        case Constants.CMD_USE_OBJECT:
          parseUseObject (isBlank);
          break;
        case Constants.CMD_COMMENT:
          parseComment ();
          break;
        case Constants.CMD_END:
          in = oldIn;
          return;
        case Constants.CMD_SET_FONT:
          parseSetFont ();
          break;
        case Constants.CMD_FILL_TEXT:
          parseFillText ();
          break;
        case Constants.CMD_HOT_SPOT:
          parseHotSpot ();
          break;
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
      isBlank = false;
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
    //int i = readUnsignedVInt();
    //Color color = new Color( i );
    //graphics.setColor( color );
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
    Rectangle r = parseRectangle ();
    int x = r.x;
    int y = r.y;
    int width = r.width;
    int height = r.height;
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

  private Rectangle parseRectangle () throws IOException
  {
    return new Rectangle (
            readScaledVInt (0),
            readScaledVInt (1),
            readLength (0),
            readLength (1));
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

    // Remove duplicates eg due to scaling.
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

    // Draw.
    if (isFill)
      graphics.fillPolygon (x, y, cleanCnt);
    else if (x[0] == x[cleanCnt - 1] && y[0] == y[cleanCnt - 1])
      graphics.drawPolygon (x, y, cleanCnt);
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
    while (true)
    {
      // Read the next 4-bit nibble.
      int nibble;
      if (nibBufFull)
      {
        nibBufFull = false;
        nibble = nibBuf >> 4;
      }
      else
      {
        nibBufFull = true;
        nibBuf = in.readUnsignedByte ();
        nibble = nibBuf & 0x00f;
      }

      // Extract low 3 bits from nibble.
      result = (result << 3) | (nibble & 0x07);

      // Last nibble?
      if ((nibble & 0x08) == 0)
        return result;
    }
  }

  // We have a simple cache which stores the bitmap of the last object
  // used, provided there is nothing draw underneath it. This is
  // intended to make common backgrounds faster.

  private int bgOffset = -1; // Position of cached image,

  private void parseUseObject (boolean isBlank)
          throws Exception
  {
    int offset = objectTable[readUnsignedVInt ()];
    Rectangle box = parseRectangle ();
    float sx = box.width / (float) imageWidth;
    float sy = box.height / (float) imageHeight;
    boolean isBackground = isBlank && sx == 1 && sy == 1 && box.x == 0 && box.y == 0;

    if (isBackground && bgOffset == offset)
    {
      // Use cache.
      graphics.drawImage (bgImage, 0, 0, this);
    }
    else
    {
      float oldScaleX = scaleXy[0];
      float oldScaleY = scaleXy[1];
      graphics.translate (box.x, box.y);
      scaleXy[0] *= sx;
      scaleXy[1] *= sy;
      draw (graphics, offset, false);
      scaleXy[0] = oldScaleX;
      scaleXy[1] = oldScaleY;
      graphics.translate (-box.x, -box.y);
      if (isBackground && hotSpotCount == 0)
      {
        // Store cache. This is the first object to be drawn.
        bgOffset = offset;
        Graphics bg = bgImage.getGraphics ();
        bg.drawImage (fgImage, 0, 0, this);
        bg.dispose ();
      }
    }
    flushVInt ();
    prevXy[0] = prevXy[1] = 0;
    // Note at this point the state of the drawing engine is
    // undefined as far as the current color, font and offset are
    // concerned.
  }

  // HotSpots for clickable images.
  private static final int MaxHotSpots = 100;
  private int hotSpotCount = 0;
  private int[] hotSpotCmds = new int[MaxHotSpots];
  private String[] hotSpotArgs = new String[MaxHotSpots];
  private Rectangle[] hotSpots = new Rectangle[MaxHotSpots];

  private void parseHotSpot () throws IOException
  {
    flushVInt ();
    hotSpotArgs[hotSpotCount] = in.readUTF ();
    hotSpots[hotSpotCount] = parseRectangle ();
    hotSpotCmds[hotSpotCount] = readVInt ();
    hotSpotCount++;
  }

  private int lastHotSpot = -1;

  /** Clicking on hotspots. */
  public boolean handleEvent (Event e)
  {
    int hotSpot = -1;
    int cmd = Constants.HOT_SPOT_TOGGLE_PAUSE;
    String arg = null;
    for (hotSpot = hotSpotCount - 1; hotSpot >= 0; hotSpot--)
      if (hotSpots[hotSpot].inside (e.x, e.y))
      {
        cmd = hotSpotCmds[hotSpot];
        arg = hotSpotArgs[hotSpot];
        if (getParameter (arg) != null)
          arg = getParameter (arg);
        break;
      }

    switch (e.id)
    {
      default:
        return super.handleEvent (e);

      case Event.MOUSE_MOVE:
        break;

      case Event.MOUSE_DOWN:
        lastHotSpot = hotSpot;
        break;

      case Event.MOUSE_DRAG:
        if (hotSpot == lastHotSpot)
          break;
        // Fall through.
      case Event.MOUSE_EXIT:
        cmd = Constants.HOT_SPOT_NO_OP;
        arg = null;
        break;

      case Event.MOUSE_UP:
        if (hotSpot == lastHotSpot)
          doAction (cmd, arg);
        return true;

      case Event.KEY_PRESS:
        doAction (cmd, arg);
        return true;
    }

    // Rather dodgy attempt to set the cursor, which in Awt 1.0.2 can
    // only be done for a frame.
    for (Container c = this; c != null; c = c.getParent ())
      if (c instanceof Frame)
      {
        ((Frame) c).setCursor (
                (cmd == Constants.HOT_SPOT_URL) ?
                Frame.HAND_CURSOR : Frame.DEFAULT_CURSOR);
        break;
      }

    showStatus ((cmd == Constants.HOT_SPOT_URL) ? "Shortcut to " + arg : arg);
    return true;
  }

  private synchronized void doAction (int cmd, String arg)
  {
    int lastFrame = frameTable.length - 1;
    int firstFrame = (framePause[0] == 0 && lastFrame > 0) ? 1 : 0;
    boolean wantPause = false;

    if (cmd == Constants.HOT_SPOT_TOGGLE_PAUSE)
      cmd = isPaused ? Constants.HOT_SPOT_NEXT_FRAME : Constants.HOT_SPOT_STOP;

    int nextFrame = currentFrame;
    switch (cmd)
    {
      //case Constants.HOT_SPOT_FIRST:
      //	Assert,assert( cmd == 0 );
      //	Fall through to default case.
      default:
        // Use cmd itself as the next frame index.
        // Clip to valid range rather than wrap.
        nextFrame =
                (cmd < firstFrame) ? firstFrame :
                (cmd > lastFrame) ? lastFrame : cmd;
        break;
      case Constants.HOT_SPOT_URL:
        try
        {
          getAppletContext ().showDocument (
                  new URL (getDocumentBase (), arg));
        }
        catch (Exception e)
        {
        }
        // If we reach here, the URL didn't work.
        return;
      case Constants.HOT_SPOT_STOP:
        wantPause = true;
        break;
      case Constants.HOT_SPOT_NEXT_FRAME:
      case AUTO_NEXT_FRAME:
        if (++nextFrame > lastFrame)
          nextFrame = firstFrame;
        break;
      case Constants.HOT_SPOT_PREVIOUS_FRAME:
        if (--nextFrame < firstFrame)
          nextFrame = lastFrame;
        wantPause = true;
        break;
      case Constants.HOT_SPOT_LAST_FRAME:
        nextFrame = lastFrame;
        break;
      case Constants.HOT_SPOT_NO_OP:
        return;
    }

    currentFrame = nextFrame;
    isPaused = (wantPause || framePause[currentFrame] < 0);
    nextFrameDue = isPaused ?
            Long.MAX_VALUE :
            System.currentTimeMillis () + framePause[currentFrame];

    imageDrawn = false;
    repaint ();
    notifyAll ();
  }
}
