package gnu.bhresearch;

import gnu.bhresearch.JImagePanel;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.WmfImageProducer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class TestRect
{
  private static double SCALEX = (double) 400 / (double) 27940;
  private static double SCALEY = (double) 400 / (double) 21590;

  private static double scaleY (double y)
  {
    System.out.println (SCALEY);
    System.out.println (y * SCALEY);
    return y * SCALEY;
  }

  private static double scaleX (double x)
  {
    System.out.println (x * SCALEX);
    return x * SCALEX;
  }

  public static void main (String[] args) throws Exception
  {
/*
    WmfFile wmf = new WmfFile ("./pixie/res/anim0002.wmf", 1024, 768);
    wmf.scaleToFit (1024, 768);
    wmf.replay ();
*/
    Image image = null;
    for (int i = 0; i < 10; i++)
    {
      WmfImageProducer prod = new WmfImageProducer("./pixie/res/anim0002.wmf", 9000, 9000);
      image = Toolkit.getDefaultToolkit().createImage(prod);
      BufferedImage bi = new BufferedImage (100, 100, BufferedImage.TYPE_INT_ARGB);
      bi.createGraphics().drawImage(image, 0,0, new JButton());
    }

    JFrame frame = new JFrame ();
    frame.addWindowListener (new WindowAdapter ()
    {
      public void windowClosing (WindowEvent evet)
      {
        System.exit (0);
      }
    });
    JPanel p = new JPanel ();
    p.setBorder (new EtchedBorder ());
    p.setLayout (new BorderLayout ());
    JImagePanel ipan = new JImagePanel (image);
    ipan.setScale (true);
    ipan.setScaleRatio (true);
    p.add (ipan, BorderLayout.CENTER);
    frame.setContentPane (p);
    frame.setSize (1024, 768);
    frame.setVisible (true);
  }
}