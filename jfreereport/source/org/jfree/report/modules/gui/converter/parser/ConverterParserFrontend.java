/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ConverterParserFrontend.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ConverterParserFrontend.java,v 1.4 2004/05/07 14:29:46 mungady Exp $
 *
 * Changes 
 * -------------------------
 * 25-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.converter.parser;

import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.xml.ParserFrontend;

/**
 * A parser frontend implementation that initializes the converter parser. This parser
 * will wrap around the real xml parser implementation.
 *
 * @author Thomas Morgner
 */
public class ConverterParserFrontend extends ParserFrontend
{
  /**
   * Creates a new parser frontend.
   */
  public ConverterParserFrontend ()
  {
    super(new ConverterParser(new ReportParser()));
  }
}
