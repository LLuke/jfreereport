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
 * $Id: PDFOutputTarget.java,v 1.24 2002/09/06 17:02:43 taqua Exp $
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
 * 17-Jul-2002 : Fixed a nullpointer when an ImageReference did not contain a graphics
 */

package com.jrefinery.report.targets;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.util.Log;
import com.keypoint.PngEncoder;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.DocWriter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPatternPainter;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
  public static final String SECURITY_ALLOW_PRINTING = "AllowPrinting";
  public static final String SECURITY_ALLOW_MODIFY_CONTENTS = "AllowModifyContents";
  public static final String SECURITY_ALLOW_COPY = "AllowCopy";
  public static final String SECURITY_ALLOW_MODIFY_ANNOTATIONS = "AllowModifyAnnotations";
  public static final String SECURITY_ALLOW_FILLIN = "AllowFillIn";
  public static final String SECURITY_ALLOW_SCREENREADERS = "AllowScreenReaders";
  public static final String SECURITY_ALLOW_ASSEMBLY = "AllowAssembly";
  public static final String SECURITY_ALLOW_DEGRADED_PRINTING = "AllowDegradedPrinting";

  public static final String SECURITY_ENCRYPTION = "Encryption";
  public static final Boolean SECURITY_ENCRYPTION_40BIT = Boolean.FALSE;
  public static final Boolean SECURITY_ENCRYPTION_128BIT = Boolean.TRUE;

  public static final String SECURITY_USERPASSWORD = "userpassword";
  public static final String SECURITY_OWNERPASSWORD = "ownerpassword";

  private static final String CREATOR = JFreeReport.getInfo().getName() + " version " + JFreeReport.getInfo().getVersion();

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
   * An bytearray containing an empty password. iText replaces the owner password with random
   * values, but Adobe allows to have encryption without an owner password set.
   */
  private static final byte PDF_PASSWORD_PAD[] = {
      (byte)0x28, (byte)0xBF, (byte)0x4E, (byte)0x5E, (byte)0x4E, (byte)0x75,
      (byte)0x8A, (byte)0x41, (byte)0x64, (byte)0x00, (byte)0x4E, (byte)0x56,
      (byte)0xFF, (byte)0xFA, (byte)0x01, (byte)0x08, (byte)0x2E, (byte)0x2E,
      (byte)0x00, (byte)0xB6, (byte)0xD0, (byte)0x68, (byte)0x3E, (byte)0x80,
      (byte)0x2F, (byte)0x0C, (byte)0xA9, (byte)0xFE, (byte)0x64, (byte)0x53,
      (byte)0x69, (byte)0x7A};

  /**
   * The PDFBandCursor is used to translate between the band specific coordinate space
   * and the page coordinate space.
   */
  private static class PDFBandCursor extends BandCursor
  {
    public Rectangle2D getDrawBounds()
    {
      Rectangle2D bbounds = getBandBounds();
      Rectangle2D bounds = getElementBounds();
      double x = bounds.getX() + bbounds.getX();
      double y = bounds.getY() + bbounds.getY();
      double w = bounds.getWidth();
      double h = bounds.getHeight();
      return new Rectangle2D.Double(x, y, w, h);
    }

    public void setDrawBounds(Rectangle2D rect)
    {
      Rectangle2D bbounds = getBandBounds();
      super.setElementBounds(
          new Rectangle2D.Double(
              rect.getX() - bbounds.getX(),
              rect.getY() - bbounds.getY(),
              rect.getWidth(),
              rect.getHeight()));
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

    public PDFState(PDFOutputTarget source)
    {
      save(source);
    }

    public void save(PDFOutputTarget source)
    {
      this.myfont = source.getFont();
      this.mystroke = source.getStroke();
      this.mypaint = source.getPaint();
    }

    public void restore(PDFOutputTarget target) throws OutputTargetException
    {
      target.setFont(myfont);
      target.setStroke(mystroke);
      target.setPaint(mypaint);
    }
  }

  /**
   * The PDFFontFactory is used to find and register all TrueTypeFonts for embedding them
   * in the PDF-File.
   */
  public static class PDFFontFactory extends DefaultFontMapper
  {
    private Hashtable fontsByName;

    private PDFFontFactory()
    {
      fontsByName = new Hashtable();
    }

    /**
     * Register os-specific font paths to the PDF-FontFactory. For unix-like operating
     * systems, X11 is searched in /usr/X11R6 and the default truetype fontpath is added.
     * For windows the system font path is added (%windir%/fonts)
     */
    public void registerDefaultFontPath()
    {
      String encoding = getDefaultFontEncoding();
      // Correct the encoding for truetype fonts
      if (encoding.equals(BaseFont.IDENTITY_H) || encoding.equals(BaseFont.IDENTITY_V))
      {
        encoding = "iso-8859-1";
      }

      String osname = System.getProperty("os.name");
      String jrepath = System.getProperty("java.home");
      String fontPath = null;
      String fs = System.getProperty("file.separator");

      Log.debug("Running on operating system: " + osname);
      if (!startsWithIgnoreCase(osname, "windows"))
      {
        Log.debug("Assuming unix like file structures");
        // Assume X11 is installed in the default location.
        fontPath = "/usr/X11R6/lib/X11/fonts/truetype";
      }
      else
      {
        Log.debug("Found windows in os name, assuming DOS/Win32 structures");
        // Assume windows
        // If you are not using windows, ignore this. This just checks if a windows system
        // directory exist and includes a font dir.

        String windirs = System.getProperty("java.library.path");
        if (windirs != null)
        {
          StringTokenizer strtok = new StringTokenizer(windirs, System.getProperty("path.separator"));
          while (strtok.hasMoreTokens())
          {
            String token = strtok.nextToken();

            if (token.endsWith("System32"))
            {
              // found windows folder ;-)
              int lastBackslash = token.lastIndexOf(fs);
              fontPath = token.substring(0, lastBackslash) + fs + "Fonts";

              break;
            }
          }
        }
      }

      Log.debug("Fonts located in \"" + fontPath + "\"");
      if (fontPath != null)
      {
        registerFontPath(fontPath, encoding);
      }
      registerFontPath(new File(jrepath, "lib" + fs + "fonts").toString(), encoding);
    }

    /**
     * Register all fonts (*.ttf files) in the given path.
     */
    public void registerFontPath(String path, String encoding)
    {
      File file = new File(path);
      if (file.exists() && file.isDirectory() && file.canRead())
      {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++)
        {
          registerFontFile(files[i].toString(), encoding);
        }
      }
      file = null;
      System.gc();
    }

    /**
     * Register the font (must end this *.ttf) to the FontFactory.
     */
    public void registerFontFile(String filename, String encoding)
    {
      if (!filename.toLowerCase().endsWith("ttf"))
      {
        return;
      }
      File file = new File(filename);
      if (file.exists() && file.isFile() && file.canRead())
      {
        try
        {
          addFont(filename, encoding);
          Log.debug("Registered truetype font " + filename);
        }
        catch (Exception e)
        {
          Log.warn("Font " + filename + " is invalid. Message:" + e.toString());
          e.printStackTrace();
        }
      }
    }

    /**
     * Adds the fontname by creating the basefont object
     */
    private void addFont(String font, String encoding)
        throws DocumentException, IOException
    {
      BaseFont bfont = BaseFont.createFont(font, encoding, true);
      String[][] fi = bfont.getFullFontName();
      for (int i = 0; i < fi.length; i++)
      {
        String[] ffi = fi[i];
        fontsByName.put(ffi[3], font);
      }
    }

    /**
     * Returns all registered fonts as enumeration.
     */
    public Enumeration getRegisteredFonts()
    {
      return fontsByName.keys();
    }

    /**
     * Returns the font by looking up the name in the Factory.
     */
    public String getFontfileForName(String font)
    {
      return (String) fontsByName.get(font);
    }


  }

  private static PDFFontFactory fontFactory;

  /**
   * returns/creates the singleton font factory.
   */
  public static PDFFontFactory getFontFactory()
  {
    if (fontFactory == null)
    {
      fontFactory = new PDFFontFactory();
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
  public BandCursor createCursor()
  {
    return new PDFBandCursor();
  }

  /**
   * Constructs a PDFOutputTarget.
   *
   * @param out The output stream.
   * @param pageFormat The page format.
   * @param embedFonts Embed fonts?
   */
  public PDFOutputTarget(OutputStream out, PageFormat pageFormat, boolean embedFonts)
  {

    super(pageFormat);
    this.out = out;
    this.embedFonts = embedFonts;
    this.baseFonts = new TreeMap();
    setFontEncoding(getDefaultFontEncoding());
  }

  public static final String getDefaultFontEncoding ()
  {
    return System.getProperty("com.jrefinery.report.targets.PDFOutputTarget.ENCODING", BaseFont.WINANSI);
  }

  /**
   * Returns the coordinate of the left edge of the page.
   */
  public float getPageX()
  {
    return this.document.getPageSize().left();
  }

  /**
   * Returns the coordinate of the top of the page.
   * <P>
   * NOTE: JFreeReport works with y values increasing as you move down the page, whereas iText
   * works with y values increasing as you move up the page - this method translates the
   * iText value to match JFreeReport.
   */
  public float getPageY()
  {
    return this.document.getPageSize().bottom();
  }

  /**
   * Returns the total width of the page.
   */
  public float getPageWidth()
  {
    return this.document.getPageSize().width();
  }

  /**
   * Returns the total height of the page.
   */
  public float getPageHeight()
  {
    return this.document.getPageSize().height();
  }

  /**
   * Returns the coordinate of the left hand edge of the usable area on the page (after taking
   * into account the left margin).
   */
  public float getUsableX()
  {
    return this.document.getPageSize().left() + this.document.leftMargin();
  }

  /**
   * Returns the coordinate of the top edge of the usable area on the page (after taking
   * into account the top margin and translating to JFreeReport's orientation).
   */
  public float getUsableY()
  {
    return this.document.getPageSize().bottom() + this.document.topMargin();
  }

  /**
   * Returns the width of the usable area on the page (after taking into account margins).
   */
  public float getUsableWidth()
  {
    return this.document.getPageSize().width()
        - this.document.leftMargin()
        - this.document.rightMargin();
  }

  /**
   * Returns the height of the usable area on the page (after taking into account margins).
   */
  public float getUsableHeight()
  {
    return this.document.getPageSize().height()
        - this.document.topMargin()
        - this.document.bottomMargin();
  }

  /**
   * Returns the currently active AWT-Font.
   */
  public Font getFont()
  {
    return awtFont;
  }

  /**
   * Returns the current fonts fontsize.
   */
  public int getFontSize()
  {
    return fontSize;
  }

  /**
   * Helper functions to query a strings start portion.
   *
   * @see String#startsWith
   */
  private static boolean startsWithIgnoreCase(String base, String start)
  {
    return base.regionMatches(true, 0, start, 0, start.length());
  }

  /**
   * Helper functions to query a strings end portion.
   *
   * @see String#endsWith
   */
  private boolean endsWithIgnoreCase(String base, String end)
  {
    if (base.length() < end.length())
      return false;
    return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
  }

  /**
   * Sets the current font. The font is mapped to pdf specific fonts if possible.
   * If no basefont could be created, an OutputTargetException is thrown.
   */
  public void setFont(Font font) throws OutputTargetException
  {
    if (font == null) throw new NullPointerException();

    // do nothing if this font is already set
    if (awtFont != null && awtFont.equals(font)) return;

    this.awtFont = font;
    this.fontSize = font.getSize();

    // use the Java logical font name to map to a predefined iText font.

    String fontKey = null;
    String logicalName = font.getName();
    String encoding = getFontEncoding();

    if (startsWithIgnoreCase(logicalName, "dialoginput") ||
        startsWithIgnoreCase(logicalName, "monospaced"))
    {
      boolean bold = false;
      boolean italic = false;

      if (endsWithIgnoreCase(logicalName, "bolditalic") ||
          (font.isBold() && font.isItalic()))
      {
        bold = true;
        italic = true;
      }
      else if (endsWithIgnoreCase(logicalName, "bold") ||
          (font.isBold()))
      {
        bold = true;
      }
      else if (endsWithIgnoreCase(logicalName, "italic") ||
          (font.isItalic()))
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
    else if (startsWithIgnoreCase(logicalName, "Serif"))
    {
      boolean bold = false;
      boolean italic = false;

      if (endsWithIgnoreCase(logicalName, "bolditalic") ||
          (font.isBold() && font.isItalic()))
      {
        bold = true;
        italic = true;
      }
      else if (endsWithIgnoreCase(logicalName, "bold") ||
          (font.isBold()))
      {
        bold = true;
      }
      else if (endsWithIgnoreCase(logicalName, "italic") ||
          (font.isItalic()))
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
    else if (startsWithIgnoreCase(logicalName, "SansSerif") ||
        startsWithIgnoreCase(logicalName, "Dialog"))
    { // default, this catches Dialog and SansSerif
      boolean bold = false;
      boolean italic = false;

      if (endsWithIgnoreCase(logicalName, "bolditalic") ||
          (font.isBold() && font.isItalic()))
      {
        bold = true;
        italic = true;
      }
      else if (endsWithIgnoreCase(logicalName, "bold") ||
          (font.isBold()))
      {
        bold = true;
      }
      else if (endsWithIgnoreCase(logicalName, "italic") ||
          (font.isItalic()))
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

    // iText uses some weird mapping between IDENTY-H/V and java supported encoding, IDENTITY-H/V is
    // used to recognize TrueType fonts, but the real JavaEncoding is used to encode Type1 fonts
    String stringEncoding = encoding;
    // Correct the encoding for truetype fonts
    if (encoding.equals(BaseFont.IDENTITY_H) || encoding.equals(BaseFont.IDENTITY_V))
    {
      stringEncoding = "iso-8859-1";
    }

    BaseFont f = (BaseFont) this.baseFonts.get(fontKey);
    if (f != null)
    {
      // the encoding does not match, reset ...
      if (f.getEncoding().equals(encoding) == false)
      {
        f = null;
      }
    }

    if (f == null)
    {
      try
      {
        String filename = getFontFactory().getFontfileForName(fontKey);
        if (filename != null)
        {
          if (font.isBold() && font.isItalic())
          {
            fontKey = filename + ",BoldItalic";
          }
          else if (font.isBold())
          {
            fontKey = filename + ",Bold";
          }
          else if (font.isItalic())
          {
            fontKey = filename + ",Italic";
          }
          else
          {
            fontKey = filename;
          }
        }
        // TrueType fonts need extra handling if the font is a symbolic font.
        if ((filename != null) &&
            (endsWithIgnoreCase(filename, ".ttf") || endsWithIgnoreCase(filename, ".ttc")))
        {
          try
          {
            f = BaseFont.createFont(fontKey, encoding, this.embedFonts);
          }
          catch (DocumentException de)
          {
            // Fallback to iso8859-1 encoding (!this is not IDENTITY-H)
            f = BaseFont.createFont(fontKey, stringEncoding, this.embedFonts);
          }
        }
        else
        {
          f = BaseFont.createFont(fontKey, stringEncoding, this.embedFonts);
        }
      }
      catch (Exception e)
      {
        Log.warn("BaseFont.createFont failed. Key = " + fontKey, e);
      }
      if (f == null)
      {
        // fallback .. use BaseFont.HELVETICA as default
        try
        {
          f = BaseFont.createFont(BaseFont.HELVETICA, stringEncoding, this.embedFonts);
        }
        catch (Exception e)
        {
          Log.warn("BaseFont.createFont for FALLBACK failed.", e);
        }
      }
    }

    if (f == null)
    {
      throw new OutputTargetException("Null font = " + fontKey);
    }
    else
    {
      this.baseFonts.put(fontKey, f);
    }

    this.baseFont = f;

  }

  /**
   * Draws an Image from this imageReference. The image is directly embedded into the
   * pdf file to provide the best scaling support.
   */
  public void drawImage(ImageReference imageRef) throws OutputTargetException
  {
    try
    {
      Rectangle2D bounds = getCursor().getDrawBounds();

      Image image = getImage(imageRef);
      image.setAbsolutePosition((float) bounds.getX(),
          (float) (getPageHeight() - bounds.getY() - bounds.getHeight()));
      image.scaleAbsolute((float) bounds.getWidth(), (float) bounds.getHeight());

      if (document.add(image) == false)
        throw new OutputTargetException("Unable to add the element");
    }
    catch (BadElementException be)
    {
      throw new OutputTargetException("BadElementException", be);
    }
    catch (DocumentException de)
    {
      throw new OutputTargetException("DocumentException", de);
    }
    catch (MalformedURLException mf)
    {
      throw new OutputTargetException("Invalid URL in ImageReference", mf);
    }
    catch (IOException mf)
    {
      throw new OutputTargetException("URL Content could not be read", mf);
    }
  }

  /**
   * Helperfunction to extract an image from an imagereference. If the image is contained as
   * java.awt.Image object only, the image is recoded into an PNG-Image.
   */
  private Image getImage(ImageReference imageRef) throws DocumentException, IOException
  {
    try
    {
      if (imageRef.getSourceURL() != null)
        return Image.getInstance(imageRef.getSourceURL());
    }
    catch (BadElementException be)
    {
      Log.debug("Caught illegal Image, will recode to PNG instead", be);
    }
    catch (IOException ioe)
    {
      Log.debug("Unable to read the raw-data, will try to recode image-data.", ioe);
    }

    if (imageRef.getImage() != null)
    {
      PngEncoder encoder = new PngEncoder(imageRef.getImage());
      byte[] data = encoder.pngEncode();
      return Image.getInstance(data);
    }

    throw new DocumentException("Neither an URL nor an Image was given to paint the graphics");
  }

  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is not filled.
   */
  public void drawShape(Shape shape)
  {
    Rectangle2D bounds = getCursor().getDrawBounds();
    float ycorr = (float) (getPageHeight() - bounds.getY());
    float xcorr = (float) bounds.getX();

    PathIterator pit = shape.getPathIterator(null);
    PdfContentByte cb = this.writer.getDirectContent();

    float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone() == false)
    {
      int cmd = pit.currentSegment(params);
      switch (cmd)
      {
        case PathIterator.SEG_MOVETO:
          {
            cb.moveTo(params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            cb.lineTo(params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            cb.curveTo(params[0] + xcorr, ycorr - params[1],
                params[2] + xcorr, ycorr - params[3],
                params[4] + xcorr, ycorr - params[5]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            cb.curveTo(params[0] + xcorr, ycorr - params[1],
                params[2] + xcorr, ycorr - params[3]);
            break;
          }
        case PathIterator.SEG_CLOSE:
          {
            cb.closePath();
          }
      }
      pit.next();
    }
    cb.stroke();
  }

  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is filled using
   * the current paint and no outline is drawn.
   */
  public void fillShape(Shape shape)
  {
    Rectangle2D bounds = getCursor().getDrawBounds();
    float ycorr = (float) (getPageHeight() - bounds.getY());
    float xcorr = (float) bounds.getX();

    PathIterator pit = shape.getPathIterator(null);
    PdfContentByte cb = this.writer.getDirectContent();
    int windingRule = pit.getWindingRule();

    float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone() == false)
    {
      int cmd = pit.currentSegment(params);
      switch (cmd)
      {
        case PathIterator.SEG_MOVETO:
          {
            cb.moveTo(params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            cb.lineTo(params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            cb.curveTo(params[0] + xcorr, ycorr - params[1],
                params[2] + xcorr, ycorr - params[3],
                params[4] + xcorr, ycorr - params[5]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            cb.curveTo(params[0] + xcorr, ycorr - params[1],
                params[2] + xcorr, ycorr - params[3]);
            break;
          }
        case PathIterator.SEG_CLOSE:
          {
            cb.closePath();
          }
      }
      pit.next();
    }
    if (windingRule == PathIterator.WIND_EVEN_ODD)
    {
      cb.eoFill();
    }
    else
    {
      cb.fill();
    }
  }

  /**
   * This method is called when the page is ended.
   */
  public void endPage() throws OutputTargetException
  {
    try
    {
      this.document.newPage();
    }
    catch (Exception e)
    {
      throw new OutputTargetException("Failed to end page", e);
    }
  }

  /**
   * Opens the document.  The report title and author are added to the PDF document properties.
   *
   * @param title The report title.
   * @param author The report author.
   */
  public void open() throws OutputTargetException
  {
    PageFormat pageFormat = getPageFormat();
    float urx = (float) pageFormat.getWidth();
    float ury = (float) pageFormat.getHeight();

    float marginLeft = (float) pageFormat.getImageableX();
    float marginRight =
        (float) (pageFormat.getWidth()
        - pageFormat.getImageableWidth()
        - pageFormat.getImageableX());
    float marginTop = (float) pageFormat.getImageableY();
    float marginBottom =
        (float) (pageFormat.getHeight()
        - pageFormat.getImageableHeight()
        - pageFormat.getImageableY());
    Rectangle pageSize = new Rectangle(urx, ury);

    try
    {
      if (pageFormat.getOrientation() != PageFormat.PORTRAIT)
      {
        pageSize.rotate();
      }

      this.document = new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom);

      String title = (String) getProperty(TITLE);
      String author = (String) getProperty(AUTHOR);

      if (title != null) document.addTitle(title);
      if (author != null) document.addAuthor(author);
      document.addCreator(CREATOR);
      document.addCreationDate();

      writer = PdfWriter.getInstance(document, out);

      Boolean encrypt = (Boolean) getProperty(SECURITY_ENCRYPTION);
      if (encrypt != null)
      {
        if (encrypt.equals(SECURITY_ENCRYPTION_128BIT) == false &&
            encrypt.equals(SECURITY_ENCRYPTION_40BIT) == false)
        {
          throw new OutputTargetException("Invalid Encryption entered");
        }
        String userpassword = (String) getProperty(SECURITY_USERPASSWORD);
        String ownerpassword = (String) getProperty(SECURITY_OWNERPASSWORD);
        //Log.debug ("UserPassword: " + userpassword + " - OwnerPassword: " + ownerpassword);
        byte[] userpasswordbytes = DocWriter.getISOBytes(userpassword);
        byte[] ownerpasswordbytes = DocWriter.getISOBytes(ownerpassword);
        if (ownerpasswordbytes == null)
        {
          ownerpasswordbytes = PDF_PASSWORD_PAD;
        }
        writer.setEncryption(userpasswordbytes, ownerpasswordbytes, getPermissions(), encrypt.booleanValue());
      }

      this.document.open();

      try
      {
        setPaint(Band.DEFAULT_PAINT);
        setStroke(ShapeElement.DEFAULT_STROKE);
        setFont(Band.DEFAULT_FONT);
      }
      catch (OutputTargetException oe)
      {
        Log.error("Should not happen", oe);
      }
    }
    catch (Exception e)
    {
      throw new OutputTargetException("Opening Document failed.", e);
    }

  }

  /**
   * Extracts the permissions for this PDF. The permissions are returned as flags in the integer value.
   * All permissions are defined as properties which have to be set before the target is opened.
   */
  protected int getPermissions()
  {
    Boolean allowPrinting = (Boolean) getProperty(SECURITY_ALLOW_PRINTING, Boolean.FALSE);
    Boolean allowModifyContents = (Boolean) getProperty(SECURITY_ALLOW_MODIFY_CONTENTS, Boolean.FALSE);
    Boolean allowModifyAnnotations = (Boolean) getProperty(SECURITY_ALLOW_MODIFY_ANNOTATIONS, Boolean.FALSE);
    Boolean allowCopy = (Boolean) getProperty(SECURITY_ALLOW_COPY, Boolean.FALSE);
    Boolean allowFillIn = (Boolean) getProperty(SECURITY_ALLOW_FILLIN, Boolean.FALSE);
    Boolean allowScreenReaders = (Boolean) getProperty(SECURITY_ALLOW_SCREENREADERS, Boolean.FALSE);
    Boolean allowAssembly = (Boolean) getProperty(SECURITY_ALLOW_ASSEMBLY, Boolean.FALSE);
    Boolean allowDegradedPrinting = (Boolean) getProperty(SECURITY_ALLOW_DEGRADED_PRINTING, Boolean.FALSE);


    int permissions = 0;
    if (allowPrinting.booleanValue()) permissions |= PdfWriter.AllowPrinting;
    if (allowModifyContents.booleanValue()) permissions |= PdfWriter.AllowModifyContents;
    if (allowModifyAnnotations.booleanValue()) permissions |= PdfWriter.AllowModifyAnnotations;
    if (allowCopy.booleanValue()) permissions |= PdfWriter.AllowCopy;
    if (allowFillIn.booleanValue()) permissions |= PdfWriter.AllowFillIn;
    if (allowScreenReaders.booleanValue()) permissions |= PdfWriter.AllowScreenReaders;
    if (allowAssembly.booleanValue()) permissions |= PdfWriter.AllowAssembly;
    if (allowDegradedPrinting.booleanValue()) permissions |= PdfWriter.AllowDegradedPrinting;
    return permissions;
  }

  /**
   * Closes the document.
   */
  public void close()
  {
    this.document.close();
  }

  /**
   * Draws the band onto the specified graphics device. The Text is printed on the
   * bottom of the elements bounds.
   *
   * @param mytext The text to be printed.
   * @param align The vertical alignment for the text.
   */
  public void drawString(
      String text,
      int alignment)
  {
    Rectangle2D bounds = getCursor().getDrawBounds();

    PdfContentByte cb = this.writer.getDirectContent();
    cb.beginText();
    cb.setFontAndSize(this.baseFont, this.fontSize);

    float y2 = (float) (bounds.getY() + bounds.getHeight());
    if (alignment == Element.LEFT)
    {
      cb.showTextAligned(
          PdfContentByte.ALIGN_LEFT,
          text,
          (float) bounds.getX(),
          this.getPageHeight() - y2,
          0);
    }
    else if (alignment == Element.CENTER)
    {
      cb.showTextAligned(
          PdfContentByte.ALIGN_CENTER,
          text,
          (float) (bounds.getX() + (bounds.getWidth() / 2)),
          this.getPageHeight() - y2,
          0);
    }
    else if (alignment == Element.RIGHT)
    {
      cb.showTextAligned(
          PdfContentByte.ALIGN_RIGHT,
          text,
          (float) (bounds.getX() + bounds.getWidth()),
          this.getPageHeight() - y2,
          0);
    }
    cb.endText();
  }

  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param text the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   * @returns the width of the given string in 1/72" dpi.
   */
  protected float getStringBounds(String text, int lineStartPos, int endPos)
  {
    return (float) baseFont.getWidthPoint(text.substring(lineStartPos, endPos), getFontSize());
  }

  /**
   * Returns the height of the current font. The font height specifies the distance between
   * 2 base lines.
   */
  protected float getFontHeight()
  {
    return getFontSize();
  }

  /**
   * Defines the stroke used to draw shapes. If the stroke is of an invalid type, an
   * OutputTargetException is thrown. Currently only BasicStroke is supported.
   *
   * @throws OutputTargetException
   */
  public void setStroke(Stroke stroke) throws OutputTargetException
  {
    if (stroke == null) throw new NullPointerException();
    if (stroke instanceof BasicStroke == false) throw new OutputTargetException("Unable to handle this stroke type");

    // If this stroke is already set, do nothing
    if (awtStroke != null && awtStroke.equals(stroke)) return;

    this.awtStroke = stroke;
    BasicStroke bstroke = (BasicStroke) stroke;
    PdfContentByte cb = this.writer.getDirectContent();
    cb.setLineWidth(bstroke.getLineWidth());
  }

  /**
   * Returns the currently set stroke.
   */
  public Stroke getStroke()
  {
    return awtStroke;
  }

  /**
   * Sets the paint. If the paint could not be converted into a pdf object, an OutputTargetException
   * is thrown. This implementation currently supports java.awt.Color as the only valid paint.
   *
   * @throws OutputTargetException if the paint is invalid.
   */
  public void setPaint(Paint paint) throws OutputTargetException
  {
    if (paint == null) throw new NullPointerException();

    if (paint instanceof Color == false)
    {
      throw new OutputTargetException("Unsupported paint type, currently only color is supported");
    }

    // If this paint is already set, do nothing
    if (awtPaint != null && awtPaint.equals(paint)) return;

    this.awtPaint = paint;
    PdfContentByte cb = this.writer.getDirectContent();
    PdfPatternPainter painter = null;

    cb.setColorStroke((Color) paint);
    cb.setColorFill((Color) paint);
  }

  /**
   * Returns the currently set paint.
   */
  public Paint getPaint()
  {
    return awtPaint;
  }

  /**
   * Defines the current clipping are for the band to be drawn. This method is called by
   * the band and should not be called by other entities.
   */
  public void setClippingArea(Rectangle2D bounds)
  {
    super.setClippingArea(bounds);
  }

  /**
   * Restores the state of this target.
   *
   * @throws OutputTargetException if the given state object is not valid.
   */
  public void restoreState(Object o) throws OutputTargetException
  {
    if (o instanceof PDFState == false) throw new OutputTargetException("Need a pdf state");
    PDFState state = (PDFState) o;
    state.restore(this);
  }

  /**
   * Saves the state of this graphics object. Use restoreState to restore a previously saved
   * state.
   *
   * @returns the state container.
   */
  public Object saveState() throws OutputTargetException
  {
    return new PDFState(this);
  }

  /**
   * returns the font encoding used in this output target.
   */
  public String getFontEncoding()
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
  public void setFontEncoding(String encoding)
  {
    if (encoding == null) throw new NullPointerException();
    this.encoding = encoding;
  }
}
