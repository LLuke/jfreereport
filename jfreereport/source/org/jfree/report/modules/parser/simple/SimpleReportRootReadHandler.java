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
 * SimpleReportRootReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.simple;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.simple.readhandlers.JFreeReportReadHandler;
import org.jfree.xml.FrontendDefaultHandler;
import org.jfree.xml.parser.RootXmlReadHandler;
import org.jfree.xml.util.ObjectFactory;
import org.jfree.xml.util.SimpleObjectFactory;

public class SimpleReportRootReadHandler extends RootXmlReadHandler
{
  /**
   * The object factory loader.
   */
  private ObjectFactory factoryLoader;

  /**
   * Creates a new root handler for reading {@link JFreeReport} objects from XML.
   */
  public SimpleReportRootReadHandler ()
  {
    factoryLoader = new SimpleObjectFactory();
    setRootHandler(new JFreeReportReadHandler());
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
    return new SimpleReportRootReadHandler();
  }
}
