/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ---------------------
 * RTFImageCellData.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFImageCellData.java,v 1.3 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.rtf;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;

import com.keypoint.PngEncoder;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import org.jfree.report.ImageReference;
import org.jfree.report.util.Log;
import org.jfree.report.util.StringUtil;
import org.jfree.report.util.WaitingImageObserver;

/**
 * A wrapper for Image content within the generated RTFTable.
 *
 * @author Thomas Morgner
 */
public class RTFImageCellData extends RTFCellData
{
  /** the imagereference used for this cell. */
  private ImageReference image;

  /**
   * Creates a new ImageCell for the given bounds and image.
   *
   * @param outerBounds the cell bounds.
   * @param image the image content.
   * @param style the assigned cell style.
   */
  public RTFImageCellData(final Rectangle2D outerBounds, final ImageReference image,
                          final RTFCellStyle style)
  {
    super(outerBounds, style);
    if (image == null)
    {
      throw new NullPointerException();
    }
    this.image = image;
  }

  /**
   * Creates a iText TableCell with image content in it. The image is
   * recoded as PNG if necessary.
   *
   * @return the cell with the content.
   * @throws DocumentException if the cell could not be created.
   */
  public Cell getCell() throws DocumentException
  {
    try
    {
      final Image iTextImage = getImage(image);
      final Cell cell = new Cell();
      getStyle().applyAlignment(cell);
      cell.addElement(iTextImage);
      return cell;
    }
    catch (DocumentException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new DocumentException("Failed to create image");
    }
  }

  /**
   * Gets a flag, which indicates whether this cell contains background definitions.
   *
   * @return false, as this is no background cell.
   */
  public boolean isBackground()
  {
    return false;
  }

  /**
   * Helperfunction to extract an image from an imagereference. If the image is
   * contained as java.awt.Image object or is provided in an invalid format,
   * the image is recoded into an PNG-Image.
   *
   * @param imageRef  the image reference.
   *
   * @return an image.
   *
   * @throws DocumentException if no PDFImageElement could be created using the given
   *         ImageReference.
   * @throws IOException if the image could not be read.
   */
  private Image getImage(final ImageReference imageRef) throws DocumentException, IOException
  {
    final Rectangle2D bounds = getBounds();
    final Rectangle2D imageBounds = imageRef.getBoundsScaled();

    try
    {
      final Rectangle2D drawArea = new Rectangle2D.Float(0, 0, (float) bounds.getWidth(),
          (float) bounds.getHeight());
      if ((imageRef.getSourceURL() != null)
          && (drawArea.contains(imageBounds))
          && isSupportedImageFormat(imageRef.getSourceURL()))
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
      final WaitingImageObserver obs = new WaitingImageObserver(imageRef.getImage());
      obs.waitImageLoaded();

      final PngEncoder encoder = new PngEncoder(imageRef.getImage());
      final byte[] data = encoder.pngEncode();
      return Image.getInstance(data);
    }

    throw new DocumentException("Neither an URL nor an Image was given to paint the graphics");
  }

  /**
   * Tests, whether the given image is one of the nativly supported image formats.
   *
   * @param url the url pointing to the image, that should be tested.
   * @return true, if the image is a JPEG or PNG file, false otherwise.
   */
  private boolean isSupportedImageFormat(final URL url)
  {
    final String file = url.getFile();
    if (StringUtil.endsWithIgnoreCase(file, ".jpg"))
    {
      return true;
    }
    if (StringUtil.endsWithIgnoreCase(file, ".jpeg"))
    {
      return true;
    }
    if (StringUtil.endsWithIgnoreCase(file, ".png"))
    {
      return true;
    }
    return false;
  }

}
