/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * WmfImageProducer.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: WmfImageProducer.java,v 1.3 2004/01/19 18:36:25 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Implements the ImageProducer interface for the MetaFiles
 */
public class WmfImageProducer implements ImageProducer
{
  private WmfFile metafile;
  private ArrayList consumers;

  public WmfImageProducer (final String inName, final int width, final int height)
          throws IOException
  {
    consumers = new ArrayList();
    metafile = new WmfFile(inName, width, height);
//    metafile.replay ();
  }

  public WmfImageProducer (final URL inName, final int width, final int height)
          throws IOException
  {
    consumers = new ArrayList();
    metafile = new WmfFile(inName, width, height);
//    metafile.replay ();
  }

  public WmfImageProducer (final URL inName)
          throws IOException
  {
    consumers = new ArrayList();
    metafile = new WmfFile(inName);
//    metafile.replay ();
  }

  public synchronized void addConsumer (final ImageConsumer ic)
  {
    if (isConsumer(ic))
    {
      return;
    }

    consumers.add(ic);
  }


  public synchronized boolean isConsumer (final ImageConsumer ic)
  {
    return consumers.contains(ic);
  }


  public synchronized void removeConsumer (final ImageConsumer ic)
  {
    consumers.remove(ic);
  }


  public synchronized void requestTopDownLeftRightResend (final ImageConsumer ic)
  {
    startProduction(ic);
  }


  public synchronized void startProduction (final ImageConsumer pic)
  {
    if (pic != null)
    {
      addConsumer(pic);
    }

    final ImageConsumer[] cons = (ImageConsumer[]) consumers.toArray(new ImageConsumer[consumers.size()]);
    final BufferedImage image = metafile.replay();

    final int w = image.getWidth();
    final int h = image.getHeight();
    final ColorModel model = image.getColorModel();

    for (int i = 0; i < cons.length; i++)
    {
      final ImageConsumer ic = cons[i];
      ic.setHints(ImageConsumer.TOPDOWNLEFTRIGHT);
      ic.setHints(ImageConsumer.SINGLEFRAME);
      ic.setHints(ImageConsumer.SINGLEPASS);
      ic.setHints(ImageConsumer.COMPLETESCANLINES);
      ic.setDimensions(w, h);
      ic.setColorModel(model);
    }

    final int LINES = 10;
    int[] pixels = new int[w * LINES];

    for (int i = 0; i < h; i += LINES)
    {
      int rows = 0;
      if ((i + LINES) > h)
      {
        rows = h - i;
      }
      else
      {
        rows = LINES;
      }

      pixels = image.getRGB(0, i, w, rows, pixels, 0, w);
      for (int j = 0; j < cons.length; j++)
      {
        final ImageConsumer ic = cons[j];
        ic.setPixels(0, i, w, rows, model, pixels, 0, w);
      }
    }

    for (int i = 0; i < cons.length; i++)
    {
      final ImageConsumer ic = cons[i];
      ic.imageComplete(ImageConsumer.STATICIMAGEDONE);
    }

    if (pic != null)
    {
      removeConsumer(pic);
    }
  }
}
