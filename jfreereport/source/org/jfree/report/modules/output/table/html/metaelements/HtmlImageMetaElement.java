/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html.metaelements;

import java.io.PrintWriter;
import java.io.IOException;
import java.awt.geom.Rectangle2D;

import org.jfree.report.content.Content;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.modules.output.table.html.HtmlFilesystem;
import org.jfree.report.modules.output.table.html.util.HtmlCharacterEntities;
import org.jfree.report.modules.output.table.html.ref.HtmlReference;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.util.Log;
import org.jfree.report.DefaultImageReference;

public class HtmlImageMetaElement extends HtmlMetaElement
{
  public HtmlImageMetaElement
          (final Content elementContent, final ElementStyleSheet style,
                  final boolean usesXHTML)
  {
    super(elementContent, style, usesXHTML);
  }

  public void write (final PrintWriter pout, final HtmlFilesystem filesystem)
  {
    final RawContent content = (RawContent) getContent();
    try
    {
      final DefaultImageReference image = (DefaultImageReference) content.getContent();
      final HtmlReference href = filesystem.createImageReference(image);
      if (href.isExternal())
      {
        pout.print("<img src=\"");
        pout.print(href.getReferenceData());
        pout.print("\" width=\"");
        final Rectangle2D bounds = image.getBoundsScaled();
        pout.write(String.valueOf((int) bounds.getWidth()));
        pout.print("\" height=\"");
        pout.write(String.valueOf((int) bounds.getHeight()));
        if (image.getSourceURL() != null)
        {
          pout.print("\" alt=\"");
          pout.print
              (HtmlCharacterEntities.getEntityParser().encodeEntities(image.getSourceURL().toString()));
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
