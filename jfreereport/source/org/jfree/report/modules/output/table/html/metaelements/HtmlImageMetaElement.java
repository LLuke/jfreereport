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
 * $Id: HtmlImageMetaElement.java,v 1.7 2005/03/03 17:07:59 taqua Exp $
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
import org.jfree.report.util.Log;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;

public class HtmlImageMetaElement extends HtmlMetaElement
{
  public HtmlImageMetaElement
          (final ImageContent elementContent, final ElementStyleSheet style,
           final boolean usesXHTML)
  {
    super(elementContent, style, usesXHTML);
  }

  public void write (final PrintWriter pout, final HtmlFilesystem filesystem)
  {
    final ImageContent content = (ImageContent) getContent();
    try
    {
      final ImageContainer image = content.getContent();
      final HtmlReference href = filesystem.createImageReference(image);
      if (href.isExternal())
      {
        final StrictBounds imageArea = content.getBounds();
        final int imageWidth = (int) StrictGeomUtility.toExternalValue(imageArea.getWidth());
        final int imageHeight = (int) StrictGeomUtility.toExternalValue(imageArea.getHeight());

        pout.print("<img src=\"");
        pout.print(href.getReferenceData());
        pout.print("\" width=\"");
        pout.write(String.valueOf(imageWidth));
        pout.print("\" height=\"");
        pout.write(String.valueOf(imageHeight));
        if (href.getReferenceData() != null)
        {
          pout.print("\" alt=\"");
          pout.print(HtmlCharacterEntities.getEntityParser().
                  encodeEntities(href.getReferenceData().toString()));
          pout.print("\"");
        }
        if (isUsesXHTML())
        {
          pout.print("\" />");
        }
        else
        {
          pout.print("\" >");
        }
      }
      else
      {
        // this must not be encoded...
        pout.print(href.getReferenceData());
      }
    }
    catch (IOException ioe)
    {
      Log.warn("Writing the image failed", ioe);
    }
  }
}
