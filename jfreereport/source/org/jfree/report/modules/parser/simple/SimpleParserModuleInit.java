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
 * $Id: SimpleParserModuleInit.java,v 1.9 2005/01/25 00:22:15 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06.07.2003 : Initial version
 *
 */

package org.jfree.report.modules.parser.simple;

import java.net.URL;

import org.jfree.report.modules.parser.base.InitialReportHandler;
import org.jfree.report.modules.parser.base.ParserEntityResolver;
import org.jfree.report.modules.parser.simple.readhandlers.JFreeReportReadHandler;
import org.jfree.base.modules.ModuleInitializer;
import org.jfree.base.modules.ModuleInitializeException;

/**
 * Handles the initalisation of the simple parser module. This contains support
 * for the simple report definition format.
 *
 * @author Thomas Morgner
 */
public class SimpleParserModuleInit implements ModuleInitializer
{

  /** the document element tag for the simple report format. */
  public static final String OLD_REPORT_TAG = "report";

  /** the Public ID for the simple version of JFreeReport XML definitions. */
  public static final String PUBLIC_ID_SIMPLE =
      "-//JFreeReport//DTD report definition//EN//simple";

  /**
   * DefaultConstructor. Does nothing.
   */
  public SimpleParserModuleInit()
  {
  }

  /**
   * Initializes the simple parser and registers this handler with the parser
   * base module.
   *
   * @throws ModuleInitializeException if initializing the module failes.
   */
  public void performInit() throws ModuleInitializeException
  {
    final ParserEntityResolver res = ParserEntityResolver.getDefaultResolver();

    final URL urlReportDTD = res.getClass().getResource
        ("/org/jfree/report/modules/parser/simple/resources/report.dtd");
    res.setDTDLocation(PUBLIC_ID_SIMPLE, urlReportDTD);

    InitialReportHandler.registerHandler
            (OLD_REPORT_TAG, JFreeReportReadHandler.class.getName());
  }
}
