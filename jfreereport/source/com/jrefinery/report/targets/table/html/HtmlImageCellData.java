/**
 * Date: Jan 25, 2003
 * Time: 5:53:06 AM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ImageReference;

import java.awt.geom.Rectangle2D;

public class HtmlImageCellData extends HtmlCellData
{
  private ImageReference image;

  public HtmlImageCellData(Rectangle2D outerBounds, ImageReference image, HtmlCellStyle style)
  {
    super(outerBounds, style);
    this.image = image;
  }

  public String getValue()
  {
    if (image.getSourceURL() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append("<img src=\"");
      b.append(image.getSourceURL().toExternalForm());
      b.append("\" width=\"");
      Rectangle2D bounds = image.getBoundsScaled();
      b.append(bounds.getWidth());
      b.append("\" height=\"");
      b.append(bounds.getHeight());
      b.append("\">");
      return b.toString();
    }
    return "ImageReference";
  }

  public boolean isBackground()
  {
    return false;
  }
}
