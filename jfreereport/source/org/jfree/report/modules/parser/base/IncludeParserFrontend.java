/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * IncludeParserFrontend.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: IncludeParserFrontend.java,v 1.5 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.07.2003 : Initial version
 *
 */

package org.jfree.report.modules.parser.base;

import java.io.IOException;
import java.net.URL;

import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.Parser;
import org.jfree.xml.ParserFrontend;

/**
 * A parser front end implementation to support include statements
 * in a report definition.
 *
 * @author Thomas Morgner
 */
public class IncludeParserFrontend extends ParserFrontend
{
  /**
   * Creates a new include parser frontend using the given
   * parser as configuration source.
   *
   * @param parser the base parser
   */
  public IncludeParserFrontend(final Parser parser)
  {
    super(new IncludeParser(parser));
  }

  /**
   * Starts to parse the given URL.
   *
   * @param file the source from where to parse, must point to a
   * report definition
   * @return the parsed report
   * @throws ElementDefinitionException if an parse error occured.
   * @throws IOException if an IO error occured.
   */
  public Object parse(final URL file)
      throws ElementDefinitionException, IOException
  {
    return parse(file, file);
  }
}
