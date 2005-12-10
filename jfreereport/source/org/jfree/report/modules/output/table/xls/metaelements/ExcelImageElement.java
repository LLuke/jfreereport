/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ExcelImageElement.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.12.2005 : Initial version
 */
package org.jfree.report.modules.output.table.xls.metaelements;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.keypoint.PngEncoder;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.io.IOUtils;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.URLImageContainer;
import org.jfree.report.ImageContainer;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.content.ImageContent;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.xls.ExcelExportContext;
import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.util.Log;
import org.jfree.util.WaitingImageObserver;

/**
 * Creation-Date: 09.12.2005, 18:25:10
 *
 * @author Thomas Morgner
 */
public class ExcelImageElement extends ExcelMetaElement
{
  public ExcelImageElement(final ImageContent elementContent,
                           final ElementStyleSheet style)
  {
    super(elementContent, style);
  }

  public void applyValue(final ExcelExportContext context,
                         final HSSFCell cell)
  {
    ImageContent rawContent = (ImageContent) getContent();
    final SheetLayout currentLayout = context.getCurrentLayout();

    // the content rectangle ...
    final StrictBounds cb = rawContent.getBounds();
    final TableRectangle rectangle =
            currentLayout.getTableBounds(cb, null);
    if (rectangle == null)
    {
      // there was an error while computing the grid-position for this
      // element. Evil me...
      Log.debug("Invalid reference: I was not able to compute " +
                "the rectangle for the content.");
      return;
    }

    final int cell1x = rectangle.getX1();
    final int cell1y = rectangle.getY1();
    final int cell2x = Math.max (cell1x, rectangle.getX2() - 1);
    final int cell2y = Math.max (cell1y, rectangle.getY2() - 1);

    final long cell1width = currentLayout.getCellWidth(cell1x);
    final long cell1height = currentLayout.getRowHeight(cell1y);
    final long cell2width = currentLayout.getCellWidth(cell2x);
    final long cell2height = currentLayout.getRowHeight(cell2y);

    final long cell1xPos = currentLayout.getXPosition(cell1x);
    final long cell1yPos = currentLayout.getYPosition(cell1y);
    final long cell2xPos = currentLayout.getXPosition(cell2x);
    final long cell2yPos = currentLayout.getYPosition(cell2y);

    final int dx1 = (int) (1023 * ((cb.getX() - cell1xPos) / (double) cell1width));
    final int dy1 = (int) (255 * ((cb.getY() - cell1yPos) / (double) cell1height));
    final int dx2 = (int) (1023 * ((cb.getX() + cb.getWidth() - cell2xPos) / (double) cell2width));
    final int dy2 = (int) (255 * ((cb.getY() + cb.getHeight() - cell2yPos) / (double) cell2height));


    final HSSFClientAnchor anchor =
            new HSSFClientAnchor(dx1, dy1, dx2, dy2,
                    (short) cell1x, cell1y,
                    (short) cell2x, cell2y);
    anchor.setAnchorType(1); // Move, but don't size
    try
    {
      int pictureId = loadImage(context.getWorkbook());
      if (pictureId > 0)
        context.getPatriarch().createPicture(anchor, pictureId);

    }
    catch (IOException e)
    {
      Log.warn ("Failed to add image. Ignoring.");
    }
  }


  private int getImageFormat(final URL sourceURL)
  {
    if (sourceURL.getFile().equalsIgnoreCase(".png"))
    {
      return HSSFWorkbook.PICTURE_TYPE_PNG;
    }
    if (sourceURL.getFile().equalsIgnoreCase(".jpg") ||
            sourceURL.getFile().equalsIgnoreCase(".jpeg"))
    {
      return HSSFWorkbook.PICTURE_TYPE_JPEG;
    }
    if (sourceURL.getFile().equalsIgnoreCase(".bmp") ||
            sourceURL.getFile().equalsIgnoreCase(".ico"))
    {
      return HSSFWorkbook.PICTURE_TYPE_DIB;
    }
    return -1;
  }

  public int loadImage(final HSSFWorkbook workbook)
           throws IOException
  {
    ImageContent rawContent = (ImageContent) getContent();
    ImageContainer reference = rawContent.getContent();
    URL url = null;
    // The image has an assigned URL ...
    if (reference instanceof URLImageContainer)
    {
      final URLImageContainer urlImage = (URLImageContainer) reference;

      url = urlImage.getSourceURL();
      // if we have an source to load the image data from ..
      if (url != null)
      {
        // and the image is one of the supported image formats ...
        // we we can embedd it directly ...
        final int format = getImageFormat(urlImage.getSourceURL());
        if (format != -1 && urlImage.isLoadable())
        {
          final InputStream urlIn =
                  new BufferedInputStream
                          (urlImage.getSourceURL().openStream());
          final ByteArrayOutputStream bout = new ByteArrayOutputStream();
          IOUtils.getInstance().copyStreams(urlIn, bout);
          urlIn.close();
          bout.close();

          final byte[] data = bout.toByteArray();
          // create the image
          return workbook.addPicture(data, format);
        }
      }
    }

    if (reference instanceof LocalImageContainer)
    {
      // Check, whether the imagereference contains an AWT image.
      // if so, then we can use that image instance for the recoding
      final LocalImageContainer li = (LocalImageContainer) reference;
      Image image = li.getImage();
      if (image == null)
      {
        if (url != null)
        {
          image = ImageFactory.getInstance().createImage(url);
        }
      }
      if (image != null)
      {
        // now encode the image. We don't need to create digest data
        // for the image contents, as the image is perfectly identifyable
        // by its URL
        final byte[] data = encodeImage (image);
        return workbook.addPicture(data, HSSFWorkbook.PICTURE_TYPE_PNG);
      }
    }
    return -1;
  }


  /**
   * Encodes the given image as PNG, stores the image in the generated file and returns
   * the name of the new image file.
   *
   * @param image            the image to be encoded
   * @param createComparator true, if the image creation should be cached to avoid
   *                         duplicate images
   * @return the name of the image, never null.
   *
   * @throws IOException if an IO erro occured.
   */
  private byte[] encodeImage (final Image image)
  {
    // quick caching ... use a weak list ...
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();

    final PngEncoder encoder = new PngEncoder(image,
            PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 5);
    return encoder.pngEncode();

  }

}
