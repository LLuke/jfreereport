/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * URLFilterObjectDescription.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: URLFilterObjectDescription.java,v 1.1 2003/05/16 15:28:22 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 16-05-2003 : Initial version
 *  
 */

package com.jrefinery.report.io.ext.factory.datasource;

import java.net.URL;

import com.jrefinery.report.filter.URLFilter;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.xml.factory.objects.BeanObjectDescription;

/**
 * An ObjectDescription for the URLFilterClass. This class uses either
 * an given or an preconfigured base url to construct the URL.
 */
public class URLFilterObjectDescription extends BeanObjectDescription
{
  /**
   * Creates a new object description.
   *
   * @param className  the class.
   */
  public URLFilterObjectDescription(Class className)
  {
    super(className);
    if (URLFilter.class.isInstance(className) == false)
    {
      throw new IllegalArgumentException("Given class is no instance of URLFilter.");
    }
  }

  /**
   * Creates an object based on this description.
   *
   * @return The object.
   */
  public Object createObject()
  {
    URLFilter t = (URLFilter) super.createObject();
    if (t.getBaseURL() == null)
    {
      String baseURL = getConfig().getConfigProperty(Configuration.CONTENT_BASE_KEY);
      try
      {
        URL bURL = new URL(baseURL);
        t.setBaseURL(bURL);
      }
      catch (Exception e)
      {
        Log.warn("BaseURL is invalid: ", e);
      }
    }
    return t;
  }
}
