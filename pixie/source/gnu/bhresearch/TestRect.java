
import gnu.bhresearch.JImagePanel;
import gnu.bhresearch.pixie.wmf.WmfFile;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
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
//    BufferedImage image = new BufferedImage (400,400, BufferedImage.TYPE_INT_ARGB);
//    Graphics2D g = image.createGraphics ();
//    Rectangle2D rect = new Rectangle2D.Double (10,10,50,50);
//    rect.add (new Rectangle2D.Double (40,40,90,90));
//    MfLogPen pen = new MfLogPen ();
//    pen.setWidth (1);
//    pen.setStyle (4);
//
//    MfLogBrush brush = new MfLogBrush ();
//    brush.setHatchedStyle (2);
//    brush.setStyle (2);
//    brush.setColor (Color.black);
//    BufferedImage bim = brush.createHatchStyle (2,Color.black.getRGB());
//
//    g = image.createGraphics ();
//    g.setColor (Color.black);
//    g.setPaint (brush.getPaint());
    //g.setStroke (pen.getStroke ());
    //g.draw (new Line2D.Double (scaleX(20000),scaleY(20000),scaleX(40000),scaleY(40000)));
    //g.draw (new Rectangle (18908,16778,1177,1209));
    //g.drawImage (bim, 0,0, new JButton());
    //g.fill (new Rectangle (18,16,177,209));

//    WmfFile wmf = new WmfFile ("./pixie/res/anim0002.wmf", 1024, 768);
    WmfFile wmf = new WmfFile ("./pixie/res/test.wmf");
    wmf.scaleToFit (1024, 768);
    wmf.replay ();

    BufferedImage image = wmf.getImage ();

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


























