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
 * SubReport.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SubReport.java,v 1.1 2006/04/18 11:49:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.structure;

import java.util.HashMap;

import org.jfree.report.i18n.ResourceBundleFactory;

/**
 * Creation-Date: 04.03.2006, 21:38:21
 *
 * @author Thomas Morgner
 */
public class SubReport extends ReportDefinition
{
  private HashMap exportParameters;
  private HashMap inputParameters;

  private ResourceBundleFactory resourceBundleFactory;

  public SubReport()
  {
    setType("sub-report");
    exportParameters = new HashMap();
    inputParameters = new HashMap();
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    if (resourceBundleFactory == null)
    {
      Section parent = getParent();
      if (parent != null)
      {
        return parent.getReport().getResourceBundleFactory();
      }
      return null;
    }
    return resourceBundleFactory;
  }

  public void setResourceBundleFactory(final ResourceBundleFactory resourceBundleFactory)
  {
    this.resourceBundleFactory = resourceBundleFactory;
  }

  public void addExportParameter (String outerName, String innerName)
  {
    exportParameters.put(outerName, innerName);
  }

  public void removeExportParameter (String outerName)
  {
    exportParameters.remove(outerName);
  }

  public String getExportParameter (String outerName)
  {
    return (String) exportParameters.get(outerName);
  }

  public String[] getExportParameters ()
  {
    return (String[])
            exportParameters.keySet().toArray(new String[exportParameters.size()]);
  }

  public String[] getPeerExportParameters ()
  {
    return (String[])
            exportParameters.values().toArray(new String[exportParameters.size()]);
  }

  public void addInputParameter (String outerName, String innerName)
  {
    inputParameters.put(outerName, innerName);
  }

  public void removeInputParameter (String outerName)
  {
    inputParameters.remove(outerName);
  }

  public String getInnerParameter (String outerName)
  {
    return (String) inputParameters.get(outerName);
  }

  public String[] getInputParameters ()
  {
    return (String[])
            inputParameters.keySet().toArray(new String[inputParameters.size()]);
  }

  public String[] getPeerInputParameters ()
  {
    return (String[])
            inputParameters.values().toArray(new String[inputParameters.size()]);
  }

  public boolean isGlobalImport()
  {
    return "*".equals(inputParameters.get("*"));
  }

  public boolean isGlobalExport()
  {
    return "*".equals(exportParameters.get("*"));
  }
}
