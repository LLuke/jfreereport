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
 * PDFExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: PrintExportResources_ru.java,v 1.3 2003/08/24 15:08:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.print.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Russian language resource for the printing export GUI.
 *
 * @author Sergey M Mozgovoi
 */
public class PrintExportResources_ru extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PrintExportResources_ru()
  {
  }

  /**
   * Returns an array of localised resources.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
      {
        {"action.page-setup.name", "\u041f\u0430\u0440\u0430\u043c\u0435\u0442\u0440\u044b " +
      "\u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b"},
        {"action.page-setup.description", "\u041f\u0430\u0440\u0430\u043c\u0435\u0442\u0440" +
      "\u044b \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b"},

        {"action.print.name", "\u041f\u0435\u0447\u0430\u0442\u044c..."},
        {"action.print.description",
         "\u041f\u0435\u0447\u0430\u0442\u044c \u043e\u0442\u0447\u0435\u0442\u0430"},

        {"error.printfailed.message",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u0435\u0447\u0430" +
      "\u0442\u0438 \u043e\u0442\u0447\u0435\u0442\u0430: {0}"},
        {"error.printfailed.title",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u0435\u0447\u0430" +
      "\u0442\u0438"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PrintExportResources.class.getName(), "ru"});
  }
}
