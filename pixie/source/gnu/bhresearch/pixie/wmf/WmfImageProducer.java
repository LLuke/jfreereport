/*
 ConverterWmfToPxi.java - Parse Windows metafiles into PXI format.

 Copyright (c) 1997-1998 David R Harris.
 You can redistribute this work and/or modify it under the terms of the
 GNU Library General Public License version 2, as published by the Free
 Software Foundation. No warranty is implied. See lgpl.htm for details.

*/
package gnu.bhresearch.pixie.wmf;

import java.io.InputStream;
import java.io.IOException;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Font;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.ImageConsumer;
import gnu.bhresearch.quant.Debug;
import gnu.bhresearch.quant.Assert;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Vector;
import java.awt.BorderLayout;
import gnu.bhresearch.JImagePanel;
import java.net.URL;

/**
 *	Convert Windows metafiles into PXI format.
 */
public class WmfImageProducer implements ImageProducer
{
  private WmfFile metafile;
  private String inName;
  private Vector consumers;  
    
  public WmfImageProducer (String inName, int width, int height) 
    throws IOException
  {
    consumers = new Vector ();
    metafile = new WmfFile (inName, width, height);
//    metafile.replay ();
  }

  public WmfImageProducer (URL inName, int width, int height) 
    throws IOException
  {
    consumers = new Vector ();
    metafile = new WmfFile (inName, width, height);
//    metafile.replay ();
  }

  public WmfImageProducer (URL inName) 
    throws IOException
  {
    consumers = new Vector ();
    metafile = new WmfFile (inName);
//    metafile.replay ();
  }

  public void addConsumer(ImageConsumer ic)
  {
    consumers.add (ic);
  }


  public boolean isConsumer(ImageConsumer ic)
  {
    return consumers.contains (ic);
  }


  public void removeConsumer(ImageConsumer ic)
  {
    consumers.remove (ic);
  }


  public void requestTopDownLeftRightResend(ImageConsumer ic)
  {
    startProduction (ic);
  }


  public void startProduction(ImageConsumer ic)
  {
    int startX = 0;
    int startY = 0;
    BufferedImage image = metafile.replay ();
    int w = image.getWidth();
    int h = image.getHeight();
    ColorModel model = image.getColorModel ();
    
    int[] pixels = image.getRGB (startX, startY, w, h, null, 0, w);
    ic.setDimensions (w, h);
    ic.setHints (ic.SINGLEPASS);
    ic.setHints (ic.SINGLEFRAME);
    ic.setPixels (startX, startY, w, h, model, pixels, 0, w);
    ic.imageComplete (ic.STATICIMAGEDONE);
  }

  public static void main (String [] args)
  throws Exception
  {
    WmfImageProducer pr = new WmfImageProducer ("./jfreereport/resource/anim0002.wmf", 1024,768);
    JFrame frame = new JFrame ();
    JPanel p = new JPanel ();
    p.add (new JImagePanel (pr.metafile.replay()), BorderLayout.CENTER);
    p.setLayout (new BorderLayout ());
    frame.setContentPane (p);
    frame.setSize (1024, 768);
    frame.setVisible (true);
  }
  
}
