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
 * HtmlImageCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlImageCellData.java,v 1.3 2003/02/02 23:43:52 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.PrintWriter;

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
