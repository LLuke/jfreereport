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
 * ------------------------
 * ReportConfiguration.java
 * ------------------------
 * (C)opyright 2002-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PropertyFileReportConfiguration.java,v 1.2 2003/02/02 23:43:53 taqua Exp $
 *
 * Changes
 * -------
 * 14-Jan-2003 : Initial version
 */
package com.jrefinery.report.util;

import java.io.IOException;
import java.io.InputStream;

/** A report configuration that reads its values from the jfreereport.properties file. */
public class PropertyFileReportConfiguration extends ReportConfiguration
{
  public void load(String fileName)
  {
    InputStream in = this.getClass().getResourceAsStream(fileName);
    if (in != null)
    {
      try
      {
        this.getConfiguration().load(in);
      }
      catch (IOException ioe)
      {
        Log.warn ("Unable to read global configuration", ioe);
      }
    }
    else
    {
      Log.debug ("File: " + fileName);
    }

  }

  /**
   * Default constructor.
   */
  public PropertyFileReportConfiguration()
  {
  }
}
