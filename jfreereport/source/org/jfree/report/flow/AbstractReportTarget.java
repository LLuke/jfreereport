/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
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
 * $Id: AbstractReportTarget.java,v 1.7 2007/03/12 18:03:13 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.flow;

import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.report.JFreeReportInfo;
import org.jfree.report.structure.Element;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 03.07.2006, 16:31:12
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportTarget implements ReportTarget
{
  private ResourceManager resourceManager;
  private ResourceKey baseResource;
  private ReportJob reportJob;

  protected AbstractReportTarget(final ReportJob reportJob,
                                 final ResourceManager resourceManager,
                                 final ResourceKey baseResource)
  {
    if (reportJob == null)
    {
      throw new NullPointerException();
    }
    this.baseResource = baseResource;
    this.reportJob = reportJob;

    if (resourceManager == null)
    {
      this.resourceManager = new ResourceManager();
      this.resourceManager.registerDefaults();
    }
    else
    {
      this.resourceManager = resourceManager;
    }
  }

  protected NamespaceDefinition[] createDefaultNameSpaces()
  {
    return Namespaces.createFromConfig
        (reportJob.getConfiguration(), "org.jfree.report.namespaces.",
            getResourceManager());
  }

  protected ResourceManager getResourceManager()
  {
    return resourceManager;
  }

  protected ResourceKey getBaseResource()
  {
    return baseResource;
  }

  public ReportJob getReportJob()
  {
    return reportJob;
  }

  protected final String getNamespaceFromAttribute(AttributeMap attrs)
  {
    final Object attribute = attrs.getAttribute
        (JFreeReportInfo.REPORT_NAMESPACE, Element.NAMESPACE_ATTRIBUTE);
    if (attribute instanceof String)
    {
      return (String) attribute;
    }
    return JFreeReportInfo.REPORT_NAMESPACE;
  }

  protected final String getElemenTypeFromAttribute(AttributeMap attrs)
  {
    final Object attribute = attrs.getAttribute
        (JFreeReportInfo.REPORT_NAMESPACE, Element.TYPE_ATTRIBUTE);
    if (attribute instanceof String)
    {
      return (String) attribute;
    }
    return "element";
  }

  protected final boolean isElementOfType(final String uri,
                                          final String tagName,
                                          final AttributeMap attrs)
  {
    final String namespace = getNamespaceFromAttribute(attrs);
    if (ObjectUtilities.equal(namespace, uri) == false)
    {
      return false;
    }

    final String elementTagName = getElemenTypeFromAttribute(attrs);
    return ObjectUtilities.equal(tagName, elementTagName);
  }


}
