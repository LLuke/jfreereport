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
 * PlainTextExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: PlainTextExportResources_ru.java,v 1.4 2003/08/25 14:29:30 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.plaintext.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Russian language resource for the PlainText export GUI.
 *
 * @author Sergey M Mozgovoi
 */
public class PlainTextExportResources_ru extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PlainTextExportResources_ru()
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
        {"action.export-to-plaintext.name",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043a\u0430\u043a " +
      "\u0442\u0435\u043a\u0441\u0442\u043e\u0432\u044b\u0439 \u0444\u0430\u0439\u043b ..."},
        {"action.export-to-plaintext.description",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 PlainText " +
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
      "\u0430\u043d\u0435\u043d\u0438\u0438 PlainText \u0444\u0430\u0439\u043b\u0430: {0}"},
        {"error.savefailed.title",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043e\u0445\u0440" +
      "\u0430\u043d\u0435\u043d\u0438\u0438"},

        {"plain-text-exportdialog.dialogtitle",
         "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u043e\u0442\u0447\u0435\u0442" +
      "\u0430 \u0432 \u043e\u0431\u044b\u0447\u043d\u044b\u0439 \u0442\u0435\u043a" +
      "\u0441\u0442\u043e\u0432\u044b\u0439 \u0444\u0430\u0439\u043b ..."},
        {"plain-text-exportdialog.filename",
         "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"plain-text-exportdialog.encoding",
         "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430"},
        {"plain-text-exportdialog.printer",
         "\u0422\u0438\u043f \u043f\u0440\u0438\u043d\u0442\u0435\u0440\u0430"},
        {"plain-text-exportdialog.printer.plain",
         "\u0412\u044b\u0432\u043e\u0434 \u043e\u0431\u044b\u0447\u043d\u043e\u0433\u043e " +
      "\u0442\u0435\u043a\u0441\u0442"},
        {"plain-text-exportdialog.printer.epson",
         "Epson ESC/P \u0441\u043e\u0432\u043c\u0435\u0441\u0442\u0438\u043c\u044b\u0439"},
        {"plain-text-exportdialog.printer.ibm",
         "IBM \u0441\u043e\u0432\u043c\u0435\u0441\u0442\u0438\u043c\u044b\u0439"},
        {"plain-text-exportdialog.selectFile",
         "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},

        {"plain-text-exportdialog.warningTitle",
         "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"plain-text-exportdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"plain-text-exportdialog.targetIsEmpty",
         "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434" +
      "\u0430\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f CSV-\u0444\u0430" +
      "\u0439\u043b\u0430."},
        {"plain-text-exportdialog.targetIsNoFile",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435" +
      "\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e" +
      "\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"plain-text-exportdialog.targetIsNotWritable",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440" +
      "\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441" +
      "\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441" +
      "\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"plain-text-exportdialog.targetOverwriteTitle",
         "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c " +
      "\u0444\u0430\u0439\u043b?"},

        {"plain-text-exportdialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"plain-text-exportdialog.confirm", "\u041f\u043e\u0434\u0432\u0435\u0440\u0436" +
      "\u0434\u0435\u043d\u0438\u0435"},

        {"plain-text-exportdialog.chars-per-inch",
         "cpi (\u0411\u0443\u043a\u0432 \u043d\u0430 \u0434\u044e\u0439\u043c)"},
        {"plain-text-exportdialog.lines-per-inch",
         "lpi (\u041b\u0438\u043d\u0438\u0439 \u043d\u0430 \u0434\u044e\u0439\u043c)"},
        {"plain-text-exportdialog.font-settings",
         "\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430 \u0448\u0440\u0438" +
      "\u0444\u0442\u0430"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PlainTextExportResources.class.getName(), "ru"});
  }
}
