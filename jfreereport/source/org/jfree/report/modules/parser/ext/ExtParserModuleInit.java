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
 * $Id: ExtParserModuleInit.java,v 1.5 2003/08/20 17:24:35 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 06-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.ext;

import java.net.URL;

import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.modules.ModuleInitializer;
import org.jfree.report.modules.parser.base.InitialReportHandler;
import org.jfree.report.modules.parser.base.ParserEntityResolver;

/**
 * Performs the module initialization for the extended parser.
 * 
 * @author Thomas Morgner
 */
public class ExtParserModuleInit implements ModuleInitializer
{
  /** the document element tag for the extended report format. */
  public static final String REPORT_DEFINITION_TAG = "report-definition";

  /** the Public ID for the extensible version of JFreeReport XML definitions. */
  public static final String PUBLIC_ID_EXTENDED =
      "-//JFreeReport//DTD report definition//EN//extended";

  /**
   * Default Constructor.
   */
  public ExtParserModuleInit()
  {
  }

  /**
   * Initializes the ext-parser and registers it at the parser base module. 
   * @see org.jfree.report.modules.ModuleInitializer#performInit()
   * 
   * @throws ModuleInitializeException if an error ocurres.
   */
  public void performInit() throws ModuleInitializeException
  {
    ParserEntityResolver res = ParserEntityResolver.getDefaultResolver();

    final URL urlExtReportDTD = res.getClass().getResource(
        "/org/jfree/report/modules/parser/ext/resources/extreport.dtd");
    res.setDTDLocation(PUBLIC_ID_EXTENDED, urlExtReportDTD);

    InitialReportHandler.registerHandler(REPORT_DEFINITION_TAG, ExtReportHandler.class.getName());

  }
}
