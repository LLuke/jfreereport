package gnu.bhresearch.pixie.wmf;

import gnu.bhresearch.pixie.wmf.records.MfCmd;
import gnu.bhresearch.pixie.wmf.records.MfCmdSetWindowExt;
import gnu.bhresearch.pixie.wmf.records.MfCmdSetWindowOrg;
import gnu.bhresearch.quant.Assert;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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

/**
 * Everyting the parser did before.
 */
public class WmfFile
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
  private BufferedImage image;
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

  public BufferedImage getImage()
  {
    return image;
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
    Assert.assert(filePos >= 0 && filePos <= fileSize, "FilePos=" + filePos + "; FileSize=" + fileSize);
  }

  /**
   * Read Placeable and Windows headers.
   */
  public MfHeader readHeader() throws IOException
  {
    header = new MfHeader();
    header.read(in, inName);
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

    MfRecord record = new MfRecord();
    record.read(in, inName);
    filePos += record.getLength();
    return record;
  }

  /**
   * Read and interpret the body of the metafile.
   */
  protected void parseRecords() throws IOException
  {
    MfCmd.registerAllKnownTypes();
    int curX = 0;
    int curY = 0;

    MfRecord mf = null;
    while ((mf = readNextRecord()) != null)
    {
      MfCmd cmd = MfCmd.getCommand(mf.getType());
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

  public synchronized BufferedImage replay()
  {

    image = new BufferedImage(imageX, imageY, BufferedImage.TYPE_INT_ARGB);
    graphics = image.createGraphics();
    graphics.fill(new Rectangle(0, 0, imageX, imageY));

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
        System.out.println("Error on command i = " + i);
        e.printStackTrace();
      }
    }
    BufferedImage retval = image;

    image = null;
    graphics.dispose();
    graphics = null;
    resetStates();

    return retval;
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
    Assert.assert(state > 0);

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

    Assert.failed();
    return -1; // Shouldn't happen for valid files.
  }


  public void storeObject(WmfObject o)
  {
    int idx = findFreeSlot();
    objects[idx] = o;
  }

  public void deleteObject(int slot)
  {
    Assert.assert((slot >= 0) && (slot < objects.length), "Range violation");
    objects[slot] = null;
  }

  public WmfObject getObject(int slot)
  {
    Assert.assert((slot >= 0) && (slot < objects.length), "Range violation");
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
}
