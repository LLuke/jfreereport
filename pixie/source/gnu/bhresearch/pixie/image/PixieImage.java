package gnu.bhresearch.pixie.image;

import gnu.bhresearch.pixie.command.ObjectStore;
import gnu.bhresearch.pixie.command.PixieFrame;
import java.util.Vector;
import java.io.IOException;
import java.awt.Dimension;

public class PixieImage
{
  private ObjectStore objectStore;
  private PixieHeader header;
  private Vector frames;
  private Vector framePauses;
  private int maxX;
  private int maxY;
    
  public PixieImage ()
  {
    objectStore = new ObjectStore();
    header = new PixieHeader ();
    frames = new Vector ();
    framePauses = new Vector ();
  }
  
  public void setObjectStore (ObjectStore store)
  {
    this.objectStore = store;
  }
  
  public ObjectStore getObjectStore ()
  {
    return objectStore;
  }
  
  public void setHeader (PixieHeader header)
  {
    this.header = header;
  }
  
  public PixieHeader getHeader ()
  {
    return header;
  }
  
  public void addFrame (PixieFrame frame, int pause)
  {
    if (frame == null)
      throw new NullPointerException ("Given Frame was null");
    frames.add (frame);
    framePauses.add (new Integer (pause));
    setMaxX (frame.getWidth());
    setMaxY (frame.getHeight());
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
  
  public PixieFrame getFrame (int frame)
  {
    return (PixieFrame) frames.elementAt (frame);
  }
  
  public int getFrameCount ()
  {
    return frames.size();
  }
  
  public int getFramePause (int frame)
  {
    return ((Integer) framePauses.elementAt (frame)).intValue ();
  }
  
  public PixieImage (PixieDataInput in) throws IOException
  {
    frames = new Vector ();
    framePauses = new Vector ();
    header = new PixieHeader (in);
    objectStore = header.getObjectStore(in);
    
    int length = header.getFrameCount ();
    for (int i = 0; i < length; i++)
    {
      long foffset = header.getFrameOffset(i);
      int fpause  = header.getFramePause(i);
      
      in.seek (foffset);
      PixieFrame frame = new PixieFrame(in, objectStore);
      addFrame (frame, fpause);
    }
  }
  
  public Dimension getMaximumSize ()
  {
    return new Dimension (maxX, maxY);
  }

  public void setMaximumSize (Dimension dim)
  {
    maxX = dim.width;
    maxY = dim.height;
  }
  
}
