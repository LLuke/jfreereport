package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.Constants;
import gnu.bhresearch.pixie.image.PixieDataInput;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.io.IOException;
import java.util.Vector;

/**
 * A PixieImageCommand contains a command on how to draw a pixie image.
 * All Commands are collected in a PixieFrame and replayed with paint. 
 * This implementation does not support Objects or Scrips as PixieSlide
 * does. 
 *
 * You may load a pixie image using an PixieDataInput on the static
 * method PixieImageCommand.load () or by using a PixieImageFrame.
 */
public abstract class PixieImageCommand
{
  public PixieImageCommand ()
  {
//    System.out.println (this.getClass().getName());
    scaleX = 1;
    scaleY = 1;
  }

  private float scaleX;
  private float scaleY;

  public void setScale (float scaleX, float scaleY)
  {
    float oldScaleX = this.scaleX;
    float oldScaleY = this.scaleY;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    if (oldScaleX != scaleX)
    {
      scaleXChanged ();
    }
    if (oldScaleY != scaleY)
    {
      scaleYChanged ();
    }
  }
  
  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }
  
  public int getScaledWidth (int length) 
  {
    if (length == 0)
    {
      return 1;
    }
    length = (int)(length * scaleX + 0.5f);
    return (length == 0) ? 1 : length;
  }
  
  public int getScaledHeight (int length) 
  {
    if (length == 0)
    {
      return 1;
    }
    length = (int)(length * scaleY + 0.5f);
    return (length == 0) ? 1 : length;
  }

  public int[] applyScaleX (int[] n, int [] dest)
  {
    if (dest == null)
      dest = new int[n.length];
    else  
    if (dest.length < n.length)
      dest = new int[n.length];
        
    for (int i = 0; i < n.length; i++)
    {
      dest[i] = (int)(n[i] * scaleX + 0.5f);
    }
    return dest;
  }

  public int[] applyScaleY (int[] n, int [] dest)
  {
    if (dest == null)
      dest = new int[n.length];
    else  
    if (dest.length < n.length)
      dest = new int[n.length];
        
    for (int i = 0; i < n.length; i++)
    {
      dest[i] = (int)(n[i] * scaleY + 0.5f);
    }
    return dest;
  }

  /** 
   * Return integer scaled to output units. 
   */
  public int getScaledY(int y)
  {
    return (int)(y * scaleY + 0.5f);
  }

  /** 
   * Return integer scaled to output units. 
   */
  public int getScaledX (int x)
  {
    return (int)(x * scaleX + 0.5f);
  }


  public abstract void paint (Graphics g);
  public abstract int getWidth ();
  public abstract int getHeight ();
 
}
