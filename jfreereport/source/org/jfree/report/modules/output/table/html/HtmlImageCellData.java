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
 * ----------------------
 * HtmlImageCellData.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlImageCellData.java,v 1.5 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.PrintWriter;

import org.jfree.report.ImageReference;
import org.jfree.report.util.Log;

/**
 * A wrapper for Image content within the generated HtmlTable. The image content
 * will be inserted using a HtmlReference from the provided FileSystem.
 *
 * @author Thomas Morgner
 */
public class HtmlImageCellData extends HtmlCellData
{
  /** the imagereference used for this cell. */
  private ImageReference image;

  /**
   * Creates a new ImageCell for the given bounds and image.
   *
   * @param outerBounds the cell bounds.
   * @param image the image content.
   * @param style the assigned cell style.
   * @param useXHTML a flag indicating whether to create XHTML instead of HTML4 code.
   */
  public HtmlImageCellData(final Rectangle2D outerBounds, final ImageReference image,
                           final HtmlCellStyle style, final boolean useXHTML)
  {
    super(outerBounds, style, useXHTML);
    if (image == null)
    {
      throw new NullPointerException("Image must not be null.");
    }
    this.image = image;
  }

  /**
   * Writes the (X)HTML-Code for an Image-Content. The generated code depends
   * on the created HTMLReference of the used FileSystem.
   *
   * @param pout the print writer, which receives the generated HTML-Code.
   * @param filesystem the HTML-Filesystem used to create the ImageReference.
   * @see HtmlFilesystem#createImageReference
   */
  public void write(final PrintWriter pout, final HtmlFilesystem filesystem)
  {
    try
    {
      final HtmlReferenceData href = filesystem.createImageReference(image);
      if (href.isExternal())
      {
        pout.print("<img src=\"");
        pout.print(HtmlEncoderUtil.encodeUTF(href.getReference()));
        pout.print("\" width=\"");
        final Rectangle2D bounds = image.getBoundsScaled();
        pout.write(String.valueOf((int) bounds.getWidth()));
        pout.print("\" height=\"");
        pout.write(String.valueOf((int) bounds.getHeight()));
        if (image.getSourceURL() != null)
        {
          pout.print("\" alt=\"");
          pout.print
              (HtmlProducer.getEntityParser().encodeEntities(image.getSourceURL().toString()));
          pout.print("\"");
        }
        if (isUseXHTML())
        {
          pout.print("\" />");
        }
        else
        {
          pout.print(" >");
        }
      }
      else
      {
        pout.print(HtmlEncoderUtil.encodeUTF(href.getReference()));
      }
    }
    catch (IOException ioe)
    {
      Log.warn("Writing the image failed", ioe);
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
}
