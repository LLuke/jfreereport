/**
 * Date: Jan 25, 2003
 * Time: 5:53:06 AM
 *
 * $Id: HtmlImageCellData.java,v 1.1 2003/01/25 20:38:34 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.io.IOException;

public class HtmlImageCellData extends HtmlCellData
{
  private ImageReference image;

  public HtmlImageCellData(Rectangle2D outerBounds, ImageReference image, HtmlCellStyle style, boolean useXHTML)
  {
    super(outerBounds, style, useXHTML);
    this.image = image;
  }

  public void write(PrintWriter pout, HtmlFilesystem filesystem)
  {
    try
    {
      HtmlReferenceData href = filesystem.createImageReference(image);
      if (href.isExternal())
      {
        StringBuffer b = new StringBuffer();
        b.append("<img src=\"");
        b.append(href.getReference());
        b.append("\" width=\"");
        Rectangle2D bounds = image.getBoundsScaled();
        b.append((int) bounds.getWidth());
        b.append("\" height=\"");
        b.append((int) bounds.getHeight());
        if (image.getSourceURL() != null)
        {
          b.append("\" alt=\"");
          b.append(image.getSourceURL());
        }
        if (isUseXHTML())
          b.append("\" />");
        else
          b.append("/>");

        pout.write(b.toString());
      }
      else
      {
        pout.write(href.getReference());
      }
    }
    catch (IOException ioe)
    {
      Log.warn ("Writing the image failed" , ioe);
    }
  }

  public boolean isBackground()
  {
    return false;
  }
}
