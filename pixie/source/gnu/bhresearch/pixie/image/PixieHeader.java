package gnu.bhresearch.pixie.image;

import java.io.IOException;
import gnu.bhresearch.pixie.Constants;
import gnu.bhresearch.pixie.command.ObjectStore;
import java.awt.Rectangle;

public class PixieHeader
{
  /** Magic number at start of file. */
  public static final int MAGIC = 0x5daa749;
  
  /**
  * Width of the pixie image contained in this producer
  */
  private int pixieWidth;

  /**
  * Height of the pixie image contained in this producer
  */
  private int pixieHeight;

  /** Highest file version that we understand. */
  private final static short FILE_VERSION = 300;
  private int frame;
  private long headerLength;
  private short version;
  private int logWidth;
  private int logHeight;
  
  private int[] frameTable;
  private int[] framePause;
  private int[] objectTable;
  private int bodyLength;
  
  public PixieHeader ()
  {
  }
  
  public PixieHeader (PixieDataInput in) throws IOException
  {
    readHeader (in);
  }
  
  public void readHeader (PixieDataInput in)
    throws IOException
  {
    // Verify file format via magic numbers and version number.
    if (in.readInt() != MAGIC)
    {
      throw new IOException( "Bad Pixie header: " + in.getFilePointer());
    }
  
    version = in.readShort();
    if (version != FILE_VERSION)
    {
      throw new IOException( "Bad Pixie version " + version );
    }

    pixieWidth = in.readUnsignedVInt();
    pixieHeight = in.readUnsignedVInt();

    // Skip unused fields.
    in.readUnsignedVInt(); // reserved
    in.readUnsignedVInt(); // reserved
    // Frame table.
    int frameCount = in.readUnsignedVInt();
    frameTable = new int[ frameCount ];
    framePause = new int[ frameCount ];
    
    int prev = 0;
    int prevPause = -1;
    
    for (int i = 0; i < frameCount; i++)
    {
      prev += in.readUnsignedVInt();
      frameTable[i] = prev;
      framePause[i] = prevPause;

      // Interpret frame control commands.
      int cmd = in.readUnsignedVInt();
      if (cmd == Constants.CTL_PAUSE)
      {
        framePause[i] = in.readVInt();
        prevPause = framePause[i];
        cmd = in.readUnsignedVInt();
      }
      if (cmd != Constants.CTL_END)
      {
        throw new IOException( "Unknown header command" );
      }
    }

    // Object table
    int objectCount = in.readUnsignedVInt();
    objectTable = new int[ objectCount ];
    prev = 0;
    for (int i = 0; i < objectCount; i++)
    {
      prev += in.readUnsignedVInt();
      objectTable[i] = prev;
    }

    // Skip unused fields.
    in.readUnsignedVInt();
    in.readUnsignedVInt();

    // allow Block-read of the rest.
    bodyLength = in.readUnsignedVInt();
    in.flushVInt(); // End-of-header-mark is ignored.
    headerLength = in.getFilePointer();
  }

  public int getBodyLength ()
  {
    return bodyLength;
  }

  public int getWidth ()
  {
    return pixieWidth;
  }
  
  public int getHeight ()
  {
    return pixieHeight;
  }
  
  // currently only 1 frame
  public long getFrameOffset (int framenumber)
  {
    return frameTable[framenumber] + headerLength;
  }
  
  public int getFramePause (int frame)
  {
    return framePause [frame];
  }
  
  public int getFrameCount ()
  {
    return frameTable.length;
  }
  
  public int getVersion ()
  {
    return version;
  }
  
  public void setVersion (short version)
  {
    this.version = version;
  }
  
  public void setLogicalSize (Rectangle rect)
  {
    this.pixieWidth = rect.width;
    this.pixieHeight = rect.height;
  }
  
  public ObjectStore getObjectStore (PixieDataInput in) throws IOException
  {
    return new ObjectStore (objectTable, in);
  }
}
