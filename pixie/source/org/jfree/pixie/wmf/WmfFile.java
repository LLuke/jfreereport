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
 * WmfFile.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: MfLogBrush.java,v 1.1 2003/02/25 20:58:07 taqua Exp $
 *
 * Changes
 * -------
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

import org.jfree.pixie.wmf.records.MfCmd;
import org.jfree.pixie.wmf.records.MfCmdSetWindowExt;
import org.jfree.pixie.wmf.records.MfCmdSetWindowOrg;
import org.jfree.pixie.wmf.records.CommandFactory;
import com.jrefinery.ui.Drawable;

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

  private static int getMaxPictureSize()
  {
    return 1200;
  }

  private WmfObject[] objects;
  private Stack dcStack;
  private MfPalette palette;

  private String inName;
  private InputStream in;
  private MfHeader header;
  private int fileSize;
  private int filePos;

  private ArrayList records;
  private Graphics2D graphics;

  private int maxX = 0;
  private int maxY = 0;
  private int imageX = 0;
  private int imageY = 0;

  public WmfFile(URL input)
      throws IOException
  {
    this(input, -1, -1);
  }

  public WmfFile(String input)
      throws IOException
  {
    this(input, -1, -1);
  }

  public WmfFile(URL input, int imageX, int imageY)
      throws IOException
  {
    this(new BufferedInputStream(input.openStream()), input.toString(), imageX, imageY);
  }

  /**
   * Initialize metafile for reading from filename.
   */
  public WmfFile(String inName, int imageX, int imageY)
      throws FileNotFoundException, IOException
  {
    this(new BufferedInputStream(new FileInputStream(inName)), inName, imageX, imageY);
  }

  /**
   * Initialize metafile for reading from filename.
   */
  public WmfFile(InputStream in, String inName, int imageX, int imageY)
      throws FileNotFoundException, IOException
  {
    this.inName = inName;
    this.in = in;
    this.imageX = imageX;
    this.imageY = imageY;
    records = new ArrayList();
    dcStack = new Stack();
    palette = new MfPalette();
    resetStates();
    readHeader();
    parseRecords();
  }

  public void resetStates()
  {
    dcStack.clear();
    dcStack.push(new MfDcState(this));
  }

  public MfPalette getPalette()
  {
    return palette;
  }

  /**
   * Return true if the input is a metafile
   */
  public static int isMetafile(String inName, InputStream in)
  {
    return MfHeader.isMetafile(inName, in);
  }

  /**
   * Return Placeable and Windows headers that were read earlier.
   */
  public MfHeader getHeader()
  {
    return header;
  }

  public Graphics2D getGraphics2D()
  {
    return graphics;
  }

  /**
   * Check class invariant.
   */
  public void assertValid()
  {
    if ((filePos >= 0 && filePos <= fileSize) == false)
    {
      throw new IllegalStateException("WmfFile is not valid");
    }
  }

  /**
   * Read Placeable and Windows headers.
   */
  public MfHeader readHeader() throws IOException
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
      throw new IOException(inName + "is not a real metafile");
    }
  }

  public int maxRec = 10000;

  /**
   * Fetch a record.
   */
  public MfRecord readNextRecord() throws IOException
  {
    if (filePos >= fileSize)
      return null;

    assertValid();

    MfRecord record = new MfRecord(in);
    filePos += record.getLength();
    return record;
  }

  /**
   * Read and interpret the body of the metafile.
   */
  protected void parseRecords() throws IOException
  {
    int curX = 0;
    int curY = 0;

    CommandFactory cmdFactory = CommandFactory.getInstance();
    MfRecord mf = null;
    while ((mf = readNextRecord()) != null)
    {
      MfCmd cmd = cmdFactory.getCommand(mf.getType());
      if (cmd == null)
      {
        System.out.println("Failed to parse record " + mf.getType());
      }
      else
      {
        cmd.setRecord(mf);

        if (cmd.getFunction() == MfType.SET_WINDOW_ORG)
        {
          MfCmdSetWindowOrg worg = (MfCmdSetWindowOrg) cmd;
          Point p = worg.getTarget();
          curX = p.x;
          curY = p.y;
        }
        else if (cmd.getFunction() == MfType.SET_WINDOW_EXT)
        {
          MfCmdSetWindowExt worg = (MfCmdSetWindowExt) cmd;
          Dimension d = worg.getDimension();
          maxX = Math.max(maxX, curX + d.width);
          maxY = Math.max(maxY, curY + d.height);
        }
        records.add(cmd);
      }
    }
    in.close();
    in = null;

    //System.out.println(records.size() + " records read");
    //System.out.println("Image Extends: " + maxX + " " + maxY);
    scaleToFit(MAX_PICTURE_SIZE, MAX_PICTURE_SIZE);
  }

  /**
   * <!-- Yes, this is from iText lib -->
   */
  public void scaleToFit(float fitWidth, float fitHeight)
  {
    float percentX = (fitWidth * 100) / maxX;
    float percentY = (fitHeight * 100) / maxY;
    scalePercent(percentX < percentY ? percentX : percentY);
  }

  /**
   * Scale the image to a certain percentage.
   *
   * @param		percent		the scaling percentage
   * <!-- Yes, this is from iText lib -->
   */
  public void scalePercent(float percent)
  {
    scalePercent(percent, percent);
  }

  /**
   * Scale the width and height of an image to a certain percentage.
   *
   * @param		percentX	the scaling percentage of the width
   * @param		percentY	the scaling percentage of the height
   * <!-- Yes, this is from iText lib -->
   */
  public void scalePercent(float percentX, float percentY)
  {
    imageX = (int) ((maxX * percentX) / 100f);
    imageY = (int) ((maxY * percentY) / 100f);
  }

  public static void main(String[] args) throws Exception
  {
    WmfFile wmf = new WmfFile("./pixie/res/test.wmf", 800, 600);
    wmf.replay();
  }

  public MfDcState getCurrentState()
  {
    return (MfDcState) dcStack.peek();
  }

  // pushes a state on the stack
  public void saveDCState()
  {
    MfDcState currentState = getCurrentState();
    dcStack.push(new MfDcState(currentState));

  }

  public int getStateCount()
  {
    return dcStack.size();
  }

  // Pops a state out
  public void restoreDCState(int state)
  {
    if ((state > 0) == false)
      throw new IllegalArgumentException();

    if (dcStack.size() > 1 + state)
    {
      for (int i = 0; i < state; i++)
        dcStack.pop();

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
  protected int findFreeSlot()
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


  public void storeObject(WmfObject o)
  {
    int idx = findFreeSlot();
    objects[idx] = o;
  }

  public void deleteObject(int slot)
  {
    if (((slot >= 0) && (slot < objects.length)) == false)
      throw new IllegalArgumentException("Range violation");

    objects[slot] = null;
  }

  public WmfObject getObject(int slot)
  {
    if (((slot >= 0) && (slot < objects.length)) == false)
      throw new IllegalStateException("Range violation");

    return objects[slot];
  }

  public MfLogBrush getBrushObject(int slot)
  {
    WmfObject obj = getObject(slot);
    if (obj.getType() == WmfObject.OBJ_BRUSH)
      return (MfLogBrush) obj;
    throw new IllegalStateException("Object " + slot + " was no brush");
  }

  public MfLogPen getPenObject(int slot)
  {
    WmfObject obj = getObject(slot);
    if (obj.getType() == WmfObject.OBJ_PEN)
      return (MfLogPen) obj;
    throw new IllegalStateException("Object " + slot + " was no pen");
  }

  public MfLogRegion getRegionObject(int slot)
  {
    WmfObject obj = getObject(slot);
    if (obj.getType() == WmfObject.OBJ_REGION)
      return (MfLogRegion) obj;
    throw new IllegalStateException("Object " + slot + " was no region");
  }

  public synchronized BufferedImage replay()
  {
    BufferedImage image = new BufferedImage(imageX, imageY, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = image.createGraphics();

    // clear the image area ...
    graphics.setPaint(new Color (0,0,0,0));
    graphics.fill(new Rectangle(0, 0, imageX, imageY));

    draw(graphics, new Rectangle2D.Float (0,0, imageX, imageY));
    graphics.dispose();
    graphics = null;
    return image;
  }

  public void draw(Graphics2D graphics, Rectangle2D bounds)
  {

    // this adjusts imageX and imageY
    scaleToFit((float) bounds.getWidth(), (float) bounds.getHeight());
    // adjust translation if needed ...
    graphics.translate(-bounds.getX(), -bounds.getY());

    this.graphics = graphics;

    for (int i = 0; i < records.size(); i++)
    {
      if (i > maxRec) break;
      try
      {
        MfCmd command = (MfCmd) records.get(i);
        command.setScale((float) imageX / (float) maxX, (float) imageY / (float) maxY);
        command.replay(this);
      }
      catch (Exception e)
      {
        System.out.println("Error on records i = " + i);
        e.printStackTrace();
      }
    }
    resetStates();

  }
}
