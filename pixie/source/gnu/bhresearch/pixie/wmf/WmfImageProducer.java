/*
 ConverterWmfToPxi.java - Parse Windows metafiles into PXI format.

 Copyright (c) 1997-1998 David R Harris.
 You can redistribute this work and/or modify it under the terms of the
 GNU Library General Public License version 2, as published by the Free
 Software Foundation. No warranty is implied. See lgpl.htm for details.

*/
package gnu.bhresearch.pixie.wmf;

import gnu.bhresearch.JImagePanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 *	Convert Windows metafiles into PXI format.
 */
public class WmfImageProducer implements ImageProducer
{
  private WmfFile metafile;
  private String inName;
  private ArrayList consumers;

  public WmfImageProducer(String inName, int width, int height)
      throws IOException
  {
    consumers = new ArrayList();
    metafile = new WmfFile(inName, width, height);
//    metafile.replay ();
  }

  public WmfImageProducer(URL inName, int width, int height)
      throws IOException
  {
    consumers = new ArrayList();
    metafile = new WmfFile(inName, width, height);
//    metafile.replay ();
  }

  public WmfImageProducer(URL inName)
      throws IOException
  {
    consumers = new ArrayList();
    metafile = new WmfFile(inName);
//    metafile.replay ();
  }

  public synchronized void addConsumer(ImageConsumer ic)
  {
    if (isConsumer(ic) == false)
      consumers.add(ic);
  }


  public synchronized boolean isConsumer(ImageConsumer ic)
  {
    return consumers.contains(ic);
  }


  public synchronized void removeConsumer(ImageConsumer ic)
  {
    consumers.remove(ic);
  }


  public synchronized void requestTopDownLeftRightResend(ImageConsumer ic)
  {
    startProduction(ic);
  }


  public synchronized void startProduction(ImageConsumer pic)
  {
    addConsumer(pic);

    ImageConsumer[] cons = (ImageConsumer[]) consumers.toArray(new ImageConsumer[consumers.size()]);

    BufferedImage image = metafile.replay();
    int w = image.getWidth();
    int h = image.getHeight();
    ColorModel model = image.getColorModel();

    for (int i = 0; i < cons.length; i++)
    {
      ImageConsumer ic = cons[i];
      ic.setDimensions(w, h);
      ic.setHints(ImageConsumer.TOPDOWNLEFTRIGHT);
      ic.setHints(ImageConsumer.SINGLEPASS);
      ic.setHints(ImageConsumer.COMPLETESCANLINES);
      ic.setColorModel(model);
    }

    final int LINES = 10;
    int[] pixels = new int[w * LINES];

    for (int i = 0; i < h; i += LINES)
    {
      int rows = 0;
      if ((i + LINES) > h)
        rows = h - i;
      else
        rows = LINES;

      pixels = image.getRGB(0, i, w, rows, pixels, 0, w);
      for (int j = 0; j < cons.length; j++)
      {
        ImageConsumer ic = cons[j];
        ic.setPixels(0, i, w, rows, model, pixels, 0, w);
      }
    }

    for (int i = 0; i < cons.length; i++)
    {
      ImageConsumer ic = cons[i];
      ic.imageComplete(ic.STATICIMAGEDONE);
    }
  }

  public static void main(String[] args)
      throws Exception
  {
    WmfImageProducer pr = new WmfImageProducer("./jfreereport/resource/anim0002.wmf", 1024, 768);
    JFrame frame = new JFrame();
    JPanel p = new JPanel();
    p.add(new JImagePanel(pr.metafile.replay()), BorderLayout.CENTER);
    p.setLayout(new BorderLayout());
    frame.setContentPane(p);
    frame.setSize(1024, 768);
    frame.setVisible(true);
  }

}
