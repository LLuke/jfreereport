/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * JFreeReportXmlResourceFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.factories.report.base;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.factories.common.AbstractXmlResourceFactory;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 08.04.2006, 14:27:36
 *
 * @author Thomas Morgner
 */
public class JFreeReportXmlResourceFactory extends AbstractXmlResourceFactory
{
  public JFreeReportXmlResourceFactory()
  {
  }

  public Class getFactoryType()
  {
    return JFreeReport.class;
  }

  protected Object finishResult(final Object res,
                                final ResourceManager manager,
                                final ResourceData data,
                                final ResourceKey context)
          throws ResourceCreationException, ResourceLoadingException
  {
    final JFreeReport report = (JFreeReport) res;
    if (report == null)
    {
      throw new ResourceCreationException("Report has not been parsed.");
    }
    if (context != null)
    {
      report.setBaseResource(context);
    }
    else
    {
      report.setBaseResource(data.getKey());
    }
    report.setResourceManager(manager);

    return report;
  }
}
