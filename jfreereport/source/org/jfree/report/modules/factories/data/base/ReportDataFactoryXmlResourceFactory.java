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
 * ReportDataFactoryXmlResourceFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportDataFactoryXmlResourceFactory.java,v 1.1 2006/04/18 11:45:16 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.factories.data.base;

import org.jfree.report.ReportDataFactory;
import org.jfree.report.JFreeReportBoot;
import org.jfree.xmlns.parser.AbstractXmlResourceFactory;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 08.04.2006, 14:25:23
 *
 * @author Thomas Morgner
 */
public class ReportDataFactoryXmlResourceFactory extends AbstractXmlResourceFactory
{
  public ReportDataFactoryXmlResourceFactory()
  {
  }

  protected Configuration getConfiguration ()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig();
  }

  public Class getFactoryType()
  {
    return ReportDataFactory.class;
  }
}
