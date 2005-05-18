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
 * ExtParserModuleInit.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExtParserModuleInit.java,v 1.13 2005/03/18 13:49:39 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.parser.ext;

import java.net.URL;

import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.ModuleInitializer;
import org.jfree.report.modules.parser.base.InitialReportHandler;
import org.jfree.report.modules.parser.base.ParserEntityResolver;
import org.jfree.report.modules.parser.ext.readhandlers.ReportDefinitionReadHandler;
import org.jfree.util.ObjectUtilities;

/**
 * Performs the module initialization for the extended parser.
 *
 * @author Thomas Morgner
 */
public class ExtParserModuleInit implements ModuleInitializer
{
  /**
   * the Public ID for the extensible version of JFreeReport XML definitions.
   */
  public static final String PUBLIC_ID_EXTENDED =
          "-//JFreeReport//DTD report definition//EN//extended/version 0.8.5";
  public static final String PUBLIC_ID_EXTENDED_084 =
          "-//JFreeReport//DTD report definition//EN//extended";

  /**
   * Default Constructor.
   */
  public ExtParserModuleInit ()
  {
  }

  /**
   * Initializes the ext-parser and registers it at the parser base module.
   *
   * @throws ModuleInitializeException if an error ocurres.
   */
  public void performInit ()
          throws ModuleInitializeException
  {
    final ParserEntityResolver res = ParserEntityResolver.getDefaultResolver();

    final URL urlExtReportDTD = ObjectUtilities.getResource
            ("/org/jfree/report/modules/parser/ext/resources/extreport-085.dtd",
                    ExtParserModuleInit.class);
    res.setDTDLocation(PUBLIC_ID_EXTENDED, urlExtReportDTD);
    res.setDeprecatedDTDMessage(PUBLIC_ID_EXTENDED_084,
            "The given public identifier for the XML document is deprecated. " +
            "Please use the current document type declaration instead: \n" +
            "  <!DOCTYPE report PUBLIC \n" +
            "      \"-//JFreeReport//DTD report definition//EN//extended/version 0.8.5\"\n" +
            "      \"http://jfreereport.sourceforge.net/extreport-085.dtd\">");

    InitialReportHandler.registerHandler
            ("report-definition", ReportDefinitionReadHandler.class.getName());

  }
}
