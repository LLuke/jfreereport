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
 * --------------------
 * PDFOutputTarget.java
 * --------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PDFOutputTarget.java,v 1.11 2002/06/10 17:17:15 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 24-Apr-2002 : Support for Images and MultiLineElements.
 * 07-May-2002 : Small change for source of JFreeReport info to set creator of PDF document (DG);
 * 16-May-2002 : Interface of drawShape changhed so we can draw different line width (JS)
 * 27-May-2002 : Fonts are embedded now, TrueType fonts are loaded directly into the pdf.
 *               See report4.xml for a demo. An encoding property is added to support unicode.
 * 08-Jun-2002 : Documentation.
 * 10-Jun-2002 : Fixed a bug in FontFactory which caused the class to crash in Linux
 */

package com.jrefinery.report.targets;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.util.Log;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPatternPainter;
import com.lowagie.text.pdf.PdfWriter;
import com.keypoint.PngEncoder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * An output target for the report engine that generates a PDF file using the iText class library
 * (see http://www.lowagie.com/itext).
 * <p>
 * If the systemproperty "com.jrefinery.report.targets.PDFOutputTarget.AUTOINIT" is set to "true",
 * the PDF-FontFactory is automaticly initialized when this class is loaded. Be aware that
 * embedding many fonts will result in larger files.
 * <p>
 * When using unicode characters, you will have to adjust the encoding of this target to
 * "Identity-H", to enable horizontal unicode printing. This will result in larger files.
 */
public class PDFOutputTarget extends AbstractOutputTarget
{

  /** The output stream. */
  private OutputStream out;

  /** The document. */
  private Document document;

  /** The document writer. */
  private PdfWriter writer;

  /** Embed fonts? */
  private boolean embedFonts;

  /** Storage for BaseFont objects created. */
  private Map baseFonts;

  /** The current base font. */
  private BaseFont baseFont;

  private Font awtFont;

  /** The current font size. */
  private int fontSize;

  /** The stroke used for shapes */
  private Stroke awtStroke;

  /** The current Paint as used in the AWT */
  private Paint awtPaint;

  /** The current encoding */
  private String encoding;

  /**
   * The PDFBandCursor is used to translate between the band specific coordinate space
   * and the page coordinate space.
   */
  private static class PDFBandCursor extends BandCursor
  {
    public Rectangle2D getDrawBounds ()
    {
      Rectangle2D bbounds = getBandBounds ();
      Rectangle2D bounds = getElementBounds ();
      double x = bounds.getX () + bbounds.getX ();
      double y = bounds.getY () + bbounds.getY ();
      double w = bounds.getWidth ();
      double h = bounds.getHeight ();
      return new Rectangle2D.Double (x, y, w, h);
    }

    public void setDrawBounds (Rectangle2D rect)
    {
      Rectangle2D bbounds = getBandBounds ();
      super.setElementBounds (
              new Rectangle2D.Double (
                      rect.getX () - bbounds.getX (),
                      rect.getY () - bbounds.getY (),
                      rect.getWidth (),
                      rect.getHeight ()));
    }
  }

  /**
   * The pdf state is used to store and restore the current state of this outputtarget.
   */
  private static class PDFState
  {
    private Font myfont;
    private Stroke mystroke;
    private Paint mypaint;

    public PDFState (PDFOutputTarget source)
    {
      save (source);
    }

    public void save (PDFOutputTarget source)
    {
      this.myfont = source.getFont ();
      this.mystroke = source.getStroke ();
      this.mypaint = source.getPaint ();
    }

    public void restore (PDFOutputTarget target) throws OutputTargetException
    {
      target.setFont (myfont);
      target.setStroke (mystroke);
      target.setPaint (mypaint);
    }
  }

  /**
   * The PDFFontFactory is used to find and register all TrueTypeFonts for embedding them
   * in the PDF-File.
   */
  public static class PDFFontFactory
  {
    private Hashtable fontsByName;

    private PDFFontFactory ()
    {
      fontsByName = new Hashtable ();
    }

    /**
     * Register os-specific font paths to the PDF-FontFactory. For unix-like operating
     * systems, X11 is searched in /usr/X11R6 and the default truetype fontpath is added.
     * For windows the system font path is added (%windir%/fonts)
     */
    public void registerDefaultFontPath ()
    {
      String osname = System.getProperty ("os.name");
      String jrepath = System.getProperty ("java.home");
      String fontPath = null;
      String fs = System.getProperty ("file.separator");

      Log.debug ("Running on operating system: " + osname);
      if (!startsWithIgnoreCase(osname, "windows"))
      {
        Log.debug ("Assuming unix like file structures");
        // Assume X11 is installed in the default location.
        registerFontPath (new File (jrepath, "lib/fonts").toString ());
        fontPath = "/usr/X11R6/lib/X11/fonts/truetype";
      }
      else
      {
        Log.debug ("Found windows in os name, assuming DOS/Win32 structures");
        // Assume windows
        // If you are not using windows, ignore this. This just checks if a windows system
        // directory exist and includes a font dir.

        String windirs = System.getProperty ("java.library.path");
        if (windirs != null)
        {
          StringTokenizer strtok = new StringTokenizer (windirs, System.getProperty ("path.separator"));
          while (strtok.hasMoreTokens ())
          {
            String token = strtok.nextToken ();

            if (token.endsWith ("System32"))
            {
              // found windows folder ;-)
              int lastBackslash = token.lastIndexOf (fs);
              fontPath = token.substring (0, lastBackslash) + fs + "Fonts";

              break;
            }
          }
        }
      }

      Log.debug ("Fonts located in \"" + fontPath + "\"");
      if (fontPath != null)
      {
        registerFontPath (fontPath);
      }
      registerFontPath (new File (jrepath, "lib" + fs + "fonts").toString ());
    }

    /**
     * Register all fonts (*.ttf files) in the given path.
     */
    public void registerFontPath (String path)
    {
      File file = new File (path);
      if (file.exists () && file.isDirectory () && file.canRead ())
      {
        File[] files = file.listFiles ();
        for (int i = 0; i < files.length; i++)
        {
          registerFontFile (files[i].toString ());
        }
      }
      file = null;
      System.gc ();
    }

    /**
     * Register the font (must end this *.ttf) to the FontFactory.
     */
    public void registerFontFile (String filename)
    {
      if (!filename.toLowerCase ().endsWith ("ttf"))
      {
        return;
      }
      File file = new File (filename);
      if (file.exists () && file.isFile () && file.canRead ())
      {
        try
        {
          addFont (filename);
          Log.debug ("Registered truetype font " + filename);
        }
        catch (Exception e)
        {
          Log.warn ("Font " + filename + " is invalid." + e.getMessage ());
        }
      }
    }

    /**
     * Adds the font by creating the basefont object
     */
    private void addFont (String font)
            throws DocumentException, IOException
    {
      BaseFont bfont = BaseFont.createFont (font, BaseFont.WINANSI, true);
      String[][] fi = bfont.getFullFontName ();
      for (int i = 0; i < fi.length; i++)
      {
        String[] ffi = fi[i];
        fontsByName.put (ffi[3], font);
      }
    }

    /**
     * Returns all registered fonts as enumeration.
     */
    public Enumeration getRegisteredFonts ()
    {
      return fontsByName.keys ();
    }

    /**
     * Returns the font by looking up the name in the Factory.
     */
    public String getFontfileForName (String font)
    {
      return (String) fontsByName.get (font);
    }
  }

  private static PDFFontFactory fontFactory;

  /**
   * returns/creates the singleton font factory.
   */
  public static PDFFontFactory getFontFactory ()
  {
    if (fontFactory == null)
    {
      fontFactory = new PDFFontFactory ();
    }
    return fontFactory;
  }

  /**
   * Initialialize the font factory when this class is loaded and the system property
   * of  <code>"com.jrefinery.report.targets.PDFOutputTarget.AUTOINIT"</code> is set to
   * <code>true</code>
   */
  static
  {
    String prop = System.getProperty ("com.jrefinery.report.targets.PDFOutputTarget.AUTOINIT", "false");
    if (prop.equalsIgnoreCase ("true"))
    {
      getFontFactory ().registerDefaultFontPath ();
    }
  }

  /**
   * Creates a PDFBandCursor to support coordinate space transformation.
   */
  public BandCursor createCursor ()
  {
    return new PDFBandCursor ();
  }

  /**
   * Constructs a PDFOutputTarget.
   *
   * @param out The output stream.
   * @param pageFormat The page format.
   * @param embedFonts Embed fonts?
   */
  public PDFOutputTarget (OutputStream out, PageFormat pageFormat, boolean embedFonts)
  {

    super (pageFormat);
    this.out = out;
    this.embedFonts = embedFonts;
    this.baseFonts = new TreeMap ();
    setFontEncoding (System.getProperty ("com.jrefinery.report.targets.PDFOutputTarget.ENCODING", BaseFont.WINANSI));
  }


  /**
   * Returns the coordinate of the left edge of the page.
   */
  public float getPageX ()
  {
    return this.document.getPageSize ().left ();
  }

  /**
   * Returns the coordinate of the top of the page.
   * <P>
   * NOTE: JFreeReport works with y values increasing as you move down the page, whereas iText
   * works with y values increasing as you move up the page - this method translates the
   * iText value to match JFreeReport.
   */
  public float getPageY ()
  {
    return this.document.getPageSize ().bottom ();
  }

  /**
   * Returns the total width of the page.
   */
  public float getPageWidth ()
  {
    return this.document.getPageSize ().width ();
  }

  /**
   * Returns the total height of the page.
   */
  public float getPageHeight ()
  {
    return this.document.getPageSize ().height ();
  }

  /**
   * Returns the coordinate of the left hand edge of the usable area on the page (after taking
   * into account the left margin).
   */
  public float getUsableX ()
  {
    return this.document.getPageSize ().left () + this.document.leftMargin ();
  }

  /**
   * Returns the coordinate of the top edge of the usable area on the page (after taking
   * into account the top margin and translating to JFreeReport's orientation).
   */
  public float getUsableY ()
  {
    return this.document.getPageSize ().bottom () + this.document.topMargin ();
  }

  /**
   * Returns the width of the usable area on the page (after taking into account margins).
   */
  public float getUsableWidth ()
  {
    return this.document.getPageSize ().width ()
            - this.document.leftMargin ()
            - this.document.rightMargin ();
  }

  /**
   * Returns the height of the usable area on the page (after taking into account margins).
   */
  public float getUsableHeight ()
  {
    return this.document.getPageSize ().height ()
            - this.document.topMargin ()
            - this.document.bottomMargin ();
  }

  /**
   * Returns the currently active AWT-Font.
   */
  public Font getFont ()
  {
    return awtFont;
  }

  /**
   * Returns the current fonts fontsize.
   */
  public int getFontSize ()
  {
    return fontSize;
  }

  /**
   * Helper functions to query a strings start portion.
   *
   * @see String#startsWith
   */
  private static boolean startsWithIgnoreCase (String base, String start)
  {
    return base.regionMatches (true, 0, start, 0, start.length ());
  }

  /**
   * Helper functions to query a strings end portion.
   *
   * @see String#endsWith
   */
  private boolean endsWithIgnoreCase (String base, String end)
  {
    if (base.length () < end.length ())
      return false;
    return base.regionMatches (true, base.length () - end.length (), end, 0, end.length ());
  }

  /**
   * Sets the current font. The font is mapped to pdf specific fonts if possible.
   * If no basefont could be created, an OutputTargetException is thrown.
   */
  public void setFont (Font font) throws OutputTargetException
  {
    if (font == null) throw new NullPointerException ();

    // do nothing if this font is already set
    if (awtFont != null && awtFont.equals (font)) return;

    this.awtFont = font;
    this.fontSize = font.getSize ();

    // use the Java logical font name to map to a predefined iText font.

    String fontKey = null;
    String logicalName = font.getName ();

    if (startsWithIgnoreCase (logicalName, "dialoginput") ||
            startsWithIgnoreCase (logicalName, "monospaced"))
    {
      boolean bold = false;
      boolean italic = false;

      if (endsWithIgnoreCase (logicalName, "bolditalic") ||
              (font.isBold () && font.isItalic ()))
      {
        bold = true;
        italic = true;
      }
      else if (endsWithIgnoreCase (logicalName, "bold") ||
              (font.isBold ()))
      {
        bold = true;
      }
      else if (endsWithIgnoreCase (logicalName, "italic") ||
              (font.isItalic ()))
      {
        italic = true;
      }

      if (bold && italic)
      {
        fontKey = BaseFont.COURIER_BOLDOBLIQUE;
      }
      else if (bold)
      {
        fontKey = BaseFont.COURIER_BOLD;
      }
      else if (italic)
      {
        fontKey = BaseFont.COURIER_OBLIQUE;
      }
      else
      {
        fontKey = BaseFont.COURIER;
      }

    }
    else if (startsWithIgnoreCase (logicalName, "Serif"))
    {
      boolean bold = false;
      boolean italic = false;

      if (endsWithIgnoreCase (logicalName, "bolditalic") ||
              (font.isBold () && font.isItalic ()))
      {
        bold = true;
        italic = true;
      }
      else if (endsWithIgnoreCase (logicalName, "bold") ||
              (font.isBold ()))
      {
        bold = true;
      }
      else if (endsWithIgnoreCase (logicalName, "italic") ||
              (font.isItalic ()))
      {
        italic = true;
      }

      if (bold && italic)
      {
        fontKey = BaseFont.TIMES_BOLDITALIC;
      }
      else if (bold)
      {
        fontKey = BaseFont.TIMES_BOLD;
      }
      else if (italic)
      {
        fontKey = BaseFont.TIMES_ITALIC;
      }
      else
      {
        fontKey = BaseFont.TIMES_ROMAN;
      }

    }
    else if (startsWithIgnoreCase (logicalName, "SansSerif") ||
            startsWithIgnoreCase (logicalName, "Dialog"))
    { // default, this catches Dialog and SansSerif
      boolean bold = false;
      boolean italic = false;

      if (endsWithIgnoreCase (logicalName, "bolditalic") ||
              (font.isBold () && font.isItalic ()))
      {
        bold = true;
        italic = true;
      }
      else if (endsWithIgnoreCase (logicalName, "bold") ||
              (font.isBold ()))
      {
        bold = true;
      }
      else if (endsWithIgnoreCase (logicalName, "italic") ||
              (font.isItalic ()))
      {
        italic = true;
      }

      if (bold && italic)
      {
        fontKey = BaseFont.HELVETICA_BOLDOBLIQUE;
      }
      else if (bold)
      {
        fontKey = BaseFont.HELVETICA_BOLD;
      }
      else if (italic)
      {
        fontKey = BaseFont.HELVETICA_OBLIQUE;
      }
      else
      {
        fontKey = BaseFont.HELVETICA;
      }
    }
    else
    {
      fontKey = logicalName;
    }

    BaseFont f = (BaseFont) this.baseFonts.get (fontKey);

    if (f == null)
    {
      try
      {
        String filename = getFontFactory ().getFontfileForName (fontKey);
        if (filename != null)
        {
          if (font.isBold () && font.isItalic ())
          {
            fontKey = filename + ",BoldItalic";
          }
          else if (font.isBold ())
          {
            fontKey = filename + ",Bold";
          }
          else if (font.isItalic ())
          {
            fontKey = filename + ",Italic";
          }
          else
          {
            fontKey = filename;
          }
        }
        f = BaseFont.createFont (fontKey, getFontEncoding (), this.embedFonts);
      }
      catch (Exception e)
      {
        Log.warn ("BaseFont.createFont failed.", e);
      }
      if (f == null)
      {
        // fallback .. use BaseFont.HELVETICA as default
        try
        {
          f = BaseFont.createFont (BaseFont.HELVETICA, getFontEncoding (), this.embedFonts);
        }
        catch (Exception e)
        {
          Log.warn ("BaseFont.createFont for FALLBACK failed.", e);
        }
      }
    }

    if (f == null)
    {
      throw new OutputTargetException ("Null font = " + fontKey);
    }
    else
    {
      this.baseFonts.put (fontKey, f);
    }

    this.baseFont = f;

  }

  /**
   * Draws an Image from this imageReference. The image is directly embedded into the
   * pdf file to provide the best scaling support.
   */
  public void drawImage (ImageReference imageRef) throws OutputTargetException
  {
    try
    {
      Rectangle2D bounds = getCursor ().getDrawBounds ();

      Image image = Image.getInstance (imageRef.getSourceURL ());
      image.setAbsolutePosition ((float) bounds.getX (),
              (float) (getPageHeight () - bounds.getY () - bounds.getHeight ()));
      image.scaleAbsolute ((float) bounds.getWidth (), (float) bounds.getHeight ());

      document.add (image);
    }
    catch (BadElementException be)
    {
      throw new OutputTargetException ("BadElementException", be);
    }
    catch (DocumentException de)
    {
      throw new OutputTargetException ("DocumentException", de);
    }
    catch (MalformedURLException mf)
    {
      throw new OutputTargetException ("Invalid URL in ImageReference", mf);
    }
    catch (IOException mf)
    {
      throw new OutputTargetException ("URL Content could not be read", mf);
    }
  }


  private Image getImage (ImageReference imageRef) throws DocumentException, IOException
  {
    if (imageRef.getSourceURL() != null)
      return Image.getInstance(imageRef.getSourceURL());

    if (imageRef.getImage() != null)
    {
      PngEncoder encoder = new PngEncoder(imageRef.getImage());
      byte[] data = encoder.pngEncode();
      return Image.getInstance(data);
    }

    throw new DocumentException ("Neither an URL nor an Image was given to paint the graphics");
  }

  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is not filled.
   */
  public void drawShape (Shape shape)
  {
    Rectangle2D bounds = getCursor ().getDrawBounds ();
    float ycorr = (float) (getPageHeight () - bounds.getY ());
    float xcorr = (float) bounds.getX ();

    PathIterator pit = shape.getPathIterator (null);
    PdfContentByte cb = this.writer.getDirectContent ();

    float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone () == false)
    {
      int cmd = pit.currentSegment (params);
      switch (cmd)
      {
        case PathIterator.SEG_MOVETO:
          {
            cb.moveTo (params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            cb.lineTo (params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            cb.curveTo (params[0] + xcorr, ycorr - params[1],
                    params[2] + xcorr, ycorr - params[3],
                    params[4] + xcorr, ycorr - params[5]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            cb.curveTo (params[0] + xcorr, ycorr - params[1],
                    params[2] + xcorr, ycorr - params[3]);
            break;
          }
        case PathIterator.SEG_CLOSE:
          {
            cb.closePath ();
          }
      }
      pit.next ();
    }
    cb.stroke ();
  }

  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is filled using
   * the current paint and no outline is drawn.
   */
  public void fillShape (Shape shape)
  {
    Rectangle2D bounds = getCursor ().getDrawBounds ();
    float ycorr = (float) (getPageHeight () - bounds.getY ());
    float xcorr = (float) bounds.getX ();

    PathIterator pit = shape.getPathIterator (null);
    PdfContentByte cb = this.writer.getDirectContent ();
    int windingRule = pit.getWindingRule ();

    float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone () == false)
    {
      int cmd = pit.currentSegment (params);
      switch (cmd)
      {
        case PathIterator.SEG_MOVETO:
          {
            cb.moveTo (params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            cb.lineTo (params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            cb.curveTo (params[0] + xcorr, ycorr - params[1],
                    params[2] + xcorr, ycorr - params[3],
                    params[4] + xcorr, ycorr - params[5]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            cb.curveTo (params[0] + xcorr, ycorr - params[1],
                    params[2] + xcorr, ycorr - params[3]);
            break;
          }
        case PathIterator.SEG_CLOSE:
          {
            cb.closePath ();
          }
      }
      pit.next ();
    }
    if (windingRule == PathIterator.WIND_EVEN_ODD)
    {
      cb.eoFill ();
    }
    else
    {
      cb.fill ();
    }
  }

  /**
   * This method is called when the page is ended.
   */
  public void endPage () throws OutputTargetException
  {
    try
    {
      this.document.newPage ();
    }
    catch (Exception e)
    {
      throw new OutputTargetException ("Failed to end page", e);
    }
  }

  /**
   * Opens the document.  The report title and author are added to the PDF document properties.
   *
   * @param title The report title.
   * @param author The report author.
   */
  public void open (String title, String author) throws OutputTargetException
  {
    PageFormat pageFormat = getPageFormat ();
    float urx = (float) pageFormat.getWidth ();
    float ury = (float) pageFormat.getHeight ();

    float marginLeft = (float) pageFormat.getImageableX ();
    float marginRight =
            (float) (pageFormat.getWidth ()
            - pageFormat.getImageableWidth ()
            - pageFormat.getImageableX ());
    float marginTop = (float) pageFormat.getImageableY ();
    float marginBottom =
            (float) (pageFormat.getHeight ()
            - pageFormat.getImageableHeight ()
            - pageFormat.getImageableY ());
    Rectangle pageSize = new Rectangle (urx, ury);

    try
    {
      if (pageFormat.getOrientation () != PageFormat.PORTRAIT)
      {
        pageSize.rotate ();
      }

      this.document = new Document (pageSize, marginLeft, marginRight, marginTop, marginBottom);
      document.addTitle (title);
      document.addAuthor (author);
      document.addCreator (
              JFreeReport.INFO.getName () + " version " + JFreeReport.INFO.getVersion ());
      document.addCreationDate ();

      writer = PdfWriter.getInstance (document, out);
      this.document.open ();

      try
      {
        setPaint (Band.DEFAULT_PAINT);
        setStroke (ShapeElement.DEFAULT_STROKE);
        setFont (Band.DEFAULT_FONT);
      }
      catch (OutputTargetException oe)
      {
        Log.error ("Should not happen", oe);
      }
    }
    catch (Exception e)
    {
      throw new OutputTargetException ("Opening Document failed.", e);
    }

  }

  /**
   * Closes the document.
   */
  public void close ()
  {
    this.document.close ();
  }

  /**
   * Draws the band onto the specified graphics device. The Text is printed on the
   * bottom of the elements bounds.
   *
   * @param mytext The text to be printed.
   * @param align The vertical alignment for the text.
   */
  public void drawString (
          String text,
          int alignment)
  {
    Rectangle2D bounds = getCursor ().getDrawBounds ();

    PdfContentByte cb = this.writer.getDirectContent ();
    cb.beginText ();
    cb.setFontAndSize (this.baseFont, this.fontSize);

    float y2 = (float) (bounds.getY () + bounds.getHeight ());
    if (alignment == Element.LEFT)
    {
      cb.showTextAligned (
              PdfContentByte.ALIGN_LEFT,
              text,
              (float) bounds.getX (),
              this.getPageHeight () - y2,
              0);
    }
    else if (alignment == Element.CENTER)
    {
      cb.showTextAligned (
              PdfContentByte.ALIGN_CENTER,
              text,
              (float) (bounds.getX () + (bounds.getWidth () / 2)),
              this.getPageHeight () - y2,
              0);
    }
    else if (alignment == Element.RIGHT)
    {
      cb.showTextAligned (
              PdfContentByte.ALIGN_RIGHT,
              text,
              (float) (bounds.getX () + bounds.getWidth ()),
              this.getPageHeight () - y2,
              0);
    }
    cb.endText ();
  }

  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param text the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   * @returns the width of the given string in 1/72" dpi.
   */
  protected float getStringBounds (String text, int lineStartPos, int endPos)
  {
    return (float) baseFont.getWidthPoint (text.substring (lineStartPos, endPos), getFontSize ());
  }

  /**
   * Returns the height of the current font. The font height specifies the distance between
   * 2 base lines.
   */
  protected float getFontHeight ()
  {
    return getFontSize ();
  }

  /**
   * Defines the stroke used to draw shapes. If the stroke is of an invalid type, an
   * OutputTargetException is thrown. Currently only BasicStroke is supported.
   *
   * @throws OutputTargetException
   */
  public void setStroke (Stroke stroke) throws OutputTargetException
  {
    if (stroke == null) throw new NullPointerException ();
    if (stroke instanceof BasicStroke == false) throw new OutputTargetException ("Unable to handle this stroke type");

    // If this stroke is already set, do nothing
    if (awtStroke != null && awtStroke.equals (stroke)) return;

    this.awtStroke = stroke;
    BasicStroke bstroke = (BasicStroke) stroke;
    PdfContentByte cb = this.writer.getDirectContent ();
    cb.setLineWidth (bstroke.getLineWidth ());
  }

  /**
   * Returns the currently set stroke.
   */
  public Stroke getStroke ()
  {
    return awtStroke;
  }

  /**
   * Sets the paint. If the paint could not be converted into a pdf object, an OutputTargetException
   * is thrown. This implementation currently supports java.awt.Color as the only valid paint.
   *
   * @throws OutputTargetException if the paint is invalid.
   */
  public void setPaint (Paint paint) throws OutputTargetException
  {
    if (paint == null) throw new NullPointerException ();

    if (paint instanceof Color == false)
    {
      throw new OutputTargetException ("Unsupported paint type, currently only color is supported");
    }

    // If this paint is already set, do nothing
    if (awtPaint != null && awtPaint.equals (paint)) return;

    this.awtPaint = paint;
    PdfContentByte cb = this.writer.getDirectContent ();
    PdfPatternPainter painter = null;

    cb.setColorStroke ((Color) paint);
    cb.setColorFill ((Color) paint);
  }

  /**
   * Returns the currently set paint.
   */
  public Paint getPaint ()
  {
    return awtPaint;
  }

  /**
   * Defines the current clipping are for the band to be drawn. This method is called by
   * the band and should not be called by other entities.
   */
  public void setClippingArea (Rectangle2D bounds)
  {
    super.setClippingArea (bounds);
  }

  /**
   * Restores the state of this target.
   *
   * @throws OutputTargetException if the given state object is not valid.
   */
  public void restoreState (Object o) throws OutputTargetException
  {
    if (o instanceof PDFState == false) throw new OutputTargetException ("Need a pdf state");
    PDFState state = (PDFState) o;
    state.restore (this);
  }

  /**
   * Saves the state of this graphics object. Use restoreState to restore a previously saved
   * state.
   *
   * @returns the state container.
   */
  public Object saveState () throws OutputTargetException
  {
    return new PDFState (this);
  }

  /**
   * returns the font encoding used in this output target.
   */
  public String getFontEncoding ()
  {
    return encoding;
  }

  /**
   * Defines the text encoding used in this outputtarget.
   *
   * <ul>
   * <li>The Unicode encoding with horizontal writing is "Identity-H"
   * <li>The Unicode encoding with vertical writing is "Identity-V"
   * <li>"Cp1250"
   * <li>"Cp1252" is also known as WinAnsi
   * <li>"Cp1257"
   * <li>"MacRoman"
   * </ul>
   */
  public void setFontEncoding (String encoding)
  {
    if (encoding == null) throw new NullPointerException ();
    this.encoding = encoding;
  }
}
