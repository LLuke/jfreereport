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
 * ------------------------------------
 * PropertyFileReportConfiguration.java
 * ------------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PropertyFileReportConfiguration.java,v 1.7 2003/03/04 20:29:04 taqua Exp $
 *
 * Changes
 * -------
 * 14-Jan-2003 : Initial version
 */
package com.jrefinery.report.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

/**
 * A report configuration that reads its values from the jfreereport.properties file.
 * 
 * @author Thomas Morgner
 */
public class PropertyFileReportConfiguration extends ReportConfiguration
{
  /**
   * Loads the properties stored in the given file. This method does nothing if
   * the file does not exist or is unreadable.
   *
   * @param fileName the file name of the stored properties.
   */
  public void load(String fileName)
  {
    InputStream in = this.getClass().getResourceAsStream(fileName);
    if (in != null)
    {
      try
      {
        BufferedInputStream bin = new BufferedInputStream(in);
        this.getConfiguration().load(bin);
        bin.close();
      }
      catch (IOException ioe)
      {
        Log.warn ("Unable to read global configuration", ioe);
      }
    }
    else
    {
      // Log.debug ("Report configuration file not found: " + fileName);
    }

  }

  /**
   * Default constructor.
   */
  public PropertyFileReportConfiguration()
  {
  }
}
