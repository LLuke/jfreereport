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
 * HtmlExportResources.java
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
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.html.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class HtmlExportResources_ru extends JFreeReportResources
{
  public HtmlExportResources_ru()
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
        {"action.export-to-html.name",
         "\u042d\u043a\u0441\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c " +
      "\u0432 HTML..."},
        {"action.export-to-html.description",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 HTML " +
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
        {"error.validationfailed.message",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u0432 \u043f\u0440\u043e\u0446\u0435\u0441" +
      "\u0441\u0435 \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438" +
      "\u044f \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u0438, \u0432\u0432\u043e" +
      "\u0434\u0438\u043c\u043e\u0439 \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442" +
      "\u0435\u043b\u0435\u043c."},
        {"error.validationfailed.title",
         "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u043e\u0434\u0442" +
      "\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0438"},

        {"htmlexportdialog.dialogtitle", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 " +
      "\u043e\u0442\u0447\u0435\u0442\u0430 \u0432 HTML-\u0444\u0430\u0439\u043b ..."},

        {"htmlexportdialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"htmlexportdialog.datafilename",
         "\u0414\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044f \u0441 \u0434\u0430" +
      "\u043d\u043d\u044b\u043c\u0438"},
        {"htmlexportdialog.copy-external-references",
         "\u041a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432\u043d\u0435\u0448" +
      "\u043d\u0438\u0435 \u0441\u0441\u044b\u043b\u043a\u0438"},

        {"htmlexportdialog.author", "\u0410\u0432\u0442\u043e\u0440"},
        {"htmlexportdialog.title", "\u0417\u0430\u0433\u043e\u043b\u043e\u0432\u043e\u043a"},
        {"htmlexportdialog.encoding", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430"},
        {"htmlexportdialog.selectZipFile",
         "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},
        {"htmlexportdialog.selectStreamFile",
         "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},
        {"htmlexportdialog.selectDirFile",
         "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},

        {"htmlexportdialog.strict-layout",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0441\u0442\u0440\u043e\u0433" +
      "\u043e\u0435 \u0442\u0430\u0431\u043b\u0438\u0447\u043d\u043e\u0435 \u0440\u0430\u0441" +
      "\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u043f\u0440\u0438 \u044d\u043a" +
      "\u0441\u043f\u043e\u0440\u0442\u0435"},
        {"htmlexportdialog.generate-xhtml",
         "\u0413\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c XHTML 1.0 " +
      "\u0432\u044b\u0445\u043e\u0434\u043d\u043e\u0439 \u0444\u043e\u0440\u043c\u0430\u0442"},
        {"htmlexportdialog.generate-html4",
         "\u0413\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c HTML 4.0 " +
      "\u0432\u044b\u0445\u043e\u0434\u043d\u043e\u0439 \u0444\u043e\u0440\u043c\u0430\u0442"},

        {"htmlexportdialog.warningTitle",
         "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"htmlexportdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"htmlexportdialog.targetIsEmpty",
         "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434\u0430" +
      "\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f HTML-\u0444\u0430\u0439" +
      "\u043b\u0430."},
        {"htmlexportdialog.targetIsNoFile",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435" +
      "\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e" +
      "\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"htmlexportdialog.targetIsNotWritable",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435" +
      "\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441" +
      "\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441" +
      "\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"htmlexportdialog.targetOverwriteTitle",
         "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0444" +
      "\u0430\u0439\u043b?"},

        {"htmlexportdialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"htmlexportdialog.confirm",
         "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "\u041f\u0443\u0442\u044c \u043a \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e" +
      "\u043c\u0443 \u043e\u0431\u044a\u0435\u043a\u0442\u0443 \u0443\u043a\u0430\u0437" +
      "\u044b\u0432\u0430\u0435\u0442 \u043d\u0430 \u043f\u0443\u0441\u0442\u0443\u044e " +
      "\u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044e.\n" +
      "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430 \u0432\u0432\u0435\u0434" +
      "\u0438\u0442\u0435 \u043f\u0443\u0442\u044c \u043a \u0434\u0438\u0440\u0435\u043a" +
      "\u0442\u043e\u0440\u0438\u0438 \u0441 \u0434\u0430\u043d\u043d\u044b\u043c\u0438 " +
      "\u043e\u0442\u043d\u043e\u0441\u0438\u0442\u0435\u043b\u044c\u043d\u043e ZIP \u0444" +
      "\u0430\u0439\u043b\u0430."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u0430\u044f \u0434\u0438\u0440\u0435" +
      "\u043a\u0442\u043e\u0440\u0438\u044f \u0441 \u0434\u0430\u043d\u043d\u044b\u043c\u0438 " +
      "\u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442\n"
      + "\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u043d\u0435\u0434\u043e\u0441\u0442" +
      "\u0430\u044e\u0449\u0438\u0435 \u043f\u043e\u0434\u0434\u0438\u0440\u0435\u043a\u0442" +
      "\u043e\u0440\u0438\u0438?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "\u0421\u043e\u0437\u0434\u0430\u0442" +
      "\u044c \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044e \u0434\u043b\u044f " +
      "\u0434\u0430\u043d\u043d\u044b\u0445?"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{HtmlExportResources.class.getName(), "ru"});
  }
}
