package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.Constants;
import gnu.bhresearch.pixie.image.PixieDataInput;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.io.IOException;
import java.util.Vector;
import java.awt.geom.AffineTransform;

public class SetFont extends PixieImageCommand
{
  private Font f;
  private Font scaled_font;
  
  public SetFont (Font f)
  {
    this.f = f;
    scaled_font = f;
  }
  
  
  public SetFont (PixieDataInput in)
  throws IOException
  {
    in.flushVInt();
    f = new Font(in.readUTF(), in.readUnsignedVInt(), in.readHeight());
  }

  public void paint (Graphics graphics)
  {
    graphics.setFont (scaled_font);
  }
  
  public void setScale (float scaleX, float scaleY)
  {
    super.setScale (scaleX, scaleY);
    scaled_font = f.deriveFont (AffineTransform.getScaleInstance (scaleX, scaleY));
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

