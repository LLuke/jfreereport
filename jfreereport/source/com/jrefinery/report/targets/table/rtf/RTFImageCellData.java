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
 * -------------------
 * RTFImageCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFImageCellData.java,v 1.4 2003/02/18 19:37:35 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.rtf;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.StringUtil;
import com.keypoint.PngEncoder;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;

public class RTFImageCellData extends RTFCellData
{
  private ImageReference image;

  public RTFImageCellData(Rectangle2D outerBounds, ImageReference image, RTFCellStyle style)
  {
    super(outerBounds, style);
    this.image = image;
  }

  public Cell getCell() throws DocumentException
  {
    try
    {
      Image iTextImage = getImage(image);
      Cell cell = new Cell();
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

  public boolean isBackground()
  {
    return false;
  }


  /**
   * Helperfunction to extract an image from an imagereference. If the image is contained as
   * java.awt.Image object only, the image is recoded into an PNG-Image.
   *
   * @param imageRef  the image reference.
   *
   * @return an image.
   *
   * @throws DocumentException if no PDFImageElement could be created using the given ImageReference.
   * @throws IOException if the image could not be read.
   */
  private Image getImage(ImageReference imageRef) throws DocumentException, IOException
  {
    Rectangle2D bounds = getBounds();
    Rectangle2D imageBounds = imageRef.getBoundsScaled();

    try
    {
      Rectangle2D drawArea = new Rectangle2D.Float (0, 0, (float) bounds.getWidth(), (float) bounds.getHeight());
      if ((imageRef.getSourceURL() != null) &&
          (drawArea.contains(imageBounds)) &&
          isSupportedImageFormat(imageRef.getSourceURL()))
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

  protected boolean isSupportedImageFormat (URL url)
  {
    String file = url.getFile();
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
