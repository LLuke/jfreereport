package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.Constants;
import gnu.bhresearch.pixie.image.PixieDataInput;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.io.IOException;
import java.util.Vector;


public class Hotspot extends PixieImageCommand
{
  private String args;
  private int x;
  private int y;
  private int width;
  private int height;
  private int cmd;
  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;
  
  public Hotspot (PixieDataInput in)
    throws IOException
  {
    in.flushVInt();
    args = in.readUTF();
    x = in.readVIntX ();
    y = in.readVIntY ();
    width = in.readWidth ();
    height = in.readHeight ();
    cmd = in.readVInt();
    scaleXChanged ();
    scaleYChanged ();
  }
  
  public Hotspot (int cmd, String args, int x, int y, int width, int height)
  {
    this.cmd = cmd;
    this.args = args;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    scaleXChanged ();
    scaleYChanged ();
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

  public void paint (Graphics g)
  {
    // Hotspots do not paint at all
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

