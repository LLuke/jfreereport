/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 06.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.simple;

import java.net.URL;

import org.jfree.report.modules.parser.base.ParserEntityResolver;

public class SimpleParserModuleInit implements Runnable
{
  /** the Public ID for the simple version of JFreeReport XML definitions. */
  public static final String PUBLIC_ID_SIMPLE =
      "-//JFreeReport//DTD report definition//EN//simple";

  public SimpleParserModuleInit()
  {
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used
   * to create a thread, starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may
   * take any action whatsoever.
   *
   * @see     Thread#run()
   */
  public void run()
  {
    ParserEntityResolver res = ParserEntityResolver.getDefaultResolver();

    final URL urlReportDTD = res.getClass().getResource
        ("/org/jfree/report/modules/parser/simple/resources/report.dtd");
    res.setDTDLocation(PUBLIC_ID_SIMPLE, urlReportDTD);
  }
}
