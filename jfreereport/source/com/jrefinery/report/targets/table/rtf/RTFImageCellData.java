/**
 * Date: Jan 25, 2003
 * Time: 5:53:06 AM
 *
 * $Id: RTFImageCellData.java,v 1.1 2003/02/01 22:10:37 taqua Exp $
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
   * @throws DocumentException ??
   * @throws java.io.IOException ??
   */
  private Image getImage(ImageReference imageRef) throws DocumentException, IOException
  {
    Rectangle2D bounds = getBounds();
    Rectangle2D imageBounds = imageRef.getBoundsScaled();

    try
    {
      Rectangle2D drawArea = new Rectangle2D.Double (0, 0, bounds.getWidth(), bounds.getHeight());
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
