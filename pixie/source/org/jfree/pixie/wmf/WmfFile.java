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
 * WmfFile.java
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
package org.jfree.pixie.wmf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Arrays;

import org.jfree.pixie.wmf.records.CommandFactory;
import org.jfree.pixie.wmf.records.MfCmd;
import org.jfree.pixie.wmf.records.MfCmdSetWindowExt;
import org.jfree.pixie.wmf.records.MfCmdSetWindowOrg;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;

/**
 * Parses and replays the WmfFile.
 */
public class WmfFile implements Drawable
{
  public static final int QUALITY_NO = 0;    // Can't convert.
  public static final int QUALITY_MAYBE = 1; // Might be able to convert.
  public static final int QUALITY_YES = 2;   // Can convert.

  // Maximal picture size is 1200x1200. A average wmf file scales easily
  // to 20000 and more, so we have to limit the pixel image's size.

  private static final int MAX_PICTURE_SIZE = getMaxPictureSize();

  private static int getMaxPictureSize ()
  {
    return 1200;
  }

  private WmfObject[] objects;
  private Stack dcStack;
  private MfPalette palette;

  //private String inName;
  private InputStream in;
  private MfHeader header;
  private int fileSize;
  private int filePos;

  private ArrayList records;
  private Graphics2D graphics;

  private int maxWidth = 0;
  private int maxHeight = 0;
  private int imageWidth = 0;
  private int imageHeight = 0;

  private int minX = 0;
  private int minY = 0;
  private int imageX = 0;
  private int imageY = 0;

  public WmfFile (final URL input)
          throws IOException
  {
    this(input, -1, -1);
  }

  public WmfFile (final String input)
          throws IOException
  {
    this(input, -1, -1);
  }

  public WmfFile (final URL input, final int imageX, final int imageY)
          throws IOException
  {
    this(new BufferedInputStream(input.openStream()), imageX, imageY);
  }

  /**
   * Initialize metafile for reading from filename.
   */
  public WmfFile (final String inName, final int imageX, final int imageY)
          throws FileNotFoundException, IOException
  {
    this(new BufferedInputStream(new FileInputStream(inName)), imageX, imageY);
  }

  /**
   * Initialize metafile for reading from filename.
   */
  public WmfFile (final InputStream in, final int imageX,
                  final int imageY)
          throws FileNotFoundException, IOException
  {
    this.in = in;
    this.imageWidth = imageX;
    this.imageHeight = imageY;
    records = new ArrayList();
    dcStack = new Stack();
    palette = new MfPalette();
    readHeader();
    parseRecords();
    resetStates();
  }

  public Dimension getImageSize ()
  {
    return new Dimension(maxWidth, maxHeight);
  }

  public void resetStates ()
  {
    Arrays.fill(objects, null);
    dcStack.clear();
    dcStack.push(new MfDcState(this));
  }

  public MfPalette getPalette ()
  {
    return palette;
  }

  /**
   * Return true if the input is a metafile
   */
  public static int isMetafile (final String inName, final InputStream in)
  {
    return MfHeader.isMetafile(inName, in);
  }

  /**
   * Return Placeable and Windows headers that were read earlier.
   */
  public MfHeader getHeader ()
  {
    return header;
  }

  public Graphics2D getGraphics2D ()
  {
    return graphics;
  }

  /**
   * Check class invariant.
   */
  public void assertValid ()
  {
    if (filePos < 0 || filePos > fileSize)
    {
      throw new IllegalStateException("WmfFile is not valid");
    }
  }

  /**
   * Read Placeable and Windows headers.
   */
  public MfHeader readHeader ()
          throws IOException
  {
    header = new MfHeader();
    header.read(in);
    if (header.isValid())
    {
      fileSize = header.getFileSize();
      objects = new WmfObject[header.getObjectsSize()];
      filePos = header.getHeaderSize();
      return header;
    }
    else
    {
      throw new IOException("The given file is not a real metafile");
    }
  }

  /**
   * Fetch a record.
   */
  public MfRecord readNextRecord ()
          throws IOException
  {
    if (filePos >= fileSize)
    {
      return null;
    }

    assertValid();

    final MfRecord record = new MfRecord(in);
    filePos += record.getLength();
    return record;
  }

  /**
   * Read and interpret the body of the metafile.
   */
  protected void parseRecords ()
          throws IOException
  {
    minX = Integer.MAX_VALUE;
    minY = Integer.MAX_VALUE;
    maxWidth = 0;
    maxHeight = 0;

    final CommandFactory cmdFactory = CommandFactory.getInstance();
    MfRecord mf;
    while ((mf = readNextRecord()) != null)
    {
      final MfCmd cmd = cmdFactory.getCommand(mf.getType());
      if (cmd == null)
      {
        Log.info("Failed to parse record " + mf.getType());
      }
      else
      {
        cmd.setRecord(mf);

        if (cmd.getFunction() == MfType.SET_WINDOW_ORG)
        {
          final MfCmdSetWindowOrg worg = (MfCmdSetWindowOrg) cmd;
          final Point p = worg.getTarget();
          minX = Math.min(p.x, minX);
          minY = Math.min(p.y, minY);
        }
        else if (cmd.getFunction() == MfType.SET_WINDOW_EXT)
        {
          final MfCmdSetWindowExt worg = (MfCmdSetWindowExt) cmd;
          final Dimension d = worg.getDimension();
          maxWidth = Math.max(maxWidth, d.width);
          maxHeight = Math.max(maxHeight, d.height);
        }
        records.add(cmd);
      }
    }
    in.close();
    in = null;

    // make sure that we don't have invalid values in case no
    // setWindow records were found ...
    if (minX == Integer.MAX_VALUE)
    {
      minX = 0;
    }
    if (minY == Integer.MAX_VALUE)
    {
      minY = 0;
    }

    //System.out.println(records.size() + " records read");
    //System.out.println("Image Extends: " + maxWidth + " " + maxHeight);
    scaleToFit(MAX_PICTURE_SIZE, MAX_PICTURE_SIZE);
  }

  /**
   * <!-- Yes, this is from iText lib -->
   */
  public void scaleToFit (final float fitWidth, final float fitHeight)
  {
    final float percentX = (fitWidth * 100) / maxWidth;
    final float percentY = (fitHeight * 100) / maxHeight;
    scalePercent(percentX < percentY ? percentX : percentY);
  }

  /**
   * Scale the image to a certain percentage.
   *
   * @param		percent		the scaling percentage <!-- Yes, this is from iText lib -->
   */
  public void scalePercent (final float percent)
  {
    scalePercent(percent, percent);
  }

  /**
   * Scale the width and height of an image to a certain percentage.
   *
   * @param		percentX	the scaling percentage of the width
   * @param		percentY	the scaling percentage of the height <!-- Yes, this is from iText
   * lib -->
   */
  public void scalePercent (final float percentX, final float percentY)
  {
    imageWidth = (int) ((maxWidth * percentX) / 100f);
    imageHeight = (int) ((maxHeight * percentY) / 100f);
    imageX = (int) ((minX * percentX) / 100f);
    imageY = (int) ((minY * percentY) / 100f);
  }

  public static void main (final String[] args)
          throws Exception
  {
    final WmfFile wmf = new WmfFile("./pixie/res/test.wmf", 800, 600);
    wmf.replay();
  }

  public MfDcState getCurrentState ()
  {
    return (MfDcState) dcStack.peek();
  }

  // pushes a state on the stack
  public void saveDCState ()
  {
    final MfDcState currentState = getCurrentState();
    dcStack.push(new MfDcState(currentState));

  }

  public int getStateCount ()
  {
    return dcStack.size();
  }

  // Pops a state out
  public void restoreDCState (final int state)
  {
    if ((state > 0) == false)
    {
      throw new IllegalArgumentException();
    }

    if (dcStack.size() > 1 + state)
    {
      for (int i = 0; i < state; i++)
      {
        dcStack.pop();
      }

      getCurrentState().restoredState();
    }
    else
    {
      throw new EmptyStackException();
    }
  }

  /**
   * Return the next free slot from the objects table.
   */
  protected int findFreeSlot ()
  {
    for (int slot = 0; slot < objects.length; slot++)
    {
      if (objects[slot] == null)
      {
        return slot;
      }
    }

    throw new IllegalStateException("No free slot");
  }


  public void storeObject (final WmfObject o)
  {
    final int idx = findFreeSlot();
    objects[idx] = o;
  }

  public void deleteObject (final int slot)
  {
    if ((slot < 0) || (slot >= objects.length))
    {
      throw new IllegalArgumentException("Range violation");
    }

    objects[slot] = null;
  }

  public WmfObject getObject (final int slot)
  {
    if ((slot < 0) || (slot >= objects.length))
    {
      throw new IllegalStateException("Range violation");
    }

    return objects[slot];
  }

  public MfLogBrush getBrushObject (final int slot)
  {
    final WmfObject obj = getObject(slot);
    if (obj.getType() == WmfObject.OBJ_BRUSH)
    {
      return (MfLogBrush) obj;
    }
    throw new IllegalStateException("Object " + slot + " was no brush");
  }

  public MfLogPen getPenObject (final int slot)
  {
    final WmfObject obj = getObject(slot);
    if (obj.getType() == WmfObject.OBJ_PEN)
    {
      return (MfLogPen) obj;
    }
    throw new IllegalStateException("Object " + slot + " was no pen");
  }

  public MfLogRegion getRegionObject (final int slot)
  {
    final WmfObject obj = getObject(slot);
    if (obj.getType() == WmfObject.OBJ_REGION)
    {
      return (MfLogRegion) obj;
    }
    throw new IllegalStateException("Object " + slot + " was no region");
  }

  public synchronized BufferedImage replay ()
  {
    return replay(imageWidth, imageHeight);
  }


  public synchronized BufferedImage replay (final int imageX, final int imageY)
  {
    final BufferedImage image = new BufferedImage(imageX, imageY, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D graphics = image.createGraphics();

    // clear the image area ...
    graphics.setPaint(new Color(0, 0, 0, 0));
    graphics.fill(new Rectangle(0, 0, imageX, imageY));

    draw(graphics, new Rectangle2D.Float(0, 0, imageX, imageY));
    graphics.dispose();
    return image;
  }

  public synchronized void draw (final Graphics2D graphics, final Rectangle2D bounds)
  {

    // this adjusts imageWidth and imageHeight
    scaleToFit((float) bounds.getWidth(), (float) bounds.getHeight());
    // adjust translation if needed ...
    graphics.translate(-bounds.getX(), -bounds.getY());
    // adjust to the image origin
    graphics.translate(-imageX, -imageY);

    this.graphics = graphics;

    for (int i = 0; i < records.size(); i++)
    {
      try
      {
        final MfCmd command = (MfCmd) records.get(i);
        command.setScale((float) imageWidth / (float) maxWidth, (float) imageHeight / (float) maxHeight);
        command.replay(this);
      }
      catch (Exception e)
      {
        Log.warn("Error while processing image record #" + i);
        e.printStackTrace();
      }
    }
    resetStates();

  }
}
