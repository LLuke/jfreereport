/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * ReportDefinitionException.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 24-Apr-2002 : Initial version
 */
package com.jrefinery.report.io;

import org.xml.sax.SAXException;

/**
 * A reportdefinition exception is thrown when the parsing of the report definition
 * failed because invalid or missing attributes are encountered.
 */
public class ReportDefinitionException extends SAXException
{
  private Exception parent;

  public ReportDefinitionException (String message)
  {
    super (message);
  }

  public ReportDefinitionException (Exception e)
  {
    this (e, e.getMessage ());
  }

  public ReportDefinitionException (Exception e, String message)
  {
    this (message);
    e.printStackTrace();
    parent = e;
  }

  public Exception getParentException ()
  {
    return parent;
  }



}
