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
 * HtmlImageMetaElement.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlImageMetaElement.java,v 1.13 2005/09/07 14:25:11 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html.metaelements;

import java.io.IOException;
import java.io.PrintWriter;

import org.jfree.report.ImageContainer;
import org.jfree.report.content.ImageContent;
import org.jfree.report.modules.output.table.html.HtmlFilesystem;
import org.jfree.report.modules.output.table.html.ref.HtmlReference;
import org.jfree.report.modules.output.table.html.util.HtmlCharacterEntities;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Log;

public class HtmlImageMetaElement extends HtmlMetaElement
{
  private boolean useDevIndependentImageSizes;

  public HtmlImageMetaElement (final ImageContent elementContent,
                               final ElementStyleSheet style,
                               final boolean usesXHTML,
                               final boolean useDevIndependentImageSizes)
  {
    super(elementContent, style, usesXHTML);
    this.useDevIndependentImageSizes = useDevIndependentImageSizes;
  }

  public boolean isUseDevIndependentImageSizes ()
  {
    return useDevIndependentImageSizes;
  }

  public void write (final PrintWriter pout,
                     final HtmlFilesystem filesystem,
                     final boolean emptyCellsUseCSS)
  {
    final ImageContent content = (ImageContent) getContent();
    try
    {
      final ImageContainer image = content.getContent();
      final HtmlReference href = filesystem.createImageReference(image);
      final String referenceData = href.getReferenceData();
      if (href.isExternal())
      {
        final StrictBounds imageArea = content.getBounds();
        final int imageWidth = (int) StrictGeomUtility.toExternalValue(imageArea.getWidth());
        final int imageHeight = (int) StrictGeomUtility.toExternalValue(imageArea.getHeight());

        pout.print("<img src=\"");
        pout.print(referenceData);
        pout.print("\" style=\"width:");
        pout.write(String.valueOf(imageWidth));
        if (useDevIndependentImageSizes)
        {
          pout.print("pt; height:");
        }
        else
        {
          pout.print("px; height:");
        }
        pout.write(String.valueOf(imageHeight));
        if (useDevIndependentImageSizes)
        {
          pout.print("pt;");
        }
        else
        {
          pout.print("px;");
        }
        if (referenceData != null)
        {
          pout.print("\" alt=\"");
          pout.print(HtmlCharacterEntities.getEntityParser().
                  encodeEntities(referenceData));
          pout.print("\"");
        }
        if (isUsesXHTML())
        {
          pout.print(" />");
        }
        else
        {
          pout.print(" >");
        }
      }
      else
      {
        // this must not be encoded...
        if (emptyCellsUseCSS == false &&
            (referenceData == null || referenceData.trim().length() == 0))
        {
          pout.print("&nbsp;");
        }
        else
        {
          pout.print(referenceData);
        }
      }
    }
    catch (IOException ioe)
    {
      Log.warn("Writing the image failed", ioe);
    }
  }
}
