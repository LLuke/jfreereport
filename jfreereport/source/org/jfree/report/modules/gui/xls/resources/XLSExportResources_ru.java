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
 * XLSExportResources_ru.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: XLSExportResources_ru.java,v 1.2 2003/08/19 13:37:24 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.xls.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Russian language resource for the excel export GUI.
 * 
 * @author Sergey M Mozgovoi
 */
public class XLSExportResources_ru extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public XLSExportResources_ru()
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
        {"action.export-to-excel.name",
         "\u042d\u043a\u0441\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c " +
      "\u0432 Excel..."},
        {"action.export-to-excel.description",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 MS-Excel " +
      "\u0444\u043e\u0440\u043c\u0430\u0442\u0435"},

        {"excelexportdialog.dialogtitle",
         "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u043e\u0442\u0447\u0435\u0442\u0430 " +
      "\u0432 Excel-\u0444\u0430\u0439\u043b ..."},
        {"excelexportdialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"excelexportdialog.author", "\u0410\u0432\u0442\u043e\u0440"},
        {"excelexportdialog.title", "\u0417\u0430\u0433\u043e\u043b\u043e\u0432\u043e\u043a"},
        {"excelexportdialog.selectFile",
         "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},

        {"excelexportdialog.warningTitle",
         "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"excelexportdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"excelexportdialog.targetIsEmpty",
         "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434\u0430" +
      "\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f Excel-\u0444\u0430\u0439" +
      "\u043b\u0430."},
        {"excelexportdialog.targetIsNoFile",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435" +
      "\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e" +
      "\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"excelexportdialog.targetIsNotWritable",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435" +
      "\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441" +
      "\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441" +
      "\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"excelexportdialog.targetOverwriteTitle",
         "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c " +
      "\u0444\u0430\u0439\u043b?"},

        {"excelexportdialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"excelexportdialog.confirm",
         "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"excelexportdialog.strict-layout",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0441\u0442\u0440\u043e" +
      "\u0433\u043e\u0435 \u0442\u0430\u0431\u043b\u0438\u0447\u043d\u043e\u0435 \u0440" +
      "\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u043f\u0440\u0438 " +
      "\u044d\u043a\u0441\u043f\u043e\u0440\u0442\u0435"},

        {"error.processingfailed.title",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u0432 \u043f\u0440\u043e\u0446\u0435\u0441\u0441" +
      "\u0435 \u0441\u043e\u0437\u0434\u0430\u043d\u0438\u0438 " +
      "\u043e\u0442\u0447\u0435\u0442\u0430"},
        {"error.processingfailed.message",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043e\u0431\u0440\u0430" +
      "\u0431\u043e\u0442\u043a\u0435 \u044d\u0442\u043e\u0433\u043e \u043e\u0442\u0447\u0435" +
      "\u0442\u0430: {0}"},
        {"error.savefailed.message",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043e\u0445\u0440" +
      "\u0430\u043d\u0435\u043d\u0438\u0438 PDF \u0444\u0430\u0439\u043b\u0430: {0}"},
        {"error.savefailed.title",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043e\u0445\u0440" +
      "\u0430\u043d\u0435\u043d\u0438\u0438"},
        
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{XLSExportResources.class.getName(), "ru"});
  }
}
