/*
 * Copyright (c) 1998, 1999 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */
package gnu.bhresearch;

import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.RepaintManager;

/**
 * the JImagePanel is a panel, which background is filled by an java.awt.Image.
 * The size of the panel can be changed, the image's aspect ratio can set to be
 * preserved.
 * <p>
 * The background picture will be scaled over the whole size of the panel.
 *
 */ 
public class JImagePanel extends JPanel
{
	private ImageIcon img = null;
	private float imgQuot = -1;
  private boolean scaleRatio = false;
  private boolean scale = true;
  private int width = 0;
  private int height = 0;
	private int maxX = 0;
  private int maxY = 0;
  
  /**
   * Creates an empty JImagePanel
   */
	public JImagePanel ()
	{
		super ();
		img = new ImageIcon ();
    img.setImageObserver (this);
	}
	
  /**
   * Creates an JImagePanel and set the background to the image </code>image</code>
   */
	public JImagePanel (Image image)
	{
		super ();
		img = new ImageIcon ();
    img.setImageObserver (this);
    img.setImage (image);
    checkSize();
    super.imageUpdate (image, WIDTH + HEIGHT, 0, 0, image.getWidth(this), image.getHeight(this));
	}
	
  public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
  {
  	if (img == this.img.getImage ())
    {
	  	if (((infoflags & WIDTH) == WIDTH) || ((infoflags & HEIGHT) == HEIGHT))
      	checkSize ();
    }
    return super.imageUpdate (img, infoflags, x, y, width, height);
  }
           
  /**
   * sets the background of the JImagePanel
   *
   * @param image the new background image
   *
   */
	public void setImage (Image image)
	{
    if (image == null)
    {
      img.setImage (new BufferedImage (0,0,BufferedImage.TYPE_INT_RGB));
    }
    else
    {
	    img.setImage (image);
    }
	}
	
  /**
   * loads an image from the given URL <code>url</code> and sets the received
   * picture as background.
   *
   * @param url the URL of the new image. If the image is invalid, no exception will
   * be raised and no image will be set.
   * @throws NullPointerException if url is null
   */
	public void loadImage (URL url)
	{
    if (url == null)
      throw new NullPointerException ("URL is null");
      
		Toolkit toolkit = Toolkit.getDefaultToolkit ();
		Image image = toolkit.getImage (url);
    
    width = image.getWidth (this);
    height = image.getHeight (this);
    if ((width == -1) || (height == -1))
    {
      imgQuot = -1;
    }
    else
    {
      imgQuot = (float) width / (float) height;
    }
		
		setImage (image);
	}
	
  /**
   * loads a image from the given filesystem path and sets it as background.
   *
   * @param filename the filename of the image. If the image is invalid, no exception
   * will be raised and no picture shown.
   * @throws NullPointerException if url is null
   */
	public void loadImage (String filename)
	{
    URL imgUrl = this.getClass ().getClassLoader ().getResource (filename);
    if (imgUrl == null)
    {
      System.out.println ("Loading " + filename + " - File not found in classpath");
      File file = new File (filename);
      try
      {
        String canfname = file.getCanonicalPath ();
        imgUrl = new URL ("file:/" + canfname);
      }
      catch (IOException ioe)
      {
        System.out.println ("Failed to locate ImageFile" + ioe);
        return;
      }
    }
    loadImage (imgUrl);
	}
	
  /**
   * returns the current background image
   * 
   * @returns the current background as image.
   */
	public Image getImage ()
	{
		return img.getImage();
	}
	
  /**
   * draws the component. If the image is invalid, nothing will be drawn.
   * The image will be scaled before painting and the scaled image will be
   * buffered for faster redrawing.
   */
	public void paintComponent (Graphics g)
	{
    if (img == null) 
    {
      super.paintComponent (g);
    }
    else
    if (img.getImage () == null)
    {
      super.paintComponent (g);
    }
    else
    {
    	if (isOpaque ())
      {
	      super.paintComponent (g);
      }
      
      if (! scale)
      {
      	Graphics2D g2d = (Graphics2D) g;
   	    AffineTransform at = new AffineTransform();
        
        g2d.drawImage(img.getImage (), at, this);
      }
      else
      {
				checkSize ();

				int myWidth = getWidth();
				int myHeight = getHeight ();

  		  g.drawImage (img.getImage (), 0,0, myWidth, myHeight, this);
      }
    }
	}

  /**
   * defines, wheter the aspect ratio of the image should be preserved or not.
   * 
   * @param scaleRatio must be <code>true</code> if the aspect ratio should be
   * preserved. If scaleRatio is <code>false</code> the image may look weired.
   */
  public void setScaleRatio (boolean scaleRatio)
  {
    this.scaleRatio = scaleRatio;
    checkSize ();
  }
  
  /**
   * checks, whether the aspect ratio should be preserved.
   */
  public boolean getScaleRatio ()
  {
    return scaleRatio;
  }
  
  /**
   * defines, whether this component is opaque.
   */
  public void setOpaque (boolean opaque)
  {
    super.setOpaque (opaque);
  }
  
  /**
   * Checks, whether the original size of the picture should be preserved. No
   * scaling will be performed, which results in faster drawing, but the image
   * may not fit into the panel.
   *
   * @param scale must be true, if scaling of the picture is allowed. The default is
   * true.
   */
  public void setScale (boolean scale)
  {
    this.scale = scale;
    checkSize ();
  }

	private void checkSize ()
	{
  	if (img.getImage () == null)
    	return;
      
	  width = maxX > 0 ? Math.min (img.getImage().getWidth (this), maxX) : img.getImage().getWidth (this);
	  height = maxY > 0 ? Math.min (img.getImage().getHeight(this), maxY) : img.getImage().getHeight (this);
    if (width != 0 && height != 0)
    {
	    if (scaleRatio)
	    {
	      imgQuot = (float) width / (float) height;
	      if (imgQuot > 0)
	      {
	        width  = (int) ((float) height * imgQuot);
	      }
	      else
	      {
	        height = (int) ((float) width * (1 / imgQuot));
	      }
	    }
    }
	}
  
  /**
   * Checks whether the image may be scaled or not.
   */
  public boolean getScale ()
  {
    return scale;
  }
  
  public Dimension getSize ()
  {
//  	if (scale)
//  		return super.getSize ();
		return new Dimension (width, height);  
  }
  
  public Dimension getMinimumSize ()
  {
//  	if (scale)
//    	return super.getMinimumSize ();
    return new Dimension (width, height);  
  }
  
  public Dimension getPreferredSize ()
  {
//  	if (scale)
//    	return super.getPreferredSize ();
//      
	  return new Dimension (width, height);  
  }
  
  public Dimension getMaximumSize ()
  {
//   	if (scale)
//    	return super.getMaximumSize ();
	  return new Dimension (width, height);  
  }
  
  public int getWidth ()
  {
  	return width;  
  }
  
  public int getHeight ()
  {
  	return height;
  }
 
  public void setMaxX (int x)
  {
  	maxX = x;
  }
  
  public void setMaxY (int y)
  {
	  maxY = y;
  }
  
  public void setMaximumSize (Dimension d)
  {
	  setMaxX (d.width);
	  setMaxY (d.height);
  }
  
  public void setPreferredSize (Dimension d)
  {
	  setMaxX (d.width);
	  setMaxY (d.height);
  }
}