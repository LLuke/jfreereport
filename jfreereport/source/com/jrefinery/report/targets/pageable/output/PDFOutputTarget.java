/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: PDFOutputTarget.java,v 1.15 2003/01/29 21:57:12 taqua Exp $
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
 * 13-Sep-2002 : Removed caching of fonts for FontFactory as it causes OutOfMemoryErrors when a huge
 *               font collection is used
 * 04-Nov-2002 : BugFix: PDFFonts need caching on setFont() or OutOfMemoryErrors occur
 */

package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.targets.pageable.physicals.PhysicalPage;
import com.jrefinery.report.targets.pageable.physicals.LogicalPageImpl;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.LogicalPage;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.SizeCalculator;
import com.jrefinery.report.targets.style.ElementDefaultStyleSheet;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.NullOutputStream;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.StringUtil;
import com.keypoint.PngEncoder;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
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
import java.util.StringTokenizer;

/**
 * An output target for the report engine that generates a PDF file using the iText class library
 * (see <code>http://www.lowagie.com/iText</code>, note that the URL is case-sensitive!).
 * <p>
 * If the system property "com.jrefinery.report.targets.pageable.output.PDFOutputTarget.AUTOINIT"
 * is set to "true",
 * the PDF-FontFactory is automatically initialized when this class is loaded. Be aware that
 * embedding many fonts will result in larger files.
 * <p>
 * When using Unicode characters, you will have to adjust the encoding of this target to
 * "Identity-H", to enable horizontal unicode printing. This will result in larger files.
 * <p>
 * The Encoding property is now a string with one of the values of "none" "40bit" or "128bit".
 *
 * @author David Gilbert
 */
public class PDFOutputTarget extends AbstractOutputTarget
{
  /** The configuration prefix. */
  public static final String CONFIGURATION_PREFIX
      = "com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.";

  /** Literal text for the 'EmbedFonts' property name. */
  public static final String EMBED_FONTS = "EmbedFonts";

  /** Literal text for the 'AllowPrinting' property name. */
  public static final String SECURITY_ALLOW_PRINTING = "AllowPrinting";

  /** Literal text for the 'AllowModifyContents' property name. */
  public static final String SECURITY_ALLOW_MODIFY_CONTENTS = "AllowModifyContents";

  /** Literal text for the 'AllowCopy' property name. */
  public static final String SECURITY_ALLOW_COPY = "AllowCopy";

  /** Literal text for the 'AllowModifyAnnotations' property name. */
  public static final String SECURITY_ALLOW_MODIFY_ANNOTATIONS = "AllowModifyAnnotations";

  /** Literal text for the 'AllowFillIn' property name. */
  public static final String SECURITY_ALLOW_FILLIN = "AllowFillIn";

  /** Literal text for the 'AllowScreenReaders' property name. */
  public static final String SECURITY_ALLOW_SCREENREADERS = "AllowScreenReaders";

  /** Literal text for the 'AllowAssembly' property name. */
  public static final String SECURITY_ALLOW_ASSEMBLY = "AllowAssembly";

  /** Literal text for the 'AllowDegradedPrinting' property name. */
  public static final String SECURITY_ALLOW_DEGRADED_PRINTING = "AllowDegradedPrinting";

  /** Literal text for the 'Encryption' property name. */
  public static final String SECURITY_ENCRYPTION = "Encryption";

  /** A constant for the encryption type (40 bit). */
  public static final String SECURITY_ENCRYPTION_NONE = "none";

  /** A constant for the encryption type (40 bit). */
  public static final String SECURITY_ENCRYPTION_40BIT = "40bit";

  /** A constant for the encryption type (128 bit). */
  public static final String SECURITY_ENCRYPTION_128BIT = "128bit";

  /** Literal text for the 'userpassword' property name. */
  public static final String SECURITY_USERPASSWORD = "userpassword";

  /** Literal text for the 'ownerpassword' property name. */
  public static final String SECURITY_OWNERPASSWORD = "ownerpassword";

  /** A useful constant for specifying the PDF creator. */
  private static final String CREATOR = JFreeReport.getInfo().getName() + " version "
      + JFreeReport.getInfo().getVersion();

  /** The encoding key. */
  public static final String ENCODING = "Encoding";

  /** The output stream. */
  private OutputStream out;

  /** The document. */
  private Document pdfDocument;

  /** The document writer. */
  private PdfWriter writer;

  /** The current base font. */
  private BaseFont baseFont;

  /** The AWT font. */
  private FontDefinition fontDefinition;

  /** The stroke used for shapes */
  private Stroke awtStroke;

  /** The current Paint as used in the AWT */
  private Paint awtPaint;

  /** The PDF font support. */
  private PDFFontSupport fontSupport;

  /**
   * A bytearray containing an empty password. iText replaces the owner password with random
   * values, but Adobe allows to have encryption without an owner password set.
   * Copied from iText
   */
  private static final byte PDF_PASSWORD_PAD[] = {
    (byte) 0x28, (byte) 0xBF, (byte) 0x4E, (byte) 0x5E, (byte) 0x4E, (byte) 0x75,
    (byte) 0x8A, (byte) 0x41, (byte) 0x64, (byte) 0x00, (byte) 0x4E, (byte) 0x56,
    (byte) 0xFF, (byte) 0xFA, (byte) 0x01, (byte) 0x08, (byte) 0x2E, (byte) 0x2E,
    (byte) 0x00, (byte) 0xB6, (byte) 0xD0, (byte) 0x68, (byte) 0x3E, (byte) 0x80,
    (byte) 0x2F, (byte) 0x0C, (byte) 0xA9, (byte) 0xFE, (byte) 0x64, (byte) 0x53,
    (byte) 0x69, (byte) 0x7A};

  /**
   * The pdf state is used to store and restore the current state of this output target.
   */
  private static class PDFState
  {
    /** The font. */
    private FontDefinition myfont;

    /** The stroke. */
    private Stroke mystroke;

    /** The paint. */
    private Paint mypaint;

    /**
     * Creates a new state.
     *
     * @param source  the source of the state information to save.
     */
    public PDFState(PDFOutputTarget source)
    {
      save(source);
    }

    /**
     * Saves the state of the specified target.
     *
     * @param source  the source of the state information.
     */
    public void save(PDFOutputTarget source)
    {
      this.myfont = source.getFont();
      this.mystroke = source.getStroke();
      this.mypaint = source.getPaint();
    }

    /**
     * Restores the state of the specified target.
     *
     * @param target  the target for the state information
     *
     * @throws OutputTargetException if there is a problem with the output target.
     */
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
    /** Fonts stored by name. */
    private Hashtable fontsByName;

    /**
     * Creates a new factory.
     */
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
        // is this correct?
        encoding = "iso-8859-1";
      }

      String osname = System.getProperty("os.name");
      String jrepath = System.getProperty("java.home");
      String fontPath = null;
      String fs = System.getProperty("file.separator");

      Log.debug("Running on operating system: " + osname);
      Log.debug("Character encoding used as default: " + encoding);

      if (!StringUtil.startsWithIgnoreCase(osname, "windows"))
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
          StringTokenizer strtok
              = new StringTokenizer(windirs, System.getProperty("path.separator"));
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
     *
     * @param path  the path.
     * @param encoding  the encoding.
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
     *
     * @param filename  the filename.
     * @param encoding  the encoding.
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
        }
        catch (Exception e)
        {
          Log.warn("Font " + filename + " is invalid. Message:", e);
        }
      }
    }

    /**
     * Adds the fontname by creating the basefont object
     *
     * @param font  the font name.
     * @param encoding  the encoding.
     *
     * @throws DocumentException ??
     * @throws IOException ??
     */
    private void addFont(String font, String encoding)
        throws DocumentException, IOException
    {
      if (fontsByName.containsValue(font))
      {
        return; // already in there
      }
      BaseFont bfont = BaseFont.createFont(font, encoding, true, false, null, null);
      String[][] fi = bfont.getFullFontName();
      for (int i = 0; i < fi.length; i++)
      {
        String[] ffi = fi[i];
        if (fontsByName.containsKey(ffi[3]) == false)
        {
          fontsByName.put(ffi[3], font);
          Log.debug("Registered truetype font " + ffi[3] + "; File=" + font);
        }
      }
    }

    /**
     * Returns all registered fonts as enumeration.
     *
     * @return an enumeration of the registered fonts.
     */
    public Enumeration getRegisteredFonts()
    {
      return fontsByName.keys();
    }

    /**
     * Returns the name of the font file by looking up the name.
     *
     * @param font  the font name
     *
     * @return the font file name.
     */
    public String getFontfileForName(String font)
    {
      return (String) fontsByName.get(font);
    }
  }

  /** The font factory. */
  private static PDFFontFactory fontFactory;

  /**
   * Returns/creates the singleton font factory.
   *
   * @return the font factory.
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
   * of  <code>"com.jrefinery.report.targets.pageable.output.PDFOutputTarget.AutoInit"</code> is
   * set to <code>true</code>.
   */
  static
  {
    if (ReportConfiguration.getGlobalConfig().isPDFTargetAutoInit())
    {
      getFontFactory().registerDefaultFontPath();
    }
  }

  /**
   * Constructs a PDFOutputTarget.
   *
   * @param out  the output stream.
   * @param pageFormat  the page format.
   * @param embedFonts  embed fonts?
   */
  public PDFOutputTarget(OutputStream out, PageFormat pageFormat, boolean embedFonts)
  {
    this(out, pageFormat, pageFormat, embedFonts);
  }

  /**
   * Creates a new PDFOutputTarget.
   *
   * @param out  the output stream.
   * @param logPage  the logical page.
   * @param embedFonts  embed the fonts?
   */
  public PDFOutputTarget(OutputStream out, LogicalPage logPage, boolean embedFonts)
  {
    super(logPage);
    this.out = out;
    this.fontSupport = new PDFFontSupport();
    setEmbedFonts(embedFonts);
  }

  /**
   * Creates a new PDFOutputTarget.
   *
   * @param out  the output stream.
   * @param logPageFormat  the logical page format.
   * @param physPageFormat  the physical page format.
   * @param embedFonts  embed the fonts?
   */
  public PDFOutputTarget(OutputStream out, PageFormat logPageFormat, PageFormat physPageFormat,
                         boolean embedFonts)
  {
    this(out, new LogicalPageImpl(logPageFormat, physPageFormat), embedFonts);
  }

  /**
   * Returns the default font encoding.
   *
   * @return the default font encoding.
   */
  public static final String getDefaultFontEncoding()
  {
    return ReportConfiguration.getGlobalConfig().getPdfTargetEncoding();
  }

  /**
   * Returns the page height in points (1/72 inch).
   *
   * @return the page height.
   */
  private float getPageHeight()
  {
    return this.getDocument().getPageSize().height();
  }

  /**
   * Returns the currently active AWT-Font.
   *
   * @return the current font.
   */
  public FontDefinition getFont()
  {
    return fontDefinition;
  }

  /**
   * Returns the iText BaseFont.
   *
   * @return the iText BaseFont.
   */
  public BaseFont getBaseFont ()
  {
    return baseFont;
  }

  /**
   * Sets the current font. The font is mapped to pdf specific fonts if possible.
   * If no basefont could be created, an OutputTargetException is thrown.
   *
   * @param font  the new font (null not permitted).
   *
   * @throws OutputTargetException if there was a problem setting the font for the target.
   */
  public void setFont(FontDefinition font) throws OutputTargetException
  {
    if (font == null)
    {
      throw new NullPointerException();
    }

    // do nothing if this font is already set
    if (baseFont != null && fontDefinition != null && fontDefinition.equals(font))
    {
      Log.debug ("The Fonts are equal, request ignored.");
      return; // no need to do anything ...
    }
    this.fontDefinition = font;
    this.baseFont = fontSupport.createBaseFont(font,
                                               font.getFontEncoding(getFontEncoding()),
                                               (isEmbedFonts() || font.isEmbeddedFont())).getBaseFont();
    if (baseFont == null)
    {
      throw new OutputTargetException("The font definition was not successfull.");
    }
  }

  /**
   * Draws an Image from this imageReference. The image is directly embedded into the
   * pdf file to provide the best scaling support.
   *
   * @param imageRef  the image reference.
   *
   * @throws OutputTargetException if there was a problem drawing the image to the target.
   */
  public void drawImage(ImageReference imageRef) throws OutputTargetException
  {
    try
    {
      Rectangle2D bounds = getInternalOperationBounds();
      Rectangle2D imageBounds = imageRef.getBoundsScaled();

      float x = (float) (bounds.getX());
      float y = (float) (bounds.getY());

      Image image = getImage(imageRef);
      image.setAbsolutePosition(x, (float) (getPageHeight() - y - bounds.getHeight()));
      image.scaleAbsolute((float) imageBounds.getWidth(),
                          (float) imageBounds.getHeight());

      PdfContentByte cb = this.writer.getDirectContent();

      cb.rectangle((float) (imageBounds.getX() + x),
                   (float) (getPageHeight() - imageBounds.getY()  - y - bounds.getHeight()),
                   (float) imageBounds.getWidth(),
                   (float) imageBounds.getHeight());
      cb.clip();
      cb.newPath();
      cb.addImage(image);
      cb.restoreState();
      cb.saveState();

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
   *
   * @param imageRef  the image reference.
   *
   * @return an image.
   *
   * @throws DocumentException ??
   * @throws IOException ??
   */
  private Image getImage(ImageReference imageRef) throws DocumentException, IOException
  {
    Rectangle2D bounds = getInternalOperationBounds();
    Rectangle2D imageBounds = imageRef.getBoundsScaled();

    try
    {
      Rectangle2D drawArea = new Rectangle2D.Double (0, 0, bounds.getWidth(), bounds.getHeight());
      if ((imageRef.getSourceURL() != null) && (drawArea.contains(imageBounds)))
      {
        return Image.getInstance(imageRef.getSourceURL());
      }
    }
    catch (BadElementException be)
    {
      Log.info("Caught illegal Image, will recode to PNG instead", be);
    }
    catch (IOException ioe)
    {
      Log.info("Unable to read the raw-data, will try to recode image-data.", ioe);
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
   * Returns the corrected Y value.
   *
   * @param y  the y value.
   *
   * @return the corrected value.
   */
  private float getCorrectedY(float y)
  {
    return getPageHeight() - y;
  }

  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is not filled.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape(Shape shape)
  {
    Rectangle2D bounds = getInternalOperationBounds();

    float ycorr = (float) bounds.getY();
    float xcorr = (float) bounds.getX();

    PathIterator pit = shape.getPathIterator(null);
    PdfContentByte cb = this.writer.getDirectContent();

    cb.newPath();
    cb.moveTo(xcorr, getCorrectedY(ycorr));

    float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone() == false)
    {
      int cmd = pit.currentSegment(params);
      params[1] = getCorrectedY(params[1] + ycorr);
      params[3] = getCorrectedY(params[3] + ycorr);
      params[5] = getCorrectedY(params[5] + ycorr);

      params[0] = params[0] + xcorr;
      params[2] = params[2] + xcorr;
      params[4] = params[4] + xcorr;

      switch (cmd)
      {
        case PathIterator.SEG_MOVETO:
          {
            cb.moveTo(params[0], params[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            cb.lineTo(params[0], params[1]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            cb.curveTo(params[0], params[1],
                       params[2], params[3],
                       params[4], params[5]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            cb.curveTo(params[0], params[1],
                       params[2], params[3]);
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
   *
   * @param shape  the shape to fill.
   */
  public void fillShape(Shape shape)
  {
    Rectangle2D bounds = getInternalOperationBounds();

    float ycorr = (float) bounds.getY();
    float xcorr = (float) bounds.getX();

    PathIterator pit = shape.getPathIterator(null);
    PdfContentByte cb = this.writer.getDirectContent();

    cb.newPath();
    cb.moveTo(xcorr, getCorrectedY(ycorr));

    int windingRule = pit.getWindingRule();

    float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone() == false)
    {
      int cmd = pit.currentSegment(params);
      params[1] = getCorrectedY(params[1] + ycorr);
      params[3] = getCorrectedY(params[3] + ycorr);
      params[5] = getCorrectedY(params[5] + ycorr);

      params[0] = params[0] + xcorr;
      params[2] = params[2] + xcorr;
      params[4] = params[4] + xcorr;

      switch (cmd)
      {
        case PathIterator.SEG_MOVETO:
          {
            cb.moveTo(params[0], params[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            cb.lineTo(params[0], params[1]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            cb.curveTo(params[0], params[1],
                       params[2], params[3],
                       params[4], params[5]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            cb.curveTo(params[0], params[1],
                       params[2], params[3]);
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
   *
   * @throws OutputTargetException if there was a problem with the target.
   */
  public void endPage() throws OutputTargetException
  {
    try
    {
      this.writer.getDirectContent().restoreState();
      this.getDocument().newPage();
    }
    catch (Exception e)
    {
      throw new OutputTargetException("Failed to end page", e);
    }
  }

  /**
   * Opens the document.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void open() throws OutputTargetException
  {
    Log.debug ("Opening PDFTarget: Encoding: " + getFontEncoding() + " DefaultEncoding: " + getDefaultFontEncoding());

    PageFormat pageFormat = getLogicalPage().getPhysicalPageFormat();
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

      setDocument(new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom));

      writer = PdfWriter.getInstance(getDocument(), out);

      String encrypt = (String) getProperty(SECURITY_ENCRYPTION);

      if (encrypt != null)
      {
        if (encrypt.equals(SECURITY_ENCRYPTION_128BIT) == true
            || encrypt.equals(SECURITY_ENCRYPTION_40BIT) == true)
        {
          String userpassword = (String) getProperty(SECURITY_USERPASSWORD);
          String ownerpassword = (String) getProperty(SECURITY_OWNERPASSWORD);
          //Log.debug ("UserPassword: " + userpassword + " - OwnerPassword: " + ownerpassword);
          byte[] userpasswordbytes = DocWriter.getISOBytes(userpassword);
          byte[] ownerpasswordbytes = DocWriter.getISOBytes(ownerpassword);
          if (ownerpasswordbytes == null)
          {
            ownerpasswordbytes = PDF_PASSWORD_PAD;
          }
          writer.setEncryption(userpasswordbytes, ownerpasswordbytes, getPermissions(),
                               encrypt.equals(SECURITY_ENCRYPTION_128BIT));
        }
      }

      /**
       * MetaData can be set when the writer is registered to the document.
       */
      String title = (String) getProperty(TITLE);
      String author = (String) getProperty(AUTHOR);

      if (title != null)
      {
        getDocument().addTitle(title);
      }
      if (author != null)
      {
        getDocument().addAuthor(author);
      }
      getDocument().addCreator(CREATOR);
      getDocument().addCreationDate();

      getDocument().open();

      try
      {
        setPaint(ElementDefaultStyleSheet.DEFAULT_PAINT);
        setStroke(ShapeElement.DEFAULT_STROKE);
        setFont(ElementDefaultStyleSheet.DEFAULT_FONT_DEFINITION);
      }
      catch (OutputTargetException oe)
      {
        Log.error("Should not happen", oe);
      }
      writer.getDirectContent().saveState();
    }
    catch (Exception e)
    {
      throw new OutputTargetException("Opening Document failed.", e);
    }
  }


  /**
   * Signals that a page is being started. Stores the state of the target to
   * make it possible to restore the complete outputtarget.
   *
   * @param format  the physical page.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void beginPage(PhysicalPage format) throws OutputTargetException
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Target " + hashCode() + " is not open");
    }
    this.writer.getDirectContent().saveState();
    this.currentPageFormat = format.getPageFormat();
  }

  /**
   * Extracts the permissions for this PDF. The permissions are returned as flags in the integer
   * value. All permissions are defined as properties which have to be set before the target is
   * opened.
   *
   * @return the permissions.
   */
  protected int getPermissions()
  {
    Boolean allowPrinting = (Boolean) getProperty(SECURITY_ALLOW_PRINTING, Boolean.FALSE);
    Boolean allowModifyContents =
        (Boolean) getProperty(SECURITY_ALLOW_MODIFY_CONTENTS, Boolean.FALSE);
    Boolean allowModifyAnnotations =
        (Boolean) getProperty(SECURITY_ALLOW_MODIFY_ANNOTATIONS, Boolean.FALSE);
    Boolean allowCopy = (Boolean) getProperty(SECURITY_ALLOW_COPY, Boolean.FALSE);
    Boolean allowFillIn = (Boolean) getProperty(SECURITY_ALLOW_FILLIN, Boolean.FALSE);
    Boolean allowScreenReaders =
        (Boolean) getProperty(SECURITY_ALLOW_SCREENREADERS, Boolean.FALSE);
    Boolean allowAssembly = (Boolean) getProperty(SECURITY_ALLOW_ASSEMBLY, Boolean.FALSE);
    Boolean allowDegradedPrinting =
        (Boolean) getProperty(SECURITY_ALLOW_DEGRADED_PRINTING, Boolean.FALSE);

    int permissions = 0;
    if (allowPrinting.booleanValue())
    {
      permissions |= PdfWriter.AllowPrinting;
    }
    if (allowModifyContents.booleanValue())
    {
      permissions |= PdfWriter.AllowModifyContents;
    }
    if (allowModifyAnnotations.booleanValue())
    {
      permissions |= PdfWriter.AllowModifyAnnotations;
    }
    if (allowCopy.booleanValue())
    {
      permissions |= PdfWriter.AllowCopy;
    }
    if (allowFillIn.booleanValue())
    {
      permissions |= PdfWriter.AllowFillIn;
    }
    if (allowScreenReaders.booleanValue())
    {
      permissions |= PdfWriter.AllowScreenReaders;
    }
    if (allowAssembly.booleanValue())
    {
      permissions |= PdfWriter.AllowAssembly;
    }
    if (allowDegradedPrinting.booleanValue())
    {
      permissions |= PdfWriter.AllowDegradedPrinting;
    }
    return permissions;
  }

  /**
   * Closes the document.
   */
  public void close()
  {
    this.getDocument().close();
    this.fontSupport.close();
    this.pdfDocument = null;
    this.writer = null;
  }

  /**
   * Draws the band onto the specified graphics device. The Text is printed on the
   * bottom of the elements bounds.
   *
   * @param text The text to be printed.
   */
  public void drawString(String text)
  {
    Rectangle2D bounds = getInternalOperationBounds();
    int fontSize = getFont().getFontSize();

    PdfContentByte cb = this.writer.getDirectContent();
    cb.beginText();
    cb.setFontAndSize(this.baseFont, fontSize);

    float y2 = (float) (bounds.getY() + baseFont.getFontDescriptor(BaseFont.ASCENT, fontSize));
      cb.showTextAligned(
          PdfContentByte.ALIGN_LEFT,
          text,
          (float) bounds.getX(),
          this.getPageHeight() - y2,
          0);
    cb.endText();
  }

  /**
   * Defines the stroke used to draw shapes. If the stroke is of an invalid type, an
   * OutputTargetException is thrown. Currently only BasicStroke is supported.
   *
   * @param stroke  the stroke.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void setStroke(Stroke stroke) throws OutputTargetException
  {
    if (stroke == null)
    {
      throw new NullPointerException();
    }
    if (stroke instanceof BasicStroke == false)
    {
      throw new OutputTargetException("Unable to handle this stroke type");
    }

    // If this stroke is already set, do nothing
    if (awtStroke != null && awtStroke.equals(stroke))
    {
      return;
    }

    this.awtStroke = stroke;
    BasicStroke bstroke = (BasicStroke) stroke;
    PdfContentByte cb = this.writer.getDirectContent();
    cb.setLineWidth(bstroke.getLineWidth());
  }

  /**
   * Returns the current stroke.
   *
   * @return the current stroke.
   */
  public Stroke getStroke()
  {
    return awtStroke;
  }

  /**
   * Sets the paint. If the paint could not be converted into a pdf object, an OutputTargetException
   * is thrown. This implementation currently supports java.awt.Color as the only valid paint.
   *
   * @param paint  the paint.
   *
   * @throws OutputTargetException if the paint is invalid.
   */
  public void setPaint(Paint paint) throws OutputTargetException
  {
    if (paint == null)
    {
      throw new NullPointerException();
    }

    if (paint instanceof Color == false)
    {
      throw new OutputTargetException("Unsupported paint type, currently only color is supported");
    }

    // If this paint is already set, do nothing
    if (awtPaint != null && awtPaint.equals(paint))
    {
      return;
    }

    this.awtPaint = paint;
    PdfContentByte cb = this.writer.getDirectContent();

    cb.setColorStroke((Color) paint);
    cb.setColorFill((Color) paint);
  }

  /**
   * Returns the currently set paint.
   *
   * @return the paint.
   */
  public Paint getPaint()
  {
    return awtPaint;
  }

  /**
   * Restores the state of this target.
   *
   * @throws OutputTargetException if the given state object is not valid.
   */
  public void restoreState() throws OutputTargetException
  {
    //lastState.restore(this);
  }

  /**
   * Saves the state of this graphics object. Use restoreState to restore a previously saved
   * state.
   *
   * @return the state container.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public Object saveState() throws OutputTargetException
  {
    return new PDFState(this);
  }

  /**
   * returns the font encoding used in this output target.
   *
   * @return the font encoding.
   */
  public String getFontEncoding()
  {
    return (String) getProperty(ENCODING, getDefaultFontEncoding());
  }

  /**
   * Defines the text encoding used in this output target.
   *
   * <ul>
   * <li>The Unicode encoding with horizontal writing is "Identity-H"
   * <li>The Unicode encoding with vertical writing is "Identity-V"
   * <li>"Cp1250"
   * <li>"Cp1252" is also known as WinAnsi
   * <li>"Cp1257"
   * <li>"MacRoman"
   * </ul>
   *
   * @param encoding  the font encoding.
   */
  public void setFontEncoding(String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    setProperty(ENCODING, encoding);
  }

  /**
   * Returns the 'embed fonts' flag.
   *
   * @return the 'embed fonts' flag.
   */
  protected boolean isEmbedFonts()
  {
    return getProperty(EMBED_FONTS, "false").equals("true");
  }

  /**
   * Sets the 'embed fonts' flag.
   *
   * @param embedFonts  the new flag value.
   */
  protected void setEmbedFonts(boolean embedFonts)
  {
    setProperty(EMBED_FONTS, String.valueOf(embedFonts));
  }

  /**
   * Creates an output target that mimics a real output target, but produces no output.
   * This is used by the reporting engine when it makes its first pass through the report,
   * calculating page boundaries etc.  The second pass will use a real output target.
   *
   * @return a dummy output target.
   */
  public OutputTarget createDummyWriter()
  {
    /*
    PDFOutputTarget dummy = new PDFOutputTarget(new NullOutputStream(),
                                                getLogicalPage(), isEmbedFonts());
    Enumeration enum = getPropertyNames();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      dummy.setProperty(key, getProperty(key));
    }
    */
    return new DummyOutputTarget(this);
  }

  /**
   * Returns the document.
   *
   * @return the document.
   */
  protected Document getDocument()
  {
    return pdfDocument;
  }

  /**
   * Sets the document.
   *
   * @param document  the document (null not permitted).
   */
  protected void setDocument(Document document)
  {
    if (document == null)
    {
      throw new NullPointerException();
    }
    this.pdfDocument = document;
  }

  /**
   * Configures the output target.
   *
   * @param config  the configuration.
   */
  public void configure(ReportConfiguration config)
  {
    updateProperty(SECURITY_OWNERPASSWORD, config);
    updateProperty(SECURITY_USERPASSWORD, config);
    updateProperty(AUTHOR, config);
    updateProperty(ENCODING, config);
    updateBooleanProperty(EMBED_FONTS, config);
    updateBooleanProperty(SECURITY_ALLOW_ASSEMBLY, config);
    updateBooleanProperty(SECURITY_ALLOW_COPY, config);
    updateBooleanProperty(SECURITY_ALLOW_DEGRADED_PRINTING, config);
    updateBooleanProperty(SECURITY_ALLOW_FILLIN, config);
    updateBooleanProperty(SECURITY_ALLOW_MODIFY_ANNOTATIONS, config);
    updateBooleanProperty(SECURITY_ALLOW_MODIFY_CONTENTS, config);
    updateBooleanProperty(SECURITY_ALLOW_PRINTING, config);
    updateBooleanProperty(SECURITY_ALLOW_SCREENREADERS, config);
    updateBooleanProperty(SECURITY_OWNERPASSWORD, config);
    updateBooleanProperty(SECURITY_USERPASSWORD, config);

    // encryption needs more info: <undefined> <none> <40> <128>.
  }

  /**
   * Updates a property.
   *
   * @param key  the key.
   * @param config  the config.
   */
  private void updateProperty(String key, ReportConfiguration config)
  {
    String configValue = config.getConfigProperty(CONFIGURATION_PREFIX + key);
    String propertyValue = (String) getProperty(key, configValue);
    setProperty(key, propertyValue);
  }

  /**
   * Updates a boolean property.
   *
   * @param key  the key.
   * @param config  the config.
   */
  private void updateBooleanProperty(String key, ReportConfiguration config)
  {
    String value = config.getConfigProperty(key, "");
    Boolean bValue = Boolean.FALSE;
    if (value.equalsIgnoreCase("true"))
    {
      bValue = Boolean.TRUE;
    }
    setProperty(key, getProperty(key, bValue));
  }

  /**
   * Returns true if the output target is open, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isOpen()
  {
    if (getDocument() == null)
    {
      //Log.debug ("Document is null, assuming that the document is closed ...");
      return false;
    }
    return getDocument().isOpen();
  }

  /**
   * A PDF size calculator.
   */
  private static class PDFSizeCalculator implements SizeCalculator
  {
    /** The base font. */
    private BaseFont baseFont;

    /** The font size. */
    private float fontSize;

    /**
     * Creates a new size calculator.
     *
     * @param font  the font.
     * @param fontSize  the font size.
     */
    public PDFSizeCalculator(BaseFont font, float fontSize)
    {
      this.baseFont = font;
      this.fontSize = fontSize;
    }

    /**
     * Calculates the width of the specified String in the current Graphics context.
     *
     * @param text the text to be weighted.
     * @param lineStartPos the start position of the substring to be weighted.
     * @param endPos the position of the last characterto be included in the weightening process.
     *
     * @return the width of the given string in 1/72" dpi.
     */
    public float getStringWidth(String text, int lineStartPos, int endPos)
    {
      return baseFont.getWidthPoint(text.substring(lineStartPos, endPos), fontSize);
    }

    /**
     * Returns the height of the current font. The font height specifies the distance between
     * 2 base lines.
     *
     * @return  the font height.
     */
    public float getLineHeight()
    {
      return fontSize;
    }
  }

  /** The current page format. */
  private PageFormat currentPageFormat;

  /** The internal operation bounds. */
  private Rectangle2D internalOperationBounds;

  /**
   * Creates a 'size calculator' for the current state of the output target. The calculator
   * is used to calculate the string width and line height and later maybe more.
   *
   * @param font  the font.
   *
   * @return the size calculator.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(FontDefinition font) throws OutputTargetException
  {
    PDFFontRecord record = fontSupport.createBaseFont(font,
                                                      font.getFontEncoding(getFontEncoding()),
                                                      false);
    return new PDFSizeCalculator(record.getBaseFont(), font.getFont().getSize2D());
  }

  /**
   * Sets the operation bounds.
   *
   * @param bounds  the bounds.
   */
  public void setOperationBounds(Rectangle2D bounds)
  {
    super.setOperationBounds(bounds);
    internalOperationBounds
        = new Rectangle2D.Double (bounds.getX() + currentPageFormat.getImageableX(),
                                  bounds.getY() + currentPageFormat.getImageableY(),
                                  bounds.getWidth(), bounds.getHeight());
  }

  /**
   * Returns the internal operation bounds.
   *
   * @return the internal operation bounds.
   */
  public Rectangle2D getInternalOperationBounds()
  {
    return internalOperationBounds;
  }
}
