/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Thomas Morgner, Object Refinery Limited and Contributors.
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
 * (C)opyright 2002, 2003, by Object Refinery Limited.and Contributors;
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PDFOutputTarget.java,v 1.4 2003/07/14 20:16:05 taqua Exp $
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
 * 29-May-2003 : Turned off alpha encoding for PNG images, see bug ID 744941 (DG);
 */

package org.jfree.report.modules.output.pageable.pdf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import com.keypoint.PngEncoder;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.report.DrawableContainer;
import org.jfree.report.ImageReference;
import org.jfree.report.JFreeReport;
import org.jfree.report.ShapeElement;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.modules.output.pageable.base.output.AbstractOutputTarget;
import org.jfree.report.modules.output.pageable.base.output.DummyOutputTarget;
import org.jfree.report.modules.output.pageable.base.physicals.LogicalPageImpl;
import org.jfree.report.modules.output.pageable.base.physicals.PhysicalPage;
import org.jfree.report.modules.output.pageable.base.LogicalPage;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.support.itext.BaseFontFactory;
import org.jfree.report.modules.output.support.itext.BaseFontRecord;
import org.jfree.report.modules.output.support.itext.BaseFontSupport;
import org.jfree.report.modules.output.support.itext.BaseFontCreateException;
import org.jfree.report.style.ElementDefaultStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.WaitingImageObserver;

/**
 * An output target for the report engine that generates a PDF file using the iText class library
 * (see <code>http://www.lowagie.com/iText</code>, note that the URL is case-sensitive!).
 * <p>
 * If the system property "org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget.AUTOINIT"
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
 * @author Thomas Morgner
 */
public class PDFOutputTarget extends AbstractOutputTarget
{
  /** The 'PDF embed fonts' property key. */
  public static final String PDFTARGET_EMBED_FONTS
      = "org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget.default.EmbedFonts";

  /** The default 'PDF embed fonts' property value. */
  public static final String PDFTARGET_EMBED_FONTS_DEFAULT = "true";

  /** The 'PDF encoding' property key. */
  public static final String PDFTARGET_ENCODING
      = "org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget.default.Encoding";

  /** The default 'PDF encoding' property value. */
  public static final String PDFTARGET_ENCODING_DEFAULT = "Cp1252";

  /** The configuration prefix. */
  public static final String CONFIGURATION_PREFIX
      = "org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget.default.";

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

  /** The stroke used for shapes. */
  private Stroke awtStroke;

  /** The current Paint as used in the AWT. */
  private Paint awtPaint;

  /** The PDF font support. */
  private BaseFontSupport fontSupport;

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
   * Constructs a PDFOutputTarget.
   *
   * @param out  the output stream.
   * @param pageFormat  the page format.
   * @param embedFonts  embed fonts?
   */
  public PDFOutputTarget(final OutputStream out, final PageFormat pageFormat, final boolean embedFonts)
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
  public PDFOutputTarget(final OutputStream out, final LogicalPage logPage, final boolean embedFonts)
  {
    super(logPage);
    this.out = out;
    this.fontSupport = new BaseFontSupport();
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
  public PDFOutputTarget(final OutputStream out, final PageFormat logPageFormat, final PageFormat physPageFormat,
                         final boolean embedFonts)
  {
    this(out, new LogicalPageImpl(logPageFormat, physPageFormat), embedFonts);
  }

  /**
   * Returns the default font encoding.
   *
   * @return the default font encoding.
   */
  private static final String getDefaultFontEncoding()
  {
    return BaseFontFactory.getDefaultFontEncoding();
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
  public BaseFont getBaseFont()
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
  public void setFont(final FontDefinition font) throws OutputTargetException
  {
    if (font == null)
    {
      throw new NullPointerException();
    }

    // do nothing if this font is already set
    if (baseFont != null && fontDefinition != null && fontDefinition.equals(font))
    {
      //Log.debug ("The Fonts are equal, request ignored.");
      return; // no need to do anything ...
    }
    this.fontDefinition = font;
    try
    {
      this.baseFont = fontSupport.createBaseFont(font, font.getFontEncoding(getFontEncoding()),
          (isEmbedFonts() || font.isEmbeddedFont())).getBaseFont();
      if (baseFont == null)
      {
        throw new OutputTargetException("The font definition was not successfull.");
      }
    }
    catch (BaseFontCreateException bfce)
    {
      throw new OutputTargetException("The font definition was not successfull.", bfce);
    }
  }

  /**
   * Draws an image from this {@link ImageReference}. The image is directly embedded into the
   * pdf file to provide the best scaling support.
   *
   * @param imageRef  the image reference.
   *
   * @throws OutputTargetException if there was a problem drawing the image to the target.
   */
  public void drawImage(final ImageReference imageRef) throws OutputTargetException
  {
    try
    {
      final Rectangle2D bounds = getInternalOperationBounds();
      final Rectangle2D imageBounds = imageRef.getBoundsScaled();

      final float x = (float) (bounds.getX());
      final float y = (float) (bounds.getY());

      final Image image = getImage(imageRef);
      image.setAbsolutePosition(x, (float) (getPageHeight() - y - bounds.getHeight()));
      image.scaleAbsolute((float) imageBounds.getWidth(),
          (float) imageBounds.getHeight());

      final PdfContentByte cb = this.writer.getDirectContent();

      cb.rectangle((float) (imageBounds.getX() + x),
          (float) (getPageHeight() - imageBounds.getY() - y - bounds.getHeight()),
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
   * @throws com.lowagie.text.DocumentException if no PDFImageElement could be created using the given
   *                           ImageReference.
   * @throws java.io.IOException if the image could not be read.
   */
  private Image getImage(final ImageReference imageRef) throws DocumentException, IOException
  {
    final Rectangle2D bounds = getInternalOperationBounds();
    final Rectangle2D imageBounds = imageRef.getBoundsScaled();

    try
    {
      final Rectangle2D drawArea = new Rectangle2D.Float(0, 0, (float) bounds.getWidth(),
          (float) bounds.getHeight());
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
      // since version 0.99 iText supports Alpha-PNGs
      final WaitingImageObserver obs = new WaitingImageObserver(imageRef.getImage());
      obs.waitImageLoaded();

      final PngEncoder encoder = new PngEncoder(imageRef.getImage(), PngEncoder.ENCODE_ALPHA,
          PngEncoder.FILTER_NONE, 5);
      final byte[] data = encoder.pngEncode();
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
  private float getCorrectedY(final float y)
  {
    return getPageHeight() - y;
  }

  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is not filled.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape(final Shape shape)
  {
    final Rectangle2D bounds = getInternalOperationBounds();

    final float ycorr = (float) bounds.getY();
    final float xcorr = (float) bounds.getX();

    final PathIterator pit = shape.getPathIterator(null);
    final PdfContentByte cb = this.writer.getDirectContent();

    cb.newPath();
    cb.moveTo(xcorr, getCorrectedY(ycorr));

    final float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone() == false)
    {
      final int cmd = pit.currentSegment(params);
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
  public void fillShape(final Shape shape)
  {
    final Rectangle2D bounds = getInternalOperationBounds();

    final float ycorr = (float) bounds.getY();
    final float xcorr = (float) bounds.getX();

    final PathIterator pit = shape.getPathIterator(null);
    final PdfContentByte cb = this.writer.getDirectContent();

    cb.newPath();
    cb.moveTo(xcorr, getCorrectedY(ycorr));

    final int windingRule = pit.getWindingRule();

    final float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone() == false)
    {
      final int cmd = pit.currentSegment(params);
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

    final PageFormat pageFormat = getLogicalPage().getPhysicalPageFormat();
    final float urx = (float) pageFormat.getWidth();
    final float ury = (float) pageFormat.getHeight();

    final float marginLeft = (float) pageFormat.getImageableX();
    final float marginRight =
        (float) (pageFormat.getWidth()
        - pageFormat.getImageableWidth()
        - pageFormat.getImageableX());
    final float marginTop = (float) pageFormat.getImageableY();
    final float marginBottom =
        (float) (pageFormat.getHeight()
        - pageFormat.getImageableHeight()
        - pageFormat.getImageableY());
    final Rectangle pageSize = new Rectangle(urx, ury);

    try
    {
      if (pageFormat.getOrientation() != PageFormat.PORTRAIT)
      {
        pageSize.rotate();
      }

      setDocument(new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom));

      writer = PdfWriter.getInstance(getDocument(), out);
      writer.setLinearPageMode();

      final String encrypt = getProperty(SECURITY_ENCRYPTION);

      if (encrypt != null)
      {
        if (encrypt.equals(SECURITY_ENCRYPTION_128BIT) == true
            || encrypt.equals(SECURITY_ENCRYPTION_40BIT) == true)
        {
          final String userpassword = getProperty(SECURITY_USERPASSWORD);
          final String ownerpassword = getProperty(SECURITY_OWNERPASSWORD);
          //Log.debug ("UserPassword: " + userpassword + " - OwnerPassword: " + ownerpassword);
          final byte[] userpasswordbytes = DocWriter.getISOBytes(userpassword);
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
      final String title = getProperty(TITLE);
      final String author = getProperty(AUTHOR);

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

      //writer.getDirectContent().saveState();
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
   */
  public void beginPage(final PhysicalPage format)
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Target " + hashCode() + " is not open");
    }

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

    this.writer.getDirectContent().saveState();
    this.currentPageFormat = format.getPageFormat();
  }

  /**
   * Reads a boolean property. If the property is not set, the given
   * default value is returned. This method returns true, if the property
   * is set to the value "true", false otherwise.
   *
   * @param key the key that should be queried.
   * @param value the defaultvalue.
   * @return the true, if the property has the value "true", false otherwise.
   */
  private boolean getBooleanProperty (final String key, final boolean value)
  {
    final String val = getProperty(key);
    if (val == null)
    {
      return value;
    }
    else
    {
      return val.equals("true");
    }
  }

  /**
   * Extracts the permissions for this PDF. The permissions are returned as flags in the integer
   * value. All permissions are defined as properties which have to be set before the target is
   * opened.
   *
   * @return the permissions.
   */
  private int getPermissions()
  {
    final boolean allowPrinting = getBooleanProperty(SECURITY_ALLOW_PRINTING, false);
    final boolean allowModifyContents = getBooleanProperty(SECURITY_ALLOW_MODIFY_CONTENTS, false);
    final boolean allowModifyAnnotations = getBooleanProperty(SECURITY_ALLOW_MODIFY_ANNOTATIONS, false);
    final boolean allowCopy = getBooleanProperty(SECURITY_ALLOW_COPY, false);
    final boolean allowFillIn = getBooleanProperty(SECURITY_ALLOW_FILLIN, false);
    final boolean allowScreenReaders = getBooleanProperty(SECURITY_ALLOW_SCREENREADERS, false);
    final boolean allowAssembly = getBooleanProperty(SECURITY_ALLOW_ASSEMBLY, false);
    final boolean allowDegradedPrinting = getBooleanProperty(SECURITY_ALLOW_DEGRADED_PRINTING, false);

    int permissions = 0;
    if (allowPrinting)
    {
      permissions |= PdfWriter.AllowPrinting;
    }
    if (allowModifyContents)
    {
      permissions |= PdfWriter.AllowModifyContents;
    }
    if (allowModifyAnnotations)
    {
      permissions |= PdfWriter.AllowModifyAnnotations;
    }
    if (allowCopy)
    {
      permissions |= PdfWriter.AllowCopy;
    }
    if (allowFillIn)
    {
      permissions |= PdfWriter.AllowFillIn;
    }
    if (allowScreenReaders)
    {
      permissions |= PdfWriter.AllowScreenReaders;
    }
    if (allowAssembly)
    {
      permissions |= PdfWriter.AllowAssembly;
    }
    if (allowDegradedPrinting)
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
  public void drawString(final String text)
  {
    final Rectangle2D bounds = getInternalOperationBounds();
    final int fontSize = getFont().getFontSize();

    final PdfContentByte cb = this.writer.getDirectContent();
    cb.beginText();
    cb.setFontAndSize(this.baseFont, fontSize);

    final float y2 = (float) (bounds.getY() + baseFont.getFontDescriptor(BaseFont.ASCENT, fontSize));
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
  public void setStroke(final Stroke stroke) throws OutputTargetException
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
    final BasicStroke bstroke = (BasicStroke) stroke;
    final PdfContentByte cb = this.writer.getDirectContent();
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
  public void setPaint(final Paint paint) throws OutputTargetException
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
    final PdfContentByte cb = this.writer.getDirectContent();

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
   * returns the font encoding used in this output target.
   *
   * @return the font encoding.
   */
  private String getFontEncoding()
  {
    return getProperty(ENCODING, getDefaultFontEncoding());
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
  public void setFontEncoding(final String encoding)
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
  private boolean isEmbedFonts()
  {
    return getProperty(EMBED_FONTS, "false").equals("true");
  }

  /**
   * Sets the 'embed fonts' flag.
   *
   * @param embedFonts  the new flag value.
   */
  private void setEmbedFonts(final boolean embedFonts)
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
    return new DummyOutputTarget(this);
  }

  /**
   * Returns the document.
   *
   * @return the document.
   */
  private Document getDocument()
  {
    return pdfDocument;
  }

  /**
   * Sets the document.
   *
   * @param document  the document (null not permitted).
   */
  private void setDocument(final Document document)
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
  public void configure(final ReportConfiguration config)
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
    updateProperty(SECURITY_ENCRYPTION, config);
  }

  /**
   * Updates a property.
   *
   * @param key  the key.
   * @param config  the config.
   */
  private void updateProperty(final String key, final ReportConfiguration config)
  {
    final String configValue = config.getConfigProperty(CONFIGURATION_PREFIX + key);
    final String propertyValue = getProperty(key, configValue);
    if (propertyValue != null)
    {
      setProperty(key, propertyValue);
    }
  }

  /**
   * Updates a boolean property.
   *
   * @param key  the key.
   * @param config  the config.
   */
  private void updateBooleanProperty(final String key, final ReportConfiguration config)
  {
    final String configValue = config.getConfigProperty(CONFIGURATION_PREFIX + key);
    final String propertyValue = getProperty(key, configValue);
    // property is neither set in the configuration nor in the properties ...
    if (propertyValue == null)
    {
      return;
    }

    if (propertyValue.equalsIgnoreCase("true"))
    {
      setProperty(key, getProperty(key, "true"));
    }
    else
    {
      setProperty(key, getProperty(key, "false"));
    }
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
    private PDFSizeCalculator(final BaseFont font, final float fontSize)
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
    public float getStringWidth(final String text, final int lineStartPos, final int endPos)
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
  public SizeCalculator createTextSizeCalculator(final FontDefinition font) throws OutputTargetException
  {
    try
    {
      final BaseFontRecord record = fontSupport.createBaseFont(font,
          font.getFontEncoding(getFontEncoding()),
          false);
      return new PDFSizeCalculator(record.getBaseFont(), font.getFont().getSize2D());
    }
    catch (BaseFontCreateException bfce)
    {
      throw new OutputTargetException("The font definition was not successfull.", bfce);
    }
  }

  /**
   * Sets the operation bounds.
   *
   * @param bounds  the bounds.
   */
  public void setOperationBounds(final Rectangle2D bounds)
  {
    super.setOperationBounds(bounds);
    internalOperationBounds
        = new Rectangle2D.Float((float) (bounds.getX() + currentPageFormat.getImageableX()),
            (float) (bounds.getY() + currentPageFormat.getImageableY()),
            (float) bounds.getWidth(), (float) bounds.getHeight());
  }

  /**
   * Returns the internal operation bounds.
   *
   * @return the internal operation bounds.
   */
  private Rectangle2D getInternalOperationBounds()
  {
    return internalOperationBounds;
  }

  /**
   * Draws a drawable relative to the current position.
   *
   * @param drawable the drawable to draw.
   */
  public void drawDrawable(final DrawableContainer drawable)
  {
    // cant be done using Wmf, as Wmf does not support Unicode and Bitmaps... damn

    // not yet implemented, needs WMF Converter ...
    // only the drawable clippingbounds region will be drawn.
    // the clipping is set to the clipping bounds of the drawable

    // the clipping bounds are relative to the drawable dimension,
    // they are not influenced by the drawables position on the page

    final Rectangle2D clipBounds = drawable.getClippingBounds();

    final Graphics2D target = writer.getDirectContent().createGraphics((float) clipBounds.getWidth(),
        (float) clipBounds.getHeight());
    target.translate(-clipBounds.getX(), -clipBounds.getY());
    target.clip(new Rectangle2D.Float(0, 0,
        (float) clipBounds.getWidth(),
        (float) clipBounds.getHeight()));

    final Dimension2D drawableSize = drawable.getDrawableSize();
    final Rectangle2D drawBounds = new Rectangle2D.Float(0, 0,
        (float) drawableSize.getWidth(),
        (float) drawableSize.getHeight());
    drawable.getDrawable().draw(target, drawBounds);
    target.dispose();
  }

  /**
   * Returns the PDF encoding property value.
   *
   * @return the PDF encoding property value.
   */
  public static String getDefaultPDFEncoding()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        (PDFTARGET_ENCODING, PDFTARGET_ENCODING_DEFAULT);
  }

  /**
   * Sets the PDF encoding property value.
   *
   * @param pdfTargetEncoding  the new encoding.
   */
  public static void setDefaultPDFEncoding(final String pdfTargetEncoding)
  {
    ReportConfiguration.getGlobalConfig().setConfigProperty
        (PDFTARGET_ENCODING, pdfTargetEncoding);
  }

  /**
   * Returns true, if the Graphics2D should use aliasing to render text. Defaults to false.
   *
   * @return true, if aliasing is enabled.
   */
  public static boolean isDefaultEmbedFonts()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        (PDFTARGET_EMBED_FONTS, PDFTARGET_EMBED_FONTS_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the PDFOutputTarget should embed all fonts.
   *
   * @param embed set to true, if the PDFOutputTarget should use embedded fonts.
   */
  public static void setDefaultEmbedFonts(final boolean embed)
  {
    ReportConfiguration.getGlobalConfig().setConfigProperty
        (PDFTARGET_EMBED_FONTS, String.valueOf(embed));
  }


}
