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
 * $Id: PDFExportResources_ru.java,v 1.3 2003/08/24 15:08:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.pdf.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Russian language resource for the PDF export GUI.
 *
 * @author Sergey M Mozgovoi
 */
public class PDFExportResources_ru extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PDFExportResources_ru()
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
        {"action.save-as.name",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043a\u0430\u043a..."},

        {"action.save-as.description",
         "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 PDF " +
      "\u0444\u043e\u0440\u043c\u0430\u0442e"},

        {"file.save.pdfdescription",
         "PDF \u0434\u043e\u043a\u0443\u043c\u0435\u043d\u0442\u044b"},

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

        {"pdfsavedialog.dialogtitle", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c " +
      "\u043e\u0442\u0447\u0435\u0442 \u0432 PDF-\u0444\u0430\u0439\u043b..."},
        {"pdfsavedialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"pdfsavedialog.author", "\u0410\u0432\u0442\u043e\u0440"},
        {"pdfsavedialog.title", "\u0417\u0430\u0433\u043e\u043b\u043e\u0432\u043e\u043a"},
        {"pdfsavedialog.selectFile",
         "\u0412\u044b\u0431\u043e\u0440 \u0444\u0430\u0439\u043b\u0430"},
        {"pdfsavedialog.security",
         "\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 \u0437\u0430\u0449\u0438" +
      "\u0442\u044b \u0438 \u043a\u043e\u0434\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"},
        {"pdfsavedialog.encoding", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430"},

        {"pdfsavedialog.securityNone", "\u0411\u0435\u0437 \u0437\u0430\u0449\u0438\u0442\u044b"},
        {"pdfsavedialog.security40bit",
         "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0441 \u043f\u043e" +
      "\u043c\u043e\u0449\u044c\u044e 40-\u0431\u0438\u0442\u043d\u044b\u0445 \u043a\u043b" +
      "\u044e\u0447\u0435\u0439"},
        {"pdfsavedialog.security128bit",
         "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0441 \u043f\u043e" +
      "\u043c\u043e\u0449\u044c\u044e 128-\u0431\u0438\u0442\u043d\u044b\u0445 \u043a\u043b" +
      "\u044e\u0447\u0435\u0439"},
        {"pdfsavedialog.userpassword",
         "\u041f\u0430\u0440\u043e\u043b\u044c \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430" +
      "\u0442\u0435\u043b\u044f"},
        {"pdfsavedialog.userpasswordconfirm",
         "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"pdfsavedialog.userpasswordNoMatch",
         "\u041f\u0430\u0440\u043e\u043b\u0438 \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430" +
      "\u0442\u0435\u043b\u044f \u043d\u0435 \u0441\u043e\u0432\u043f\u0430\u0434\u0430" +
      "\u044e\u0442."},
        {"pdfsavedialog.ownerpassword",
         "\u041f\u0430\u0440\u043e\u043b\u044c \u0432\u043b\u0430\u0434\u0435\u043b\u044c" +
      "\u0446\u0430"},
        {"pdfsavedialog.ownerpasswordconfirm",
         "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"pdfsavedialog.ownerpasswordNoMatch",
         "\u041f\u0430\u0440\u043e\u043b\u0438 \u0432\u043b\u0430\u0434\u0435\u043b\u044c" +
      "\u0446\u0430 \u043d\u0435 \u0441\u043e\u0432\u043f\u0430\u0434\u0430\u044e\u0442."},

        {"pdfsavedialog.ownerpasswordEmpty",
         "\u041f\u0430\u0440\u043e\u043b\u044c \u0432\u043b\u0430\u0434\u0435\u043b\u044c" +
      "\u0446\u0430 \u043f\u0443\u0441\u0442\u043e\u0439. " +
      "\u041f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u0438 " +
      "\u043c\u043e\u0433\u0443\u0442 \u0438\u0437\u043c\u0435\u043d\u044f\u0442\u044c " +
      "\u043e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u044f " +
      "\u0437\u0430\u0449\u0438\u0442\u044b. \u041f\u0440\u043e\u0434\u043e\u043b" +
      "\u0436\u0430\u0442\u044c?"},

        {"pdfsavedialog.warningTitle", "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436" +
      "\u0434\u0435\u043d\u0438\u0435"},
        {"pdfsavedialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"pdfsavedialog.targetIsEmpty",
         "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434" +
      "\u0430\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f PDF " +
      "\u0444\u0430\u0439\u043b\u0430."},
        {"pdfsavedialog.targetIsNoFile",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a" +
      "\u0435\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f " +
      "\u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"pdfsavedialog.targetIsNotWritable",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b " +
      "\u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440" +
      "\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441" +
      "\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438" +
      "\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"pdfsavedialog.targetOverwriteTitle",
         "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c " +
      "\u0444\u0430\u0439\u043b?"},

        {"pdfsavedialog.allowCopy",
         "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u043a\u043e\u043f\u0438" +
      "\u0440\u043e\u0432\u0430\u043d\u0438\u0435"},
        {"pdfsavedialog.allowPrinting",
         "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u043f\u0435\u0447" +
      "\u0430\u0442\u044c"},
        {"pdfsavedialog.allowDegradedPrinting",
         "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0443\u0445\u0443" +
      "\u0434\u0448\u0435\u043d\u043d\u0443\u044e \u043f\u0435\u0447\u0430\u0442\u044c"},
        {"pdfsavedialog.allowScreenreader",
         "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0438\u0441\u043f\u043e" +
      "\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 Screenreader'\u043e\u0432"},
        {"pdfsavedialog.allowAssembly",
         "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c (\u043f\u0435\u0440\u0435-)" +
      "\u043a\u043e\u043c\u043f\u043e\u043d\u043e\u0432\u043a\u0443"},
        {"pdfsavedialog.allowModifyContents",
         "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0438\u0437\u043c\u0435" +
      "\u043d\u0435\u0438\u0435 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c" +
      "\u043e\u0433\u043e."},
        {"pdfsavedialog.allowModifyAnnotations",
         "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0438\u0437\u043c\u0435" +
      "\u043d\u0435\u0438\u0435 \u0430\u043d\u043d\u043e\u0442\u0430\u0446\u0438\u0439."},
        {"pdfsavedialog.allowFillIn", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c " +
      "\u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u0435  \u0444\u043e\u0440\u043c" +
      "\u0443\u043b\u044f\u0440\u043d\u044b\u0445 \u0434\u0430\u043d\u043d\u044b\u0445."},

        {"pdfsavedialog.option.noprinting",
         "\u041f\u0435\u0447\u0430\u0442\u044c \u0437\u0430\u043f\u0440\u0435\u0449" +
      "\u0435\u043d\u0430."},
        {"pdfsavedialog.option.degradedprinting",
         "\u041f\u0435\u0447\u0430\u0442\u044c \u043d\u0438\u0437\u043a\u043e\u0433\u043e " +
      "\u043a\u0430\u0447\u0435\u0441\u0442\u0432\u0430."},
        {"pdfsavedialog.option.fullprinting",
         "\u041f\u0435\u0447\u0430\u0442\u044c \u0440\u0430\u0437\u0440\u0435\u0448" +
      "\u0435\u043d\u0430."},

        {"pdfsavedialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"pdfsavedialog.confirm",
         "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "ru"});
  }
}
