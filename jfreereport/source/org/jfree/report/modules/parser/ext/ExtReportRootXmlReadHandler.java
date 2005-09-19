/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ExtReportRootXmlReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ExtReportRootXmlReadHandler.java,v 1.3 2005/03/03 23:00:20 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.ext;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.ext.readhandlers.ReportDefinitionReadHandler;
import org.jfree.xml.FrontendDefaultHandler;
import org.jfree.xml.parser.RootXmlReadHandler;
import org.jfree.xml.util.ObjectFactory;
import org.jfree.xml.util.SimpleObjectFactory;

/**
 * The root handler for parsing the report directly.
 * <p/>
 * Docmark: This may not yet work as expected, as we reference the base-modules
 * root-handler directly.
 *
 * @author Thomas Morgner
 */
public class ExtReportRootXmlReadHandler extends RootXmlReadHandler
{
  /**
   * The object factory loader.
   */
  private ObjectFactory factoryLoader;

  /**
   * Creates a new root handler for reading {@link org.jfree.report.JFreeReport} objects
   * from XML.
   */
  public ExtReportRootXmlReadHandler ()
  {
    factoryLoader = new SimpleObjectFactory();
    setRootHandler(new ReportDefinitionReadHandler());
  }

  /**
   * Returns the object factory loader.
   *
   * @return the object factory loader.
   */
  public ObjectFactory getFactoryLoader ()
  {
    return this.factoryLoader;
  }

  /**
   * Returns the chart under construction.
   *
   * @return the chart.
   */
  public JFreeReport getReport ()
  {
    try
    {
      return (JFreeReport) getRootHandler().getObject();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * Returns a new instance of the parser.
   *
   * @return a new instance of the parser.
   */
  public FrontendDefaultHandler newInstance ()
  {
    return new ExtReportRootXmlReadHandler();
  }

}
