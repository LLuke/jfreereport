package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.image.PixieDataInput;
import gnu.bhresearch.pixie.Constants;
import java.util.Vector;
import java.io.IOException;
import java.awt.Graphics;

public class PixieObjectRef extends PixieImageCommand
{
  private int x;
  private int y;
  private int width;
  private int height;
  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;
  private int objectRef;
  private PixieObject storedObject;
  
  public PixieObjectRef (PixieDataInput in, ObjectStore store)
    throws IOException
  {
    objectRef = in.readUnsignedVInt();
    storedObject = store.getObject (objectRef);
    x = in.readVIntX ();
    y = in.readVIntY ();
    width = in.readWidth ();
    height = in.readHeight ();
    scaled_x = x;
    scaled_y = y;
    scaled_width = width;
    scaled_height = height;
  }

  public PixieObjectRef (int object, int x, int y, int width, int height)
  {
    this.objectRef = object;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    scaled_x = x;
    scaled_y = y;
    scaled_width = width;
    scaled_height = height;
  }
  
  protected void scaleXChanged ()
  {
    scaled_x = getScaledX(x);
    scaled_width = getScaledX(width);
  }
  
  protected void scaleYChanged ()
  {
    scaled_y = getScaledY(y);
    scaled_height = getScaledY(height);
  }
  
  public void paint (Graphics graphics)
  {
    graphics.translate( scaled_x, scaled_y );
    storedObject.paint (graphics);
    graphics.translate( -scaled_x, -scaled_y );
  }
  
  public int getWidth ()
  {
    return width;
  }
  
  public int getHeight ()
  {
    return height;
  }
  
}