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
 * CSVExportResources_ru.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: CSVExportResources_ru.java,v 1.2 2003/08/19 13:37:23 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.csv.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Russian language resource for the CSV export GUI.
 * 
 * @author Sergey M Mozgovoi
 */
public class CSVExportResources_ru extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public CSVExportResources_ru()
  {
  }

  /**
   * Returns the array of strings in the resource bundle.
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
        {"action.export-to-csv.name",
         "\u042d\u043a\u0441\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c " +
      "\u0432 CSV..."},
        {"action.export-to-csv.description",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 CSV " +
      "\u0444\u043e\u0440\u043c\u0430\u0442\u0435"},

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

        {"csvexportdialog.dialogtitle", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u043e" +
      "\u0442\u0447\u0435\u0442\u0430 \u0432 CSV \u0444\u0430\u0439\u043b ..."},
        {"csvexportdialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"csvexportdialog.encoding", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430"},
        {"csvexportdialog.separatorchar", "\u0420\u0430\u0437\u0434\u0435\u043b\u0438\u0442" +
      "\u0435\u043b\u044c\u043d\u044b\u0439 \u0441\u0438\u043c\u0432\u043e\u043b"},
        {"csvexportdialog.selectFile", "\u0432\u044b\u0431\u0440\u0430\u0442\u044c " +
      "\u0444\u0430\u0439\u043b"},

        {"csvexportdialog.warningTitle", "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435" +
      "\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"csvexportdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"csvexportdialog.targetIsEmpty",
         "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434" +
      "\u0430\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f CSV-\u0444" +
      "\u0430\u0439\u043b\u0430."},
        {"csvexportdialog.targetIsNoFile",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435" +
      "\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e" +
      "\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"csvexportdialog.targetIsNotWritable",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440" +
      "\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441" +
      "\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438" +
      "\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"csvexportdialog.targetOverwriteTitle",
         "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c " +
      "\u0444\u0430\u0439\u043b?"},

        {"csvexportdialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"csvexportdialog.confirm",
         "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},

        {"csvexportdialog.separator.tab",
         "\u0422\u0430\u0431\u0443\u043b\u044f\u0442\u043e\u0440"},
        {"csvexportdialog.separator.colon", "\u0417\u0430\u043f\u044f\u0442\u0430\u044f (,)"},
        {"csvexportdialog.separator.semicolon",
         "\u0422\u043e\u0447\u043a\u0430 \u0441 \u0437\u0430\u043f\u044f\u0442\u043e\u0439 (;)"},
        {"csvexportdialog.separator.other", "\u041f\u0440\u043e\u0447\u0435\u0435"},

        {"csvexportdialog.exporttype",
         "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0441\u043f\u043e\u0441\u043e\u0431 " +
      "\u0434\u043b\u044f \u044d\u043a\u0441\u043f\u043e\u0440\u0442\u0430"},
        {"csvexportdialog.export.data",
         "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u0434\u0430\u043d\u043d\u044b\u0445 " +
      "\u043f\u043e-\u0441\u0442\u0440\u043e\u0447\u043d\u043e"},
        {"csvexportdialog.export.printed_elements",
         "\u041f\u0435\u0447\u0430\u0442\u044c \u044d\u043b\u0435\u043c\u0435\u043d\u0442" +
      "\u043e\u0432 (\u0420\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u043d" +
      "\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435)"},
        {"csvexportdialog.strict-layout",
         "\u041f\u043e\u0434\u0434\u0435\u0440\u0436\u043a\u0430 \u0441\u0442\u0440" +
      "\u043e\u0433\u043e\u0433\u043e \u0442\u0430\u0431\u043b\u0438\u0447\u043d" +
      "\u043e\u0433\u043e \u0440\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d" +
      "\u0438\u044f \u043f\u0440\u0438 \u044d\u043a\u0441\u043f\u043e\u0440\u0442\u0435\u044e"},

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{CSVExportResources.class.getName(), "ru"});
  }

}
