/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: PDFOutputTarget.java,v 1.28 2005/03/04 16:02:30 taqua Exp $
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
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import com.lowagie.text.Anchor;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.report.ImageContainer;
import org.jfree.report.JFreeReport;
import org.jfree.report.PageDefinition;
import org.jfree.report.ShapeElement;
import org.jfree.report.content.AnchorContent;
import org.jfree.report.content.Content;
import org.jfree.report.content.DrawableContent;
import org.jfree.report.content.ImageContent;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.output.AbstractOutputTarget;
import org.jfree.report.modules.output.support.itext.BaseFontCreateException;
import org.jfree.report.modules.output.support.itext.BaseFontFactory;
import org.jfree.report.modules.output.support.itext.BaseFontRecord;
import org.jfree.report.modules.output.support.itext.BaseFontSupport;
import org.jfree.report.modules.output.support.itext.ITextImageCache;
import org.jfree.report.style.ElementDefaultStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;

/**
 * An output target for the report engine that generates a PDF file using the iText class
 * library (see <code>http://www.lowagie.com/iText</code>, note that the URL is
 * case-sensitive!).
 * <p/>
 * If the system property "org.jfree.report.modules.output.pageable.pdf.PDFOutputTarget.AUTOINIT"
 * is set to "true", the PDF-FontFactory is automatically initialized when this class is
 * loaded. Be aware that embedding many fonts will result in larger files.
 * <p/>
 * When using Unicode characters, you will have to adjust the encoding of this target to
 * "Identity-H", to enable horizontal unicode printing. This will result in larger files.
 * <p/>
 * The Encoding property is now a string with one of the values of "none" "40bit" or
 * "128bit".
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public strictfp class PDFOutputTarget extends AbstractOutputTarget
{
  /**
   * A PDF size calculator.
   */
  private static final strictfp class PDFSizeCalculator implements SizeCalculator
  {
    /**
     * The base font.
     */
    private final BaseFont baseFont;

    /**
     * The font size.
     */
    private final float fontSize;

    /**
     * Creates a new size calculator.
     *
     * @param font     the font.
     * @param fontSize the font size.
     */
    private PDFSizeCalculator (final BaseFont font, final float fontSize)
    {
      if (font == null)
      {
        throw new NullPointerException("BaseFont must not be null");
      }
      if (fontSize <= 0)
      {
        throw new IllegalArgumentException("FontSize must be greater than 0");
      }
      this.baseFont = font;
      this.fontSize = fontSize;
    }

    /**
     * Calculates the width of the specified String in the current Graphics context.
     *
     * @param text         the text to be weighted.
     * @param lineStartPos the start position of the substring to be weighted.
     * @param endPos       the position of the last characterto be included in the
     *                     weightening process.
     * @return the width of the given string in 1/72" dpi.
     */
    public float getStringWidth (final String text, final int lineStartPos,
                                 final int endPos)
    {
      return baseFont.getWidthPoint(text.substring(lineStartPos, endPos), fontSize);
    }

    /**
     * Returns the height of the current font. The font height specifies the distance
     * between 2 base lines.
     *
     * @return the font height.
     */
    public float getLineHeight ()
    {
      return fontSize;
    }
  }

  /**
   * The configuration prefix.
   */
  public static final String CONFIGURATION_PREFIX
          = "org.jfree.report.modules.output.pageable.pdf.";

  /**
   * Literal text for the 'EmbedFonts' property name.
   */
  public static final String EMBED_FONTS = "EmbedFonts";

  /**
   * Literal text for the 'AllowPrinting' property name.
   */
  public static final String SECURITY_ALLOW_PRINTING = "AllowPrinting";

  /**
   * Literal text for the 'AllowModifyContents' property name.
   */
  public static final String SECURITY_ALLOW_MODIFY_CONTENTS = "AllowModifyContents";

  /**
   * Literal text for the 'AllowCopy' property name.
   */
  public static final String SECURITY_ALLOW_COPY = "AllowCopy";

  /**
   * Literal text for the 'AllowModifyAnnotations' property name.
   */
  public static final String SECURITY_ALLOW_MODIFY_ANNOTATIONS = "AllowModifyAnnotations";

  /**
   * Literal text for the 'AllowFillIn' property name.
   */
  public static final String SECURITY_ALLOW_FILLIN = "AllowFillIn";

  /**
   * Literal text for the 'AllowScreenReaders' property name.
   */
  public static final String SECURITY_ALLOW_SCREENREADERS = "AllowScreenReaders";

  /**
   * Literal text for the 'AllowAssembly' property name.
   */
  public static final String SECURITY_ALLOW_ASSEMBLY = "AllowAssembly";

  /**
   * Literal text for the 'AllowDegradedPrinting' property name.
   */
  public static final String SECURITY_ALLOW_DEGRADED_PRINTING = "AllowDegradedPrinting";

  /**
   * Literal text for the 'Encryption' property name.
   */
  public static final String SECURITY_ENCRYPTION = "Encryption";

  /**
   * A constant for the encryption type (40 bit).
   */
  public static final String SECURITY_ENCRYPTION_NONE = "none";

  /**
   * A constant for the encryption type (40 bit).
   */
  public static final String SECURITY_ENCRYPTION_40BIT = "40bit";

  /**
   * A constant for the encryption type (128 bit).
   */
  public static final String SECURITY_ENCRYPTION_128BIT = "128bit";

  /**
   * Literal text for the 'userpassword' property name.
   */
  public static final String SECURITY_USERPASSWORD = "UserPassword";

  /**
   * Literal text for the 'ownerpassword' property name.
   */
  public static final String SECURITY_OWNERPASSWORD = "OwnerPassword";

  /**
   * A useful constant for specifying the PDF creator.
   */
  private static final String CREATOR = JFreeReport.getInfo().getName() + " version "
          + JFreeReport.getInfo().getVersion();

  /**
   * The encoding key.
   */
  public static final String ENCODING = "Encoding";

  /**
   * The 'PDF embed fonts' property key.
   */
  public static final String PDFTARGET_EMBED_FONTS
          = CONFIGURATION_PREFIX + EMBED_FONTS;

  /**
   * The default 'PDF embed fonts' property value.
   */
  public static final String PDFTARGET_EMBED_FONTS_DEFAULT = "true";

  /**
   * The 'PDF encoding' property key.
   */
  public static final String PDFTARGET_ENCODING
          = CONFIGURATION_PREFIX + ENCODING;

  /**
   * The default 'PDF encoding' property value.
   */
  public static final String PDFTARGET_ENCODING_DEFAULT = "Cp1252";

  /**
   * The pdf specification version that should be created.
   */
  public static final String PDF_VERSION = "Version";

  /**
   * The global key name for the pdf specification version that should be created.
   */
  public static final String PDFTARGET_PDF_VERSION =
          CONFIGURATION_PREFIX + PDF_VERSION;
  /**
   * The default version number for the PDF specification version.
   */
  public static final String PDF_VERSION_DEFAULT = "1.4";

  /**
   * The output stream.
   */
  private final OutputStream out;

  /**
   * The document.
   */
  private Document pdfDocument;

  /**
   * The document writer.
   */
  private PdfWriter writer;

  /**
   * The current base font.
   */
  private BaseFont baseFont;

  /**
   * The AWT font.
   */
  private FontDefinition fontDefinition;

  /**
   * The stroke used for shapes.
   */
  private Stroke awtStroke;

  /**
   * The current Paint as used in the AWT.
   */
  private Paint awtPaint;

  /**
   * The PDF font support.
   */
  private final BaseFontSupport fontSupport;

  /**
   * The current page format.
   */
  private PageFormat currentPageFormat;

  /**
   * The internal operation bounds.
   */
  private StrictBounds internalPDFOperationBounds;

  private ITextImageCache cachedImages;

  private boolean awaitOpenDocument;

  /**
   * A bytearray containing an empty password. iText replaces the owner password with
   * random values, but Adobe allows to have encryption without an owner password set.
   * Copied from iText
   */
  private static final byte PDF_PASSWORD_PAD[] = {
    (byte) 0x28, (byte) 0xBF, (byte) 0x4E, (byte) 0x5E, (byte) 0x4E, (byte) 0x75,
    (byte) 0x8A, (byte) 0x41, (byte) 0x64, (byte) 0x00, (byte) 0x4E, (byte) 0x56,
    (byte) 0xFF, (byte) 0xFA, (byte) 0x01, (byte) 0x08, (byte) 0x2E, (byte) 0x2E,
    (byte) 0x00, (byte) 0xB6, (byte) 0xD0, (byte) 0x68, (byte) 0x3E, (byte) 0x80,
    (byte) 0x2F, (byte) 0x0C, (byte) 0xA9, (byte) 0xFE, (byte) 0x64, (byte) 0x53,
    (byte) 0x69, (byte) 0x7A};

  private VolatilePdfState pdfGraphics;

  /**
   * Creates a new PDFOutputTarget.
   *
   * @param out the output stream.
   */
  public PDFOutputTarget (final OutputStream out)
  {
    this.out = out;
    this.fontSupport = new BaseFontSupport();
    this.internalPDFOperationBounds = new StrictBounds();
    this.cachedImages = new ITextImageCache();
  }

  /**
   * Returns the page height in points (1/72 inch).
   *
   * @return the page height.
   */
  private float getPageHeight ()
  {
    return this.getDocument().getPageSize().height();
  }

  /**
   * Returns the currently active AWT-Font.
   *
   * @return the current font.
   */
  protected FontDefinition getFont ()
  {
    return fontDefinition;
  }

  /**
   * Returns the iText BaseFont.
   *
   * @return the iText BaseFont.
   */
  protected BaseFont getBaseFont ()
  {
    return baseFont;
  }

  /**
   * Sets the current font. The font is mapped to pdf specific fonts if possible. If no
   * basefont could be created, an OutputTargetException is thrown.
   *
   * @param font the new font (null not permitted).
   * @throws OutputTargetException if there was a problem setting the font for the
   *                               target.
   */
  protected void setFont (final FontDefinition font)
          throws OutputTargetException
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
   * Draws an image from this {@link ImageContainer}. The image is directly embedded into
   * the pdf file to provide the best scaling support.
   *
   * @param content the image reference.
   * @throws OutputTargetException if there was a problem drawing the image to the
   *                               target.
   */
  protected void drawImage (final ImageContent content)
          throws OutputTargetException
  {
    try
    {

      final StrictBounds bounds = getInternalPDFOperationBounds();
      //final StrictBounds imageBounds = content.getBounds();

      final float imageX = (float) StrictGeomUtility.toExternalValue(bounds.getX());
      final float imageY = (float) (getPageHeight() -
              StrictGeomUtility.toExternalValue(bounds.getY() + bounds.getHeight()));

      final ImageContainer imageContent = content.getContent();
      final Image image = cachedImages.getImage(imageContent);
      image.setAbsolutePosition(imageX, imageY);

      final StrictBounds imageArea = content.getImageArea();
      final float scaleX = bounds.getWidth() / (float) imageArea.getWidth();
      final float scaleY = bounds.getHeight() / (float) imageArea.getHeight();
      // and apply the layouters scaling ..
      image.scalePercent(100 * scaleX * imageContent.getScaleX(),
                         100 * scaleY * imageContent.getScaleY());

      final PdfContentByte cb = this.writer.getDirectContent();
      cb.saveState();

      final float clipX = (float) StrictGeomUtility.toExternalValue
              (imageArea.getX() + bounds.getX());
      final float clipY = (float) (getPageHeight() -
              StrictGeomUtility.toExternalValue (bounds.getY() + imageArea.getHeight()));
      cb.rectangle(clipX, clipY,
              (float) StrictGeomUtility.toExternalValue(imageArea.getWidth()),
              (float) StrictGeomUtility.toExternalValue(imageArea.getHeight()));
      cb.clip();
      cb.newPath();
      cb.addImage(image);
      cb.restoreState();

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
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is not
   * filled.
   *
   * @param shape the shape to draw.
   */
  protected void drawShape (final Shape shape)
  {
    final StrictBounds internalBounds = getInternalPDFOperationBounds();

    final float ycorr = (float) StrictGeomUtility.toExternalValue(internalBounds.getY());
    final float xcorr = (float) StrictGeomUtility.toExternalValue(internalBounds.getX());

    final float urx = (float) currentPageFormat.getWidth();
    final float ury = (float) currentPageFormat.getHeight();

    final Graphics2D g2 = writer.getDirectContent().createGraphics(urx, ury, fontSupport);
    g2.setPaint(getPaint());
    g2.setStroke(getStroke());
    g2.translate(xcorr, ycorr);
    g2.draw(shape);
    g2.dispose();
  }

  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is filled
   * using the current paint and no outline is drawn.
   *
   * @param shape the shape to fill.
   */
  protected void fillShape (final Shape shape)
  {
    final StrictBounds internalBounds = getInternalPDFOperationBounds();

    final float ycorr = (float) StrictGeomUtility.toExternalValue(internalBounds.getY());
    final float xcorr = (float) StrictGeomUtility.toExternalValue(internalBounds.getX());

    final float urx = (float) currentPageFormat.getWidth();
    final float ury = (float) currentPageFormat.getHeight();

    final Graphics2D g2 = writer.getDirectContent().createGraphics(urx, ury, fontSupport);
    g2.setPaint(getPaint());
    g2.setStroke(getStroke());
    g2.translate(xcorr, ycorr);
    g2.fill(shape);
    g2.dispose();
  }

  /**
   * This method is called when the page is ended.
   *
   * @throws OutputTargetException if there was a problem with the target.
   */
  public void endPage ()
          throws OutputTargetException
  {
    try
    {
//      this.pdfGraphics.dispose();
//      this.pdfGraphics = null;
//      this.writer.getDirectContent().restoreState();
      this.getDocument().newPage();
    }
    catch (Exception e)
    {
      throw new OutputTargetException("Failed to end page", e);
    }
  }

  /**
   * Extracts the to be generated PDF version as iText parameter from the given property
   * value. The value has the form "1.x" where x is the extracted version.
   *
   * @param version the version string.
   * @return the itext character defining the version.
   */
  private char getVersion (final String version)
  {
    if (version == null)
    {
      return '4';
    }
    if (version.length() < 3)
    {
      Log.warn("PDF version specification is invalid.");
      return '4';
    }
    final char retval = version.charAt(2);
    if (retval < '2' || retval > '5')
    {
      Log.warn("PDF version specification is invalid.");
      return '4';
    }
    return retval;
  }

  /**
   * Opens the document.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void open ()
          throws OutputTargetException
  {
    try
    {
      setDocument(new Document());
      //pageSize, marginLeft, marginRight, marginTop, marginBottom));

      writer = PdfWriter.getInstance(getDocument(), out);
      writer.setLinearPageMode();

      final char version = getVersion(getProperty(PDF_VERSION, PDF_VERSION_DEFAULT));
      writer.setPdfVersion(version);

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

      //getDocument().open();
      awaitOpenDocument = true;

      //writer.getDirectContent().beginTransaction();
    }
    catch (Exception e)
    {
      throw new OutputTargetException("Opening Document failed.", e);
    }
  }


  /**
   * Signals that a page is being started. Stores the state of the target to make it
   * possible to restore the complete outputtarget.
   *
   * @param format the physical page.
   */
  public void beginPage (final PageDefinition format, final int i)
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Target " + hashCode() + " is not open");
    }

    final PageFormat pageFormat = format.getPageFormat(i);
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

    getDocument().setPageSize(pageSize);
    getDocument().setMargins(marginLeft, marginRight, marginTop, marginBottom);

    // Bug: Document open expects that the first page is fully configured.
    //      we have to delay the open operation until we got the first page.
    if (awaitOpenDocument)
    {
      getDocument().open();
      awaitOpenDocument = false;
    }

    this.pdfGraphics = new VolatilePdfState(writer.getDirectContent(), ury);

    try
    {
      setPaint(ElementDefaultStyleSheet.DEFAULT_PAINT);
      setStroke(ShapeElement.DEFAULT_STROKE);
      setFont(ElementDefaultStyleSheet.DEFAULT_FONT_DEFINITION);
    }
    catch (OutputTargetException oe)
    {
      // should not happen in a sane environment ...
      Log.error("Exception while defining defaults.", oe);
      throw new IllegalStateException("Exception while defining defaults.");
    }

    this.currentPageFormat = pageFormat;
  }

  /**
   * Reads a boolean property. If the property is not set, the given default value is
   * returned. This method returns true, if the property is set to the value "true", false
   * otherwise.
   *
   * @param key   the key that should be queried.
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
   * Extracts the permissions for this PDF. The permissions are returned as flags in the
   * integer value. All permissions are defined as properties which have to be set before
   * the target is opened.
   *
   * @return the permissions.
   */
  private int getPermissions ()
  {
    final boolean allowPrinting = getBooleanProperty(SECURITY_ALLOW_PRINTING, false);
    final boolean allowModifyContents = getBooleanProperty(SECURITY_ALLOW_MODIFY_CONTENTS, false);
    final boolean allowModifyAnnotations =
            getBooleanProperty(SECURITY_ALLOW_MODIFY_ANNOTATIONS, false);
    final boolean allowCopy = getBooleanProperty(SECURITY_ALLOW_COPY, false);
    final boolean allowFillIn = getBooleanProperty(SECURITY_ALLOW_FILLIN, false);
    final boolean allowScreenReaders = getBooleanProperty(SECURITY_ALLOW_SCREENREADERS, false);
    final boolean allowAssembly = getBooleanProperty(SECURITY_ALLOW_ASSEMBLY, false);
    final boolean allowDegradedPrinting =
            getBooleanProperty(SECURITY_ALLOW_DEGRADED_PRINTING, false);

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
  public void close ()
  {
    this.getDocument().close();
    this.fontSupport.close();
    this.pdfDocument = null;
    this.writer = null;
  }

  /**
   * Draws the band onto the specified graphics device. The Text is printed on the bottom
   * of the elements bounds.
   *
   * @param text The text to be printed.
   */
  protected void printText (final String text)
  {
    final StrictBounds bounds = getInternalPDFOperationBounds();
    final float fontSize = getFont().getFontSize();

    final PdfContentByte cb = this.writer.getDirectContent();

    pdfGraphics.setPaint(getPaint(), true);
    pdfGraphics.setStroke(getStroke());

    cb.beginText();
    cb.setFontAndSize(this.baseFont, fontSize);

    final float ascent = baseFont.getFontDescriptor(BaseFont.ASCENT, fontSize);
    final float y2 = (float) (StrictGeomUtility.toExternalValue(bounds.getY()) + ascent);
    final float x1 = (float) StrictGeomUtility.toExternalValue(bounds.getX());
    final float x2 = (float) StrictGeomUtility.toExternalValue(bounds.getX() + bounds.getWidth());

    cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
            text, x1, this.getPageHeight() - y2, 0);
    cb.endText();

    pdfGraphics.setPaint(getPaint(), false);

    if (getFont().isUnderline())
    {
      final float underlinePosition = (fontSize - ascent) * 0.8f;
      cb.moveTo(x1, getPageHeight() - y2 - underlinePosition);
      cb.lineTo(x2, getPageHeight() - y2 - underlinePosition);
    }
    if (getFont().isStrikeThrough())
    {
      final float strikethroughPosition = fontSize * 0.5f;
      cb.moveTo(x1, getPageHeight() - y2 + strikethroughPosition);
      cb.lineTo(x2, getPageHeight() - y2 + strikethroughPosition);
    }
  }

  /**
   * Defines the stroke used to draw shapes. If the stroke is of an invalid type, an
   * OutputTargetException is thrown. Currently only BasicStroke is supported.
   *
   * @param stroke the stroke.
   */
  protected void setStroke (final Stroke stroke)
  {
    if (stroke == null)
    {
      throw new NullPointerException();
    }
    if (stroke instanceof BasicStroke == false)
    {
      //throw new OutputTargetException("Unable to handle this stroke type");
      Log.warn("Unable to handle stroke type: " + stroke.getClass() + " will be ignored.");
      return;
    }

    // If this stroke is already set, do nothing
    if (awtStroke != null && awtStroke.equals(stroke))
    {
      return;
    }

    this.awtStroke = stroke;
//    pdfGraphics.setStroke(awtStroke);
  }

  /**
   * Returns the current stroke.
   *
   * @return the current stroke.
   */
  protected Stroke getStroke ()
  {
    return awtStroke;
  }

  /**
   * Sets the paint. If the paint could not be converted into a pdf object, an
   * OutputTargetException is thrown. This implementation currently supports
   * java.awt.Color as the only valid paint.
   *
   * @param paint the paint.
   */
  protected void setPaint (final Paint paint)
  {
    if (paint == null)
    {
      throw new NullPointerException();
    }

    if (paint instanceof Color == false)
    {
      Log.warn("Unable to handle paint type: " + paint.getClass() + " will be ignored.");
      return;
    }

    // If this paint is already set, do nothing
    if (awtPaint != null && awtPaint.equals(paint))
    {
      return;
    }

    this.awtPaint = paint;
  }

  /**
   * Returns the currently set paint.
   *
   * @return the paint.
   */
  protected Paint getPaint ()
  {
    return awtPaint;
  }

  protected boolean isPaintSupported (final Paint p)
  {
    return (p instanceof Color || p instanceof GradientPaint || p instanceof TexturePaint);
  }

  /**
   * returns the font encoding used in this output target.
   *
   * @return the font encoding.
   */
  private String getFontEncoding ()
  {
    return getProperty(ENCODING, BaseFontFactory.getDefaultFontEncoding());
  }

  /**
   * Defines the text encoding used in this output target.
   * <p/>
   * <ul> <li>The Unicode encoding with horizontal writing is "Identity-H" <li>The Unicode
   * encoding with vertical writing is "Identity-V" <li>"Cp1250" <li>"Cp1252" is also
   * known as WinAnsi <li>"Cp1257" <li>"MacRoman" </ul>
   *
   * @param encoding the font encoding.
   */
  public void setFontEncoding (final String encoding)
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
  public boolean isEmbedFonts ()
  {
    return getProperty(EMBED_FONTS, "false").equals("true");
  }

  /**
   * Sets the 'embed fonts' flag.
   *
   * @param embedFonts the new flag value.
   */
  public void setEmbedFonts (final boolean embedFonts)
  {
    setProperty(EMBED_FONTS, String.valueOf(embedFonts));
  }

  /**
   * Returns the document.
   *
   * @return the document.
   */
  private Document getDocument ()
  {
    return pdfDocument;
  }

  /**
   * Sets the document.
   *
   * @param document the document (null not permitted).
   */
  private void setDocument (final Document document)
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
   * @param config the configuration.
   */
  public void configure (final ReportConfiguration config)
  {
    updateProperty(SECURITY_OWNERPASSWORD, config);
    updateProperty(SECURITY_USERPASSWORD, config);
    updateProperty(AUTHOR, config);
    updateProperty(ENCODING, config);
    updateProperty(PDF_VERSION, config);
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
   * @param key    the key.
   * @param config the config.
   */
  private void updateProperty (final String key, final ReportConfiguration config)
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
   * @param key    the key.
   * @param config the config.
   */
  private void updateBooleanProperty (final String key, final ReportConfiguration config)
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
  public boolean isOpen ()
  {
    if (getDocument() == null)
    {
      //Log.debug ("Document is null, assuming that the document is closed ...");
      return false;
    }
    return (awaitOpenDocument || getDocument().isOpen());
  }

  /**
   * Creates a 'size calculator' for the current state of the output target. The
   * calculator is used to calculate the string width and line height and later maybe
   * more.
   *
   * @param font the font.
   * @return the size calculator.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator (final FontDefinition font)
          throws OutputTargetException
  {
    try
    {
      final BaseFontRecord record = fontSupport.createBaseFont(font,
              font.getFontEncoding(getFontEncoding()), isEmbedFonts() || font.isEmbeddedFont());
      return new PDFSizeCalculator(record.getBaseFont(), font.getFontSize());
    }
    catch (BaseFontCreateException bfce)
    {
      throw new OutputTargetException("The font definition was not successfull.", bfce);
    }
  }

  /**
   * Sets the operation bounds.
   *
   * @param bounds the bounds.
   */
  protected void setInternalOperationBounds (final StrictBounds bounds)
  {
    super.setInternalOperationBounds(bounds);
    final StrictBounds pageBounds = getInternalPageBounds();
    final long x = -pageBounds.getX() + bounds.getX() +
            StrictGeomUtility.toInternalValue(currentPageFormat.getImageableX());
    final long y = -pageBounds.getY() + bounds.getY() +
            StrictGeomUtility.toInternalValue(currentPageFormat.getImageableY());

    internalPDFOperationBounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
  }

  /**
   * Returns the internal operation bounds.
   *
   * @return the internal operation bounds.
   */
  private StrictBounds getInternalPDFOperationBounds ()
  {
    return internalPDFOperationBounds;
  }

  /**
   * Draws a drawable relative to the current position.
   *
   * @param content the drawable to draw.
   */
  protected void drawDrawable (final DrawableContent content)
  {
    // only the drawable clippingbounds region will be drawn.
    // the clipping is set to the clipping bounds of the drawable

    // the clipping bounds are relative to the drawable dimension,
    // they are not influenced by the drawables position on the page

    final StrictBounds bounds = getInternalPDFOperationBounds();
    final float x = (float) StrictGeomUtility.toExternalValue(bounds.getX());
    final float y = (float) StrictGeomUtility.toExternalValue(bounds.getY());

    // the graphics object has a width and a height, but no
    // x,y position; so we have to assume (x = 0; y = 0) and
    // make sure that the graphics is large enough to draw everything
    final float urx = (float) currentPageFormat.getWidth();
    final float ury = (float) currentPageFormat.getHeight();
    final Graphics2D g2 = writer.getDirectContent().createGraphics(urx, ury, fontSupport);
    g2.setPaint(getPaint());
    g2.setStroke(getStroke());
    // make sure, that the operation bounds are met ..
    g2.translate(x, y);

    // only the drawable clippingbounds region will be drawn.
    // the clipping is set to the clipping bounds of the drawable

    // the clipping bounds are relative to the drawable dimension,
    // they are not influenced by the drawables position on the page
    final Drawable drawable = content.getContent();
    final StrictBounds imageArea = content.getImageArea();

    final double imageWidth = StrictGeomUtility.toExternalValue(bounds.getWidth());
    final double imageHeight = StrictGeomUtility.toExternalValue(bounds.getHeight());
    final Rectangle2D newClipArea = new Rectangle2D.Double(0, 0, imageWidth, imageHeight);
    g2.clip(newClipArea);

    final int imageX = (int) StrictGeomUtility.toExternalValue(imageArea.getX());
    final int imageY = (int) StrictGeomUtility.toExternalValue(imageArea.getY());
    g2.translate(-imageX, -imageY);

    final StrictBounds drawableBounds = content.getBounds();
    final double drawableWidth = StrictGeomUtility.toExternalValue(drawableBounds.getWidth());
    final double drawableHeight = StrictGeomUtility.toExternalValue(drawableBounds.getHeight());
    drawable.draw(g2, new Rectangle2D.Double(0, 0, drawableWidth, drawableHeight));

    g2.dispose();
  }

  /**
   * Returns the PDF encoding property value.
   *
   * @return the PDF encoding property value.
   */
  public static String getDefaultPDFEncoding ()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
            (PDFTARGET_ENCODING, PDFTARGET_ENCODING_DEFAULT);
  }

  /**
   * Sets the PDF encoding property value.
   *
   * @param pdfTargetEncoding the new encoding.
   */
  public static void setDefaultPDFEncoding (final String pdfTargetEncoding)
  {
    ReportConfiguration.getGlobalConfig().setConfigProperty
            (PDFTARGET_ENCODING, pdfTargetEncoding);
  }

  /**
   * Returns true, if the Graphics2D should use aliasing to render text. Defaults to
   * false.
   *
   * @return true, if aliasing is enabled.
   */
  public static boolean isDefaultEmbedFonts ()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
            (PDFTARGET_EMBED_FONTS, PDFTARGET_EMBED_FONTS_DEFAULT).equalsIgnoreCase("true");
  }

  /**
   * set to true, if the PDFOutputTarget should embed all fonts.
   *
   * @param embed set to true, if the PDFOutputTarget should use embedded fonts.
   */
  public static void setDefaultEmbedFonts (final boolean embed)
  {
    ReportConfiguration.getGlobalConfig().setConfigProperty
            (PDFTARGET_EMBED_FONTS, String.valueOf(embed));
  }

  protected void printAnchorContent (final MetaElement element,
                                     final Content content)
          throws OutputTargetException
  {
    try
    {
      final AnchorContent ac = (AnchorContent) content;
      final Anchor anchor = new Anchor();
      anchor.setName(ac.getAnchor().getName());
      writer.add(anchor);
    }
    catch (DocumentException e)
    {
      throw new OutputTargetException("Failed to add anchor", e);
    }
  }

  protected void printHRefForCurrentContent (final String href)
  {
    final PdfAction action = new PdfAction(href);
    final StrictBounds elementBounds = getInternalPDFOperationBounds();

    final float leftX = (float) StrictGeomUtility.toExternalValue(elementBounds.getX());
    final float rightX = (float) StrictGeomUtility.toExternalValue
            (elementBounds.getX() + elementBounds.getWidth());
    final float lowerY = getPageHeight() - (float)
            StrictGeomUtility.toExternalValue(elementBounds.getY() + elementBounds.getHeight());
    final float upperY = getPageHeight() - (float)
            StrictGeomUtility.toExternalValue(elementBounds.getY());
    final PdfContentByte cb = this.writer.getDirectContent();

    //Log.debug("Added HREF " + href + " Bounds: " + elementBounds);
    cb.setAction(action, leftX, lowerY, rightX, upperY);
  }

}
