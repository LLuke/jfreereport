/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * RTFImageMetaElement.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: RTFImageMetaElement.java,v 1.3 2005/01/25 00:15:19 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Mar 14, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.rtf.metaelements;

import java.io.IOException;
import java.net.URL;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.rtf.table.RtfCell;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.URLImageContainer;
import org.jfree.report.content.ImageContent;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.Log;
import org.jfree.report.util.StringUtil;
import org.jfree.report.util.WaitingImageObserver;
import org.jfree.report.util.geom.StrictBounds;

public class RTFImageMetaElement extends RTFMetaElement
{
  public RTFImageMetaElement (final ImageContent elementContent,
                              final ElementStyleSheet style)
  {
    super(elementContent, style);
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
   * @throws com.lowagie.text.DocumentException if no PDFImageElement could be created using the given
   *         ImageReference.
   * @throws java.io.IOException if the image could not be read.
   */
  private Image getImage(final ImageContent imageRef) throws DocumentException, IOException
  {
    final StrictBounds bounds = getBounds();
    final StrictBounds imageBounds = imageRef.getBounds();

    if (imageRef instanceof URLImageContainer)
    {
      final URLImageContainer urlImageContainer = (URLImageContainer) imageRef;
      try
      {
        final StrictBounds drawArea = new StrictBounds
                (0, 0, bounds.getWidth(), bounds.getHeight());
        if ((urlImageContainer.getSourceURL() != null)
            && (StrictBounds.contains(drawArea, imageBounds))
            && isSupportedImageFormat(urlImageContainer.getSourceURL()))
        {
          return Image.getInstance(urlImageContainer.getSourceURL());
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
    }

    if (imageRef instanceof LocalImageContainer)
    {
      final LocalImageContainer localImageContainer =
              (LocalImageContainer) imageRef;
      if (localImageContainer.getImage() != null)
      {
        final WaitingImageObserver obs =
                new WaitingImageObserver(localImageContainer.getImage());
        obs.waitImageLoaded();

        return Image.getInstance(localImageContainer.getImage(), null, false);
      }
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


  /**
   * Creates a iText TableCell with image content in it. The image is
   * recoded as PNG if necessary.
   *
   * @return the cell with the content.
   * @throws DocumentException if the cell could not be created.
   */
  public RtfCell getCell() throws DocumentException
  {
    try
    {
      final ImageContent rc = (ImageContent) getContent();
      final Image iTextImage = getImage(rc);
      final RtfCell cell = new RtfCell();
      applyAlignment(cell);
      // can't use RtfImage, as we do not have access to the RtfDocument
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

}
