package com.jrefinery.report;

import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import gnu.bhresearch.pixie.wmf.WmfImageProducer;

public class ImageReference 
{
  private Image image;
  private URL url;
  private float width;
  private float height;
  private float x;
  private float y;
  
  
  public ImageReference (URL url, float width, float height)
    throws IOException
  {
    this (url, 0,0, width, height);
  }
  
  public ImageReference (URL url, float x, float y, float width, float height)
    throws IOException
  {
    if (width < 1 || height < 1)
      throw new IllegalArgumentException ("Width and height have to be > 0");
    InputStream is = null;
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
    
    try 
    {
      is = url.openStream();
      int c1 = is.read();
      int c2 = is.read();
      is.close();
    
      is = null;
      if (c1 == 0xD7 && c2 == 0xCD) 
      {
        image = Toolkit.getDefaultToolkit().createImage(new WmfImageProducer (url));
      }
      else
      {
        this.url = url;
        image = Toolkit.getDefaultToolkit().getImage(url);
        WaitingImageObserver obs = new WaitingImageObserver (image);
        obs.run ();
      }
    }
    finally 
    {
      if (is != null) 
      {
          is.close();
      }
    }
  }

  public Image getImage ()
  {
    return image;
  }
  
  public URL getSourceURL ()
  {
    return url;
  }
  
  public float getWidth ()
  {
    return width;
  }
  
  public float getHeight ()
  {
    return height;
  }
  
  public float getX ()
  {
    return x;
  }
  
  public float getY ()
  {
    return y;
  }
  
  public Rectangle getBounds ()
  {
    return new Rectangle ((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
  }
  
  public String toString ()
  {
    StringBuffer buf = new StringBuffer ();
    buf.append ("ImageReference={ URL=");
    buf.append (getSourceURL());
    buf.append (" bounds=");
    buf.append (getBounds ());
    buf.append ("}");
    return buf.toString ();
  }
}
