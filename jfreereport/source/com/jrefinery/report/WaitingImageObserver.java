/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ----------------
 * WaitingImageObserver.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morger
 * Contributor(s):   Stefan Prange;
 *
 * $Id: WaitingImageObserver.java,v 1.7 2002/07/14 21:59:33 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 15-Apr-2002 : first version used by ImageElement.
 * 16-May-2002 : Line delimiters adjusted
 * 04-Jun-2002 : Documentation and added a NullPointerCheck for the constructor.
 * 14-Jul-2002 : BugFixed: WaitingImageObserver dead-locked (bugfix by Stefan Prange)
 */
package com.jrefinery.report;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Serializable;

/**
 * This image observer blocks until the image is completly loaded. AWT
 * deferres the loading of images until they are painted on a graphic.
 *
 * While printing reports it is not very nice not to know whether a image
 * was completely loaded, so this observer forces the loading of the image
 * until a final state (either ALLBITS, ABORT or ERROR) is reached.
 */
public class WaitingImageObserver implements ImageObserver, Serializable, Cloneable
{
  private boolean lock;
  private Image image;

  /**
   * Creates a new ImageObserver for the given Image. The Oberver has to be started
   * by an external thread.
   *
   * @throws NullPointerException if the given image is null.
   */
  public WaitingImageObserver (Image image)
  {
    if (image == null) throw new NullPointerException ();
    this.image = image;
    lock = true;
  }

  /**
   * Callback function used by AWT to inform that more data is availiable. The observer
   * waits until either all data is loaded or AWT signals that the image cannot be loaded.
   */
  public boolean imageUpdate (
          Image img,
          int infoflags,
          int x,
          int y,
          int width,
          int height)
  {
    if ((infoflags & ImageObserver.ALLBITS) == ImageObserver.ALLBITS
            || (infoflags & ImageObserver.ABORT) == ImageObserver.ABORT
            || (infoflags & ImageObserver.ERROR) == ImageObserver.ERROR)
    {
      lock = false;
    }
    return true;
  }

  /**
   * The workerthread. Simply draws the image to an BufferedImage's Graphics-Object
   * and waits for the AWT to load the image.
   */
  public void waitImageLoaded ()
  {
    BufferedImage img = new BufferedImage (1, 1, BufferedImage.TYPE_INT_RGB);
    Graphics g = img.getGraphics ();

    while (lock)
    {
      if (g.drawImage (image, 0, 0, img.getWidth (this), img.getHeight (this), this))
        return;
      try
      {
        Thread.currentThread ().sleep (200);
      }
      catch (InterruptedException e)
      {
      }
    }
  }

  /**
   * Clones this WaitingImageObserver.
   */
  public Object clone () throws CloneNotSupportedException
  {
    WaitingImageObserver obs = (WaitingImageObserver) super.clone ();
    return obs;
  }
}
