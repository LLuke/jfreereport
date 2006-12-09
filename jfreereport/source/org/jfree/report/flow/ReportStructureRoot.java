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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.flow;

import java.util.Locale;

import org.jfree.report.ReportDataFactory;
import org.jfree.report.util.ReportParameters;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 08.12.2006
 * Time: 14:53:30
 * To change this template use File | Settings | File Templates.
 */
public interface ReportStructureRoot
{
  public ReportDataFactory getDataFactory();

  public ReportParameters getInputParameters();

  public Configuration getConfiguration();

  public Locale getLocale();

  public ResourceManager getResourceManager();

  public ResourceKey getBaseResource();
}
