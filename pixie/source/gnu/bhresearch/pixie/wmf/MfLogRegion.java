package gnu.bhresearch.pixie.wmf;

import java.awt.Rectangle;

public class MfLogRegion implements WmfObject
{
  private int x;
  private int y;
  private int w;
  private int h;

  public int getType ()
  {
    return OBJ_REGION;
  }

  public MfLogRegion ()
  {
  }

  public void setBounds (int x, int y, int w, int h)
  {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  public Rectangle getBounds ()
  {
    return new Rectangle (x, y, w, h);
  }
}
