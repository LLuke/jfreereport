/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ---------------------------------------
 * ImageURLElementTemplateDescription.java
 * ---------------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ImageURLElementTemplateDescription.java,v 1.4 2004/05/07 14:29:23 mungady Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.templates;

import java.net.URL;

import org.jfree.report.filter.templates.ImageURLElementTemplate;
import org.jfree.util.Log;
import org.jfree.xml.Parser;

/**
 * An image URL element template description.
 *
 * @author Thomas Morgner
 */
public class ImageURLElementTemplateDescription extends AbstractTemplateDescription
{
  /**
   * Creates a new template description.
   *
   * @param name the name.
   */
  public ImageURLElementTemplateDescription (final String name)
  {
    super(name, ImageURLElementTemplate.class, false);
    setParameterDefinition("content", String.class);
  }

  /**
   * Creates an object based on this description.
   *
   * @return The object.
   */
  public Object createObject ()
  {
    final ImageURLElementTemplate t = (ImageURLElementTemplate) super.createObject();
    if (t.getBaseURL() == null)
    {
      final String baseURL = getConfig().getConfigProperty(Parser.CONTENTBASE_KEY);
      try
      {
        final URL bURL = new URL(baseURL);
        t.setBaseURL(bURL);
      }
      catch (Exception e)
      {
        Log.warn(new Log.SimpleMessage("BaseURL is invalid: ", baseURL), e);
      }
    }
    return t;
  }
}
