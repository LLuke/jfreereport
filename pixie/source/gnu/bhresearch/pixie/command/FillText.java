package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.Constants;
import gnu.bhresearch.pixie.image.PixieDataInput;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.IOException;

public class FillText extends PixieImageCommand
{
  private String text;
  private int x;
  private int y;
  private int flags;
  private int scaled_x;
  private int scaled_y;

  public FillText (String text, int x, int y, int flags)
  {
    this.text = text;
    this.x = x;
    this.y = y;
    this.flags = flags;
    scaleXChanged ();
    scaleYChanged ();
  }

  public FillText (PixieDataInput in)
          throws IOException
  {
    in.flushVInt ();
    text = in.readUTF ();
    x = in.readVIntX ();
    y = in.readVIntY ();
    flags = in.readUnsignedVInt ();
    scaleXChanged ();
    scaleYChanged ();
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
  }

  public void paint (Graphics graphics)
  {
    int x = this.scaled_x;
    int y = this.scaled_y;

    FontMetrics metrics = graphics.getFontMetrics ();
    int textWidth = metrics.stringWidth (text);
    if ((flags & Constants.TEXT_CENTER) != 0)
    {
      x -= textWidth / 2;
    }
    else if ((flags & Constants.TEXT_RIGHT) != 0)
    {
      x -= textWidth;
    }

    graphics.drawString (text, x, y);

    if ((flags & Constants.TEXT_UNDERLINE) != 0)
    {	// Underline.
      y += metrics.getDescent () / 8 + 1;
      graphics.drawLine (x, y, x + textWidth, y);
    }
  }

  public String getText ()
  {
    return text;
  }

  public int getWidth ()
  {
    return x;
  }

  public int getHeight ()
  {
    return y;
  }

}

