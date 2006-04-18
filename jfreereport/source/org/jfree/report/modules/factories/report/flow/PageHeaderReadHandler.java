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
 * PageHeaderReadHandler.java
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
package org.jfree.report.modules.factories.report.flow;

import org.jfree.report.structure.Element;
import org.jfree.report.structure.OutOfOrderSection;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 09.04.2006, 14:57:38
 *
 * @author Thomas Morgner
 */
public class PageHeaderReadHandler extends SectionReadHandler
{
  private OutOfOrderSection outOfOrderSection;

  /**
   * Creates a new generic read handler. The given namespace and tagname can be
   * arbitary values and should not be confused with the ones provided by the
   * XMLparser itself.
   *
   * @param namespace
   * @param tagName
   */
  public PageHeaderReadHandler()
  {
    outOfOrderSection = new OutOfOrderSection();
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing() throws SAXException
  {
    super.doneParsing();
    final OutOfOrderSection outOfOrderSection = (OutOfOrderSection) getElement();
    outOfOrderSection.setPrintInflow(false);
    outOfOrderSection.setRole("page-header");
  }

  protected Element getElement()
  {
    return outOfOrderSection;
  }
}
