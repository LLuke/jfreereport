/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------------------
 * JFreeReportResources_ru.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_fr.java,v 1.6 2003/05/16 13:24:41 taqua Exp $
 *
 *
 */
package com.jrefinery.report.resources;

/**
 * Russian Language Resources.
 *
 * @author Sergey M Mozgovoi
 */
public class JFreeReportResources_ru extends JFreeReportResources
{

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
        {"action.save-as.name", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043a\u0430\u043a..."},
        {"action.save-as.description", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 PDF \u0444\u043e\u0440\u043c\u0430\u0442e"},

        {"action.export-to-excel.name", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432 Excel..."},
        {"action.export-to-excel.description", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 MS-Excel \u0444\u043e\u0440\u043c\u0430\u0442\u0435"},

        {"action.export-to-html.name", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432 HTML..."},
        {"action.export-to-html.description", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 HTML \u0444\u043e\u0440\u043c\u0430\u0442\u0435"},

        {"action.export-to-csv.name", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432 CSV..."},
        {"action.export-to-csv.description", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 CSV \u0444\u043e\u0440\u043c\u0430\u0442\u0435"},

        {"action.export-to-plaintext.name", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043a\u0430\u043a \u0442\u0435\u043a\u0441\u0442\u043e\u0432\u044b\u0439 \u0444\u0430\u0439\u043b ..."},
        {"action.export-to-plaintext.description", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432 PlainText \u0444\u043e\u0440\u043c\u0430\u0442\u0435"},

        {"action.page-setup.name", "\u041f\u0430\u0440\u0430\u043c\u0435\u0442\u0440\u044b \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b"},
        {"action.page-setup.description", "\u041f\u0430\u0440\u0430\u043c\u0435\u0442\u0440\u044b \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b"},

        {"action.print.name", "\u041f\u0435\u0447\u0430\u0442\u044c..."},
        {"action.print.description", "\u041f\u0435\u0447\u0430\u0442\u044c \u043e\u0442\u0447\u0435\u0442\u0430"},

        {"action.close.name", "\u0417\u0430\u043a\u0440\u044b\u0442\u044c"},
        {"action.close.description", "\u0412\u044b\u0439\u0442\u0438 \u0438\u0437 \u043e\u043a\u043d\u0430 \u043f\u0440\u0435\u0434\u0432\u0430\u0440\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0441\u043c\u043e\u0442\u0440\u0430"},

        {"action.gotopage.name", "\u041f\u0435\u0440\u0435\u0439\u0442\u0438 \u043d\u0430 \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443..."},
        {"action.gotopage.description", "\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440 \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b \u043d\u0435\u043f\u043e\u0441\u0440\u0435\u0434\u0441\u0442\u0432\u0435\u043d\u043d\u043e"},

        {"dialog.gotopage.message", "\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u043e\u043c\u0435\u0440 \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b"},
        {"dialog.gotopage.title", "\u041f\u0435\u0440\u0435\u0439\u0442\u0438 \u043d\u0430 \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443"},

        {"action.about.name", "\u041e \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0435..."},
        {"action.about.description", "\u0418\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044f \u043e \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u0438"},

        {"action.firstpage.name", "\u041d\u0430\u0447\u0430\u043b\u043e"},
        {"action.firstpage.description", "\u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f \u043d\u0430 \u043f\u0435\u0440\u0432\u0443\u044e \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443"},

        {"action.back.name", "\u041d\u0430\u0437\u0430\u0434"},
        {"action.back.description", "\u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f \u043d\u0430 \u043f\u0440\u0435\u0434\u044b\u0434\u0443\u0449\u0435\u044e \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443"},

        {"action.forward.name", "\u0412\u043f\u0435\u0440\u0435\u0434"},
        {"action.forward.description", "\u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f \u043d\u0430 \u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0435\u044e \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443"},

        {"action.lastpage.name", "\u041a\u043e\u043d\u0435\u0446"},
        {"action.lastpage.description", "\u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f \u043d\u0430 \u043f\u043e\u0441\u043b\u0435\u0434\u043d\u044e\u044e \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443"},

        {"action.zoomIn.name", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0442\u044c \u043c\u0430\u0441\u0448\u0442\u0430\u0431"},
        {"action.zoomIn.description", "\u0423\u0432\u0435\u043b\u0438\u0447\u0435\u043d\u0438\u0435 \u043c\u0430\u0441\u0448\u0442\u0430\u0431\u0430"},

        {"action.zoomOut.name", "\u0423\u043c\u0435\u043d\u044c\u0448\u0438\u0442\u044c \u043c\u0430\u0441\u0448\u0442\u0430\u0431"},
        {"action.zoomOut.description", "\u0423\u043c\u0435\u043d\u044c\u0448\u0435\u043d\u0438\u0435 \u043c\u0430\u0441\u0448\u0442\u0430\u0431\u0430"},

        {"preview-frame.title", "\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440 \u043f\u0435\u0440\u0435\u0434 \u043f\u0435\u0447\u0430\u0442\u044c\u044e"},

        {"menu.file.name", "\u0424\u0430\u0439\u043b"},
        {"menu.navigation.name", "\u041d\u0430\u0432\u0438\u0433\u0430\u0446\u0438\u044f"},
        {"menu.zoom.name", "\u041c\u0430\u0441\u0448\u0442\u0430\u0431"},

        {"menu.help.name", "\u0421\u043f\u0440\u0430\u0432\u043a\u0430"},

        {"file.save.pdfdescription", "PDF \u0434\u043e\u043a\u0443\u043c\u0435\u043d\u0442\u044b"},
        {"statusline.pages", "\u0421\u0442\u0440\u0430\u043d\u0438\u0446\u0430 {0} \u0438\u0437 {1}"},
        {"statusline.error", "\u0413\u0435\u043d\u0435\u0440\u0430\u0442\u043e\u0440 \u043e\u0442\u0447\u0435\u0442\u043e\u0432 \u0432\u044b\u0434\u0430\u043b \u043e\u0448\u0438\u0431\u043a\u0443: {0}"},
        {"statusline.repaginate", "\u0420\u0430\u0441\u0447\u0435\u0442 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u0430 \u0441\u0442\u0440\u0430\u043d\u0438\u0446, \u043f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430 \u043f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435."},
        {"error.processingfailed.title", "\u041e\u0448\u0438\u0431\u043a\u0430 \u0432 \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u0435 \u0441\u043e\u0437\u0434\u0430\u043d\u0438\u0438 \u043e\u0442\u0447\u0435\u0442\u0430"},
        {"error.processingfailed.message", "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043e\u0431\u0440\u0430\u0431\u043e\u0442\u043a\u0435 \u044d\u0442\u043e\u0433\u043e \u043e\u0442\u0447\u0435\u0442\u0430: {0}"},
        {"error.savefailed.message", "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u0438 PDF \u0444\u0430\u0439\u043b\u0430: {0}"},
        {"error.savefailed.title", "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u0438"},
        {"error.printfailed.message", "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u0435\u0447\u0430\u0442\u0438 \u043e\u0442\u0447\u0435\u0442\u0430: {0}"},
        {"error.printfailed.title", "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u0435\u0447\u0430\u0442\u0438"},
        {"error.validationfailed.message", "\u041e\u0448\u0438\u0431\u043a\u0430 \u0432 \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u0435 \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u044f \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u0438, \u0432\u0432\u043e\u0434\u0438\u043c\u043e\u0439 \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u0435\u043c."},
        {"error.validationfailed.title", "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0438"},

        {"tabletarget.page", "\u0421\u0442\u0440\u0430\u043d\u0438\u0446\u0430 {0}"},

        {"pdfsavedialog.dialogtitle", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043e\u0442\u0447\u0435\u0442 \u0432 PDF-\u0444\u0430\u0439\u043b..."},
        {"pdfsavedialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"pdfsavedialog.author", "\u0410\u0432\u0442\u043e\u0440"},
        {"pdfsavedialog.title", "\u0417\u0430\u0433\u043e\u043b\u043e\u0432\u043e\u043a"},
        {"pdfsavedialog.selectFile", "\u0412\u044b\u0431\u043e\u0440 \u0444\u0430\u0439\u043b\u0430"},
        {"pdfsavedialog.security", "\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 \u0437\u0430\u0449\u0438\u0442\u044b \u0438 \u043a\u043e\u0434\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"},
        {"pdfsavedialog.encoding", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430"},

        {"pdfsavedialog.securityNone", "\u0411\u0435\u0437 \u0437\u0430\u0449\u0438\u0442\u044b"},
        {"pdfsavedialog.security40bit", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0441 \u043f\u043e\u043c\u043e\u0449\u044c\u044e 40-\u0431\u0438\u0442\u043d\u044b\u0445 \u043a\u043b\u044e\u0447\u0435\u0439"},
        {"pdfsavedialog.security128bit", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0441 \u043f\u043e\u043c\u043e\u0449\u044c\u044e 128-\u0431\u0438\u0442\u043d\u044b\u0445 \u043a\u043b\u044e\u0447\u0435\u0439"},
        {"pdfsavedialog.userpassword", "\u041f\u0430\u0440\u043e\u043b\u044c \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u044f"},
        {"pdfsavedialog.userpasswordconfirm", "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"pdfsavedialog.userpasswordNoMatch", "\u041f\u0430\u0440\u043e\u043b\u0438 \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u044f \u043d\u0435 \u0441\u043e\u0432\u043f\u0430\u0434\u0430\u044e\u0442."},
        {"pdfsavedialog.ownerpassword", "\u041f\u0430\u0440\u043e\u043b\u044c \u0432\u043b\u0430\u0434\u0435\u043b\u044c\u0446\u0430"},
        {"pdfsavedialog.ownerpasswordconfirm", "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"pdfsavedialog.ownerpasswordNoMatch", "\u041f\u0430\u0440\u043e\u043b\u0438 \u0432\u043b\u0430\u0434\u0435\u043b\u044c\u0446\u0430 \u043d\u0435 \u0441\u043e\u0432\u043f\u0430\u0434\u0430\u044e\u0442."},

        {"pdfsavedialog.ownerpasswordEmpty", "\u041f\u0430\u0440\u043e\u043b\u044c \u0432\u043b\u0430\u0434\u0435\u043b\u044c\u0446\u0430 \u043f\u0443\u0441\u0442\u043e\u0439. " +
      "\u041f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u0438 \u043c\u043e\u0433\u0443\u0442 \u0438\u0437\u043c\u0435\u043d\u044f\u0442\u044c \u043e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u044f \u0437\u0430\u0449\u0438\u0442\u044b. \u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0430\u0442\u044c?"},

        {"pdfsavedialog.warningTitle", "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"pdfsavedialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"pdfsavedialog.targetIsEmpty", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434\u0430\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f PDF \u0444\u0430\u0439\u043b\u0430."},
        {"pdfsavedialog.targetIsNoFile", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"pdfsavedialog.targetIsNotWritable", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"pdfsavedialog.targetOverwriteConfirmation", "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"pdfsavedialog.targetOverwriteTitle", "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0444\u0430\u0439\u043b?"},

        {"pdfsavedialog.allowCopy", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u043a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"},
        {"pdfsavedialog.allowPrinting", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u043f\u0435\u0447\u0430\u0442\u044c"},
        {"pdfsavedialog.allowDegradedPrinting", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0443\u0445\u0443\u0434\u0448\u0435\u043d\u043d\u0443\u044e \u043f\u0435\u0447\u0430\u0442\u044c"},
        {"pdfsavedialog.allowScreenreader", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 Screenreader'\u043e\u0432"},
        {"pdfsavedialog.allowAssembly", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c (\u043f\u0435\u0440\u0435-)\u043a\u043e\u043c\u043f\u043e\u043d\u043e\u0432\u043a\u0443"},
        {"pdfsavedialog.allowModifyContents", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0438\u0437\u043c\u0435\u043d\u0435\u0438\u0435 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u0433\u043e."},
        {"pdfsavedialog.allowModifyAnnotations", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0438\u0437\u043c\u0435\u043d\u0435\u0438\u0435 \u0430\u043d\u043d\u043e\u0442\u0430\u0446\u0438\u0439."},
        {"pdfsavedialog.allowFillIn", "\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044c \u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u0435  \u0444\u043e\u0440\u043c\u0443\u043b\u044f\u0440\u043d\u044b\u0445 \u0434\u0430\u043d\u043d\u044b\u0445."},

        {"pdfsavedialog.option.noprinting", "\u041f\u0435\u0447\u0430\u0442\u044c \u0437\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u0430."},
        {"pdfsavedialog.option.degradedprinting", "\u041f\u0435\u0447\u0430\u0442\u044c \u043d\u0438\u0437\u043a\u043e\u0433\u043e \u043a\u0430\u0447\u0435\u0441\u0442\u0432\u0430."},
        {"pdfsavedialog.option.fullprinting", "\u041f\u0435\u0447\u0430\u0442\u044c \u0440\u0430\u0437\u0440\u0435\u0448\u0435\u043d\u0430."},

        {"pdfsavedialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"pdfsavedialog.confirm", "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},

        {"excelexportdialog.dialogtitle", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u043e\u0442\u0447\u0435\u0442\u0430 \u0432 Excel-\u0444\u0430\u0439\u043b ..."},
        {"excelexportdialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"excelexportdialog.author", "\u0410\u0432\u0442\u043e\u0440"},
        {"excelexportdialog.title", "\u0417\u0430\u0433\u043e\u043b\u043e\u0432\u043e\u043a"},
        {"excelexportdialog.selectFile", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},

        {"excelexportdialog.warningTitle", "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"excelexportdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"excelexportdialog.targetIsEmpty", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434\u0430\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f Excel-\u0444\u0430\u0439\u043b\u0430."},
        {"excelexportdialog.targetIsNoFile", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"excelexportdialog.targetIsNotWritable", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"excelexportdialog.targetOverwriteTitle", "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0444\u0430\u0439\u043b?"},

        {"excelexportdialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"excelexportdialog.confirm", "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"excelexportdialog.strict-layout", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0441\u0442\u0440\u043e\u0433\u043e\u0435 \u0442\u0430\u0431\u043b\u0438\u0447\u043d\u043e\u0435 \u0440\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u043f\u0440\u0438 \u044d\u043a\u0441\u043f\u043e\u0440\u0442\u0435"},

        {"htmlexportdialog.dialogtitle", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u043e\u0442\u0447\u0435\u0442\u0430 \u0432 HTML-\u0444\u0430\u0439\u043b ..."},

        {"htmlexportdialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"htmlexportdialog.datafilename", "\u0414\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044f \u0441 \u0434\u0430\u043d\u043d\u044b\u043c\u0438"},
        {"htmlexportdialog.copy-external-references", "\u041a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432\u043d\u0435\u0448\u043d\u0438\u0435 \u0441\u0441\u044b\u043b\u043a\u0438"},

        {"htmlexportdialog.author", "\u0410\u0432\u0442\u043e\u0440"},
        {"htmlexportdialog.title", "\u0417\u0430\u0433\u043e\u043b\u043e\u0432\u043e\u043a"},
        {"htmlexportdialog.encoding", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430"},
        {"htmlexportdialog.selectZipFile", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},
        {"htmlexportdialog.selectStreamFile", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},
        {"htmlexportdialog.selectDirFile", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},

        {"htmlexportdialog.strict-layout", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0441\u0442\u0440\u043e\u0433\u043e\u0435 \u0442\u0430\u0431\u043b\u0438\u0447\u043d\u043e\u0435 \u0440\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u043f\u0440\u0438 \u044d\u043a\u0441\u043f\u043e\u0440\u0442\u0435"},
        {"htmlexportdialog.generate-xhtml", "\u0413\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c XHTML 1.0 \u0432\u044b\u0445\u043e\u0434\u043d\u043e\u0439 \u0444\u043e\u0440\u043c\u0430\u0442"},
        {"htmlexportdialog.generate-html4", "\u0413\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c HTML 4.0 \u0432\u044b\u0445\u043e\u0434\u043d\u043e\u0439 \u0444\u043e\u0440\u043c\u0430\u0442"},

        {"htmlexportdialog.warningTitle", "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"htmlexportdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"htmlexportdialog.targetIsEmpty", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434\u0430\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f HTML-\u0444\u0430\u0439\u043b\u0430."},
        {"htmlexportdialog.targetIsNoFile", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"htmlexportdialog.targetIsNotWritable", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"htmlexportdialog.targetOverwriteTitle", "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0444\u0430\u0439\u043b?"},

        {"htmlexportdialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"htmlexportdialog.confirm", "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "\u041f\u0443\u0442\u044c \u043a \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u043c\u0443 \u043e\u0431\u044a\u0435\u043a\u0442\u0443 \u0443\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043d\u0430 \u043f\u0443\u0441\u0442\u0443\u044e \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044e.\n"
      + "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430 \u0432\u0432\u0435\u0434\u0438\u0442\u0435 \u043f\u0443\u0442\u044c \u043a \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u0438 \u0441 \u0434\u0430\u043d\u043d\u044b\u043c\u0438 \u043e\u0442\u043d\u043e\u0441\u0438\u0442\u0435\u043b\u044c\u043d\u043e ZIP \u0444\u0430\u0439\u043b\u0430."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "The specified data directory is not valid."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u0430\u044f \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044f \u0441 \u0434\u0430\u043d\u043d\u044b\u043c\u0438 \u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442\n"
      + "\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u044e\u0449\u0438\u0435 \u043f\u043e\u0434\u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u0438?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u0434\u0438\u0440\u0435\u043a\u0442\u043e\u0440\u0438\u044e \u0434\u043b\u044f \u0434\u0430\u043d\u043d\u044b\u0445?"},

        {"csvexportdialog.dialogtitle", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u043e\u0442\u0447\u0435\u0442\u0430 \u0432 CSV \u0444\u0430\u0439\u043b ..."},
        {"csvexportdialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"csvexportdialog.encoding", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430"},
        {"csvexportdialog.separatorchar", "\u0420\u0430\u0437\u0434\u0435\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u0439 \u0441\u0438\u043c\u0432\u043e\u043b"},
        {"csvexportdialog.selectFile", "\u0432\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},

        {"csvexportdialog.warningTitle", "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"csvexportdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"csvexportdialog.targetIsEmpty", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434\u0430\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f CSV-\u0444\u0430\u0439\u043b\u0430."},
        {"csvexportdialog.targetIsNoFile", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"csvexportdialog.targetIsNotWritable", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"csvexportdialog.targetOverwriteTitle", "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0444\u0430\u0439\u043b?"},

        {"csvexportdialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"csvexportdialog.confirm", "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},

        {"csvexportdialog.separator.tab", "\u0422\u0430\u0431\u0443\u043b\u044f\u0442\u043e\u0440"},
        {"csvexportdialog.separator.colon", "\u0417\u0430\u043f\u044f\u0442\u0430\u044f (,)"},
        {"csvexportdialog.separator.semicolon", "\u0422\u043e\u0447\u043a\u0430 \u0441 \u0437\u0430\u043f\u044f\u0442\u043e\u0439 (;)"},
        {"csvexportdialog.separator.other", "\u041f\u0440\u043e\u0447\u0435\u0435"},

        {"csvexportdialog.exporttype", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0441\u043f\u043e\u0441\u043e\u0431 \u0434\u043b\u044f \u044d\u043a\u0441\u043f\u043e\u0440\u0442\u0430"},
        {"csvexportdialog.export.data", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u0434\u0430\u043d\u043d\u044b\u0445 \u043f\u043e-\u0441\u0442\u0440\u043e\u0447\u043d\u043e"},
        {"csvexportdialog.export.printed_elements", "\u041f\u0435\u0447\u0430\u0442\u044c \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u043e\u0432 (\u0420\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435)"},
        {"csvexportdialog.strict-layout", "\u041f\u043e\u0434\u0434\u0435\u0440\u0436\u043a\u0430 \u0441\u0442\u0440\u043e\u0433\u043e\u0433\u043e \u0442\u0430\u0431\u043b\u0438\u0447\u043d\u043e\u0433\u043e \u0440\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u044f \u043f\u0440\u0438 \u044d\u043a\u0441\u043f\u043e\u0440\u0442\u0435\u044e"},


        {"plain-text-exportdialog.dialogtitle", "\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u043e\u0442\u0447\u0435\u0442\u0430 \u0432 \u043e\u0431\u044b\u0447\u043d\u044b\u0439 \u0442\u0435\u043a\u0441\u0442\u043e\u0432\u044b\u0439 \u0444\u0430\u0439\u043b ..."},
        {"plain-text-exportdialog.filename", "\u0418\u043c\u044f \u0444\u0430\u0439\u043b\u0430"},
        {"plain-text-exportdialog.encoding", "\u041a\u043e\u0434\u0438\u0440\u043e\u0432\u043a\u0430"},
        {"plain-text-exportdialog.printer", "\u0422\u0438\u043f \u043f\u0440\u0438\u043d\u0442\u0435\u0440\u0430"},
        {"plain-text-exportdialog.printer.plain", "\u0412\u044b\u0432\u043e\u0434 \u043e\u0431\u044b\u0447\u043d\u043e\u0433\u043e \u0442\u0435\u043a\u0441\u0442"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P \u0441\u043e\u0432\u043c\u0435\u0441\u0442\u0438\u043c\u044b\u0439"},
        {"plain-text-exportdialog.printer.ibm", "IBM \u0441\u043e\u0432\u043c\u0435\u0441\u0442\u0438\u043c\u044b\u0439"},
        {"plain-text-exportdialog.selectFile", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0444\u0430\u0439\u043b"},

        {"plain-text-exportdialog.warningTitle", "\u041f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d\u0438\u0435"},
        {"plain-text-exportdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"plain-text-exportdialog.targetIsEmpty",
         "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u0437\u0430\u0434\u0430\u0439\u0442\u0435 \u0438\u043c\u044f \u0434\u043b\u044f CSV-\u0444\u0430\u0439\u043b\u0430."},
        {"plain-text-exportdialog.targetIsNoFile", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"plain-text-exportdialog.targetIsNotWritable", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0444\u0430\u0439\u043b?"},

        {"plain-text-exportdialog.cancel", "\u041e\u0442\u043c\u0435\u043d\u0430"},
        {"plain-text-exportdialog.confirm", "\u041f\u043e\u0434\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (\u0411\u0443\u043a\u0432 \u043d\u0430 \u0434\u044e\u0439\u043c)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (\u041b\u0438\u043d\u0438\u0439 \u043d\u0430 \u0434\u044e\u0439\u043c)"},
        {"plain-text-exportdialog.font-settings", "\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430 \u0448\u0440\u0438\u0444\u0442\u0430"},

        {"convertdialog.targetIsEmpty", "\u0423\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442"},
        {"convertdialog.errorTitle", "\u041e\u0448\u0438\u0431\u043a\u0430"},
        {"convertdialog.targetIsNoFile", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043e\u0431\u044a\u0435\u043a\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"convertdialog.targetIsNotWritable", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u044b\u0432\u0430\u0435\u043c\u044b\u043c."},
        {"convertdialog.targetOverwriteConfirmation",
         "\u0424\u0430\u0439\u043b ''{0}'' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442. \u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0435\u0433\u043e?"},
        {"convertdialog.targetOverwriteTitle", "\u041f\u0435\u0440\u0435\u0437\u0430\u043f\u0438\u0441\u0430\u0442\u044c \u0444\u0430\u0439\u043b?"},
        {"convertdialog.targetFile", "\u0423\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u0439 \u0444\u0430\u0439\u043b"},
        {"convertdialog.sourceIsEmpty", "\u0418\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442"},
        {"convertdialog.sourceIsNoFile", "\u0412\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u0438\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0444\u0430\u0439\u043b\u043e\u043c."},
        {"convertdialog.sourceIsNotReadable", "\u0418\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u0447\u0438\u0442\u0430\u0435\u043c\u044b\u043c."},
        {"convertdialog.sourceFile", "\u0418\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b"},

        {"convertdialog.action.selectTarget.name", "\u0412\u044b\u0431\u043e\u0440"},
        {"convertdialog.action.selectTarget.description", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0432\u044b\u0445\u043e\u0434\u043d\u043e\u0439 \u0444\u0430\u0439\u043b."},
        {"convertdialog.action.selectSource.name", "\u0412\u044b\u0431\u043e\u0440"},
        {"convertdialog.action.selectSource.description", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0438\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b."},
        {"convertdialog.action.convert.name", "\u041a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435"},
        {"convertdialog.action.convert.description", "\u041a\u043e\u043d\u0432\u0435\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0438\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u0444\u0430\u0439\u043b."},

        {"convertdialog.title", "\u041a\u043e\u043d\u0432\u0435\u0440\u0442\u043e\u0440 \u043e\u0442\u0447\u0435\u0442\u043e\u0432"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(String[] args)
  {
    ResourceCompareTool.main(new String[]{"ru"});
  }

}

