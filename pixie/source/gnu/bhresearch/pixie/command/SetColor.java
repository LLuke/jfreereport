package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.image.PixieDataInput;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

public class SetColor extends PixieImageCommand
{
  private Color color;

  public SetColor (Color color)
  {
    this.color = color;
  }

  public SetColor (PixieDataInput in)
          throws IOException
  {
    int value = in.readUnsignedVInt ();
    color = new Color (value);
  }

  public Color getColor ()
  {
    return color;
  }

  public boolean equals (Object o)
  {
    if (o instanceof SetColor)
    {
      SetColor c = (SetColor) o;
      return c.color.equals (color);
    }
    return false;
  }

  public void paint (Graphics graphics)
  {
    graphics.setColor (color);
  }

  public int getWidth ()
  {
    return 0;
  }

  public int getHeight ()
  {
    return 0;
  }

}

