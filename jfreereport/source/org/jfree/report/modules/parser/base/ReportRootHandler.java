/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * ReportRootHandler.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportRootHandler.java,v 1.6 2003/08/24 15:08:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.parser.base;

import org.jfree.xml.ElementDefinitionHandler;
import org.xml.sax.SAXException;

/**
 * The report root handler provides a special initialization method
 * to resolve the definition of the current parser instance. It is
 * guaranteed that the init method is called as soon as the object
 * was created.
 *
 * @author Thomas Morgner
 */
public interface ReportRootHandler extends ElementDefinitionHandler
{
  /**
   * Initializes the report root handler and finishes the report
   * initalisation.
   *
   * @param parser the report parser to be used to generate the report.
   * @param tagName the tagname that caused the creation of this parser
   * @throws SAXException if an error occurs.
   */
  public void init(ReportParser parser, String tagName) throws SAXException;
}
