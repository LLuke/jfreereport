package gnu.bhresearch.pixie.image;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import gnu.bhresearch.pixie.*;
import gnu.bhresearch.pixie.command.*;
import javax.swing.*;
import gnu.bhresearch.*;

public class PixieImageProducer extends JComponent implements ImageProducer
{
  private PixieImage pixieimage;
  private Vector consumers;
  private BufferedImage image;
  private PixieHeader header;
  
  public PixieImageProducer (String filename)
    throws FileNotFoundException, IOException
  {
    this (filename, -1, -1);
  }

  public PixieImageProducer (RandomAccessFile in)
    throws IOException
  {
    this (in, -1, -1);
  }

  public PixieImageProducer (InputStream in)
    throws IOException
  {
    this (in, -1, -1);
  }

  public PixieImageProducer (String filename, int width, int height)
    throws FileNotFoundException, IOException
  {
    this (new RandomAccessFile (filename, "r"), width, height);
  }

  public PixieImageProducer (RandomAccessFile in, int width, int height)
    throws IOException
  {
    init (new PixieDataInput (in), width, height);
  }

  public PixieImageProducer (InputStream in, int width, int height)
    throws IOException
  {
    init (new PixieDataInput (new RandomAccessStream (in)), width, height);
  }

  private void init (PixieDataInput infile, int width, int height)
  throws IOException
  {
    consumers = new Vector ();

    PixieDataInput in = infile;
    header = new PixieHeader (in);
    
    int pixieWidth = header.getWidth();
    int pixieHeight = header.getHeight();
    in.setImageSize (pixieWidth, pixieHeight);
    in.seek (0);
    PixieImage loader = new PixieImage (in);
    infile.close();
    loader.setMaximumSize (new Dimension (pixieWidth, pixieHeight));
    
    image = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
    if (loader.getFrameCount() > 0)
    {
      PixieFrame frame = loader.getFrame(0);
      Dimension pixieDim = loader.getMaximumSize();
      frame.setScale (width / (float) pixieDim.width, height / (float) pixieDim.height);
      frame.paint (image.getGraphics());
    }
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
    PixieImageProducer pr = new PixieImageProducer ("./pixie/res/PixiLogo.pxi", 600,300);
    JFrame frame = new JFrame ();
    JPanel p = new JPanel ();
    p.setLayout (new BorderLayout ());
    p.add (new JImagePanel (pr.image), BorderLayout.CENTER);
    frame.setContentPane (p);
    frame.setSize (1024, 768);
    frame.setVisible (true);

  }
}
