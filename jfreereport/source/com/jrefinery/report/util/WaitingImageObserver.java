/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * -------------------------
 * WaitingImageObserver.java
 * -------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   Stefan Prange;
 *
 * $Id: WaitingImageObserver.java,v 1.4 2003/02/25 15:42:51 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 15-Apr-2002 : first version used by ImageElement.
 * 16-May-2002 : Line delimiters adjusted
 * 04-Jun-2002 : Documentation and added a NullPointerCheck for the constructor.
 * 14-Jul-2002 : BugFixed: WaitingImageObserver dead-locked (bugfix by Stefan Prange)
 * 18-Mar-2003 : Updated header and made minor Javadoc changes (DG);
 * 
 */

package com.jrefinery.report.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Serializable;

/**
 * This image observer blocks until the image is completely loaded. AWT
 * defers the loading of images until they are painted on a graphic.
 *
 * While printing reports it is not very nice, not to know whether a image
 * was completely loaded, so this observer forces the loading of the image
 * until a final state (either ALLBITS, ABORT or ERROR) is reached.
 *
 * @author Thomas Morgner
 */
public class WaitingImageObserver implements ImageObserver, Serializable, Cloneable
{
  /** The lock. */
  private boolean lock;

  /** The image. */
  private Image image;

  /**
   * Creates a new ImageObserver for the given Image. The Oberver has to be started
   * by an external thread.
   *
   * @param image  the image to observe.
   *
   * @throws NullPointerException if the given image is null.
   */
  public WaitingImageObserver (Image image)
  {
    if (image == null)
    {
      throw new NullPointerException ();
    }
    this.image = image;
    lock = true;
  }

  /**
   * Callback function used by AWT to inform that more data is available. The observer
   * waits until either all data is loaded or AWT signals that the image cannot be loaded.
   *
   * @param     img   the image being observed.
   * @param     infoflags   the bitwise inclusive OR of the following
   *               flags:  <code>WIDTH</code>, <code>HEIGHT</code>,
   *               <code>PROPERTIES</code>, <code>SOMEBITS</code>,
   *               <code>FRAMEBITS</code>, <code>ALLBITS</code>,
   *               <code>ERROR</code>, <code>ABORT</code>.
   * @param     x   the <i>x</i> coordinate.
   * @param     y   the <i>y</i> coordinate.
   * @param     width    the width.
   * @param     height   the height.
   *
   * @return    <code>false</code> if the infoflags indicate that the
   *            image is completely loaded; <code>true</code> otherwise.
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
   * The workerthread. Simply draws the image to a BufferedImage's Graphics-Object
   * and waits for the AWT to load the image.
   */
  public void waitImageLoaded ()
  {
    BufferedImage img = new BufferedImage (1, 1, BufferedImage.TYPE_INT_RGB);
    Graphics g = img.getGraphics ();

    while (lock)
    {
      if (g.drawImage (image, 0, 0, img.getWidth (this), img.getHeight (this), this))
      {
        return;
      }
      try
      {
        Thread.currentThread ().sleep (200);
      }
      catch (InterruptedException e)
      {
        Log.info("WaitingImageObserver.waitImageLoaded(): InterruptedException thrown.", e);
      }
    }
  }

  /**
   * Clones this WaitingImageObserver.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    WaitingImageObserver obs = (WaitingImageObserver) super.clone ();
    return obs;
  }
}
