/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * ---------------------------
 * AbstractReportDefinitionHandler.java
 * ---------------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * 24-Apr-2002: Created to enable the XML-Parser to load external resources.
 * 10-May-2002: Added helper functions to ease up the parsing.
 */
package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;

/**
 * Extends the SAX-DefaultHandler with ContentBase capabilities.
 */
public abstract class AbstractReportDefinitionHandler extends DefaultHandler
{
  private URL contentBase;
  private int nameCounter;

  /**
   * Empty DefaultConstructor
   */
  public AbstractReportDefinitionHandler ()
  {
  }

  /**
   * Returns the report after the parsing is complete. Don't call until the report is
   * completly build or you may get unexpected results.
   */
  public abstract JFreeReport getReport ();

  /**
   * Sets the contentBase for this report. The contentBase is used to resolve relative
   * URLs and to reload the DTD and external resources if needed. If no contentBase is
   * set, no resources will be loaded and the results may be not defined.
   */
  public void setContentBase (URL url)
  {
    this.contentBase = url;
  }

  /**
   * @return the current contentbase or null, if no contentBase is set.
   */
  public URL getContentBase ()
  {
    return contentBase;
  }

  protected String generateName (String name)
  {
    if (name == null)
    {
      nameCounter += 1;
      return "@anonymous" + Integer.toHexString (nameCounter);
    }
    return name;
  }

  /**
   * Returns a initialized version of the definition handler.
   */
  public abstract AbstractReportDefinitionHandler getInstance ();

}
