/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * ------------------------------------
 * AbstractReportDefinitionHandler.java
 * ------------------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractReportDefinitionHandler.java,v 1.10 2002/11/07 21:45:27 taqua Exp $
 *
 * Changes
 * -------
 * 24-Apr-2002 : Created to enable the XML-Parser to load external resources.
 * 10-May-2002 : Added helper functions to ease up the parsing.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;

/**
 * Extends the SAX-DefaultHandler with ContentBase capabilities.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportDefinitionHandler extends DefaultHandler
{

  /** Base URL for loading resources. */
  private URL contentBase;

  /**
   * the namecounter is used to create unique element names. After a name has been
   * created, the counter is increased by one.
   */
  private int nameCounter;

  /**
   * Default constructor.
   */
  public AbstractReportDefinitionHandler ()
  {
  }

  /**
   * Returns the report after the parsing is complete.
   * <P>
   * Don't call until the report is completely built or you may get unexpected results.
   *
   * @return The parsed report.
   */
  public abstract JFreeReport getReport ();

  /**
   * @return the current contentbase, or null if no contentBase is set.
   */
  public URL getContentBase ()
  {
    return contentBase;
  }

  /**
   * Sets the contentBase for this report.
   * <P>
   * The contentBase is used to resolve relative URLs and to reload the DTD and external resources
   * if needed. If no contentBase is set, no resources will be loaded and the results may be not
   * defined.
   *
   * @param url The base URL.
   */
  public void setContentBase (URL url)
  {
    this.contentBase = url;
  }

  /**
   * If a name is supplied, then this method simply returns it.  Otherwise, if name is null, then
   * a unique name is generating by appending a number to the prefix '@anonymous'.
   *
   * @param name The name.
   *
   * @return a non-null name.
   */
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
   * Returns an initialized version of the definition handler.
   *
   * @return a report definition handler.
   */
  public abstract AbstractReportDefinitionHandler getInstance ();

}
