/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * AbstractXmlDemoHandler.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.report.demo.util;

import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.Resource;

/**
 * The AbstractXmlDemoHandler helps to simplify demo reports which read their
 * report definition from an XML file.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractXmlDemoHandler extends AbstractDemoHandler
        implements XmlDemoHandler
{
  public AbstractXmlDemoHandler()
  {
  }

  protected JFreeReport parseReport() throws ReportDefinitionException
  {
    final URL in = getReportDefinitionSource();
    if (in == null)
    {
      throw new ReportDefinitionException("ReportDefinition Source is invalid");
    }

    try
    {
      ResourceManager manager = new ResourceManager();
      manager.registerDefaults();
      Resource res = manager.createDirectly(in, JFreeReport.class);
      return (JFreeReport) res.getResource();
    }
    catch(Exception e)
    {
      throw new ReportDefinitionException("Parsing failed", e);
    }
  }

  public PreviewHandler getPreviewHandler()
  {
    return new DefaultPreviewHandler(this);
  }
}
