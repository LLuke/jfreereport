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

public class HtmlExportResources_si extends JFreeReportResources
{
  public HtmlExportResources_si()
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
        {"error.validationfailed.message",
         "Napaka med preverjanjem vnosa uporabnikovih podatkov."},
        {"error.validationfailed.title", "Napaka pri preverjanju"},
        {"error.processingfailed.title", "Obdelava poro\u010Dila ni uspela"},
        {"error.processingfailed.message", "Napaka pri obdelavi poro\u010Dila: {0}"},
        {"error.savefailed.message", "Napaka pri shranjevanju PDF datoteke: {0}"},
        {"error.savefailed.title", "Napaka pri shranjevanju"},

        {"action.export-to-html.name", "Izvozi v HTML..."},
        {"action.export-to-html.description", "Shrani v HTML obliki"},

        {"htmlexportdialog.dialogtitle", "Izvozi poro\u010Dilo v HTML datoteko ..."},

        {"htmlexportdialog.filename", "Ime datoteke"},
        {"htmlexportdialog.datafilename", "Podatkovni imenik"},
        {"htmlexportdialog.copy-external-references", "Kopiraj zunanje sklice"},


        {"htmlexportdialog.author", "Avtor"},
        {"htmlexportdialog.title", "Naslov"},
        {"htmlexportdialog.encoding", "Encoding"},
        {"htmlexportdialog.selectZipFile", "Izberite datoteko"},
        {"htmlexportdialog.selectStreamFile", "Izberite datoteko"},
        {"htmlexportdialog.selectDirFile", "Izberite datoteko"},

        {"htmlexportdialog.strict-layout", "Izvedi dosledno urejanje tabele pri izvozu."},
        {"htmlexportdialog.generate-xhtml", "Generiraj izhod v XHTML 1.0 obliki"},
        {"htmlexportdialog.generate-html4", "Generiraj izhod v HTML 4.0 obliki"},

        {"htmlexportdialog.warningTitle", "Opozorilo"},
        {"htmlexportdialog.errorTitle", "Napaka"},
        {"htmlexportdialog.targetIsEmpty", "Prosim navedite ime HTML datoteke."},
        {"htmlexportdialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"htmlexportdialog.targetIsNotWritable", "V izbrano datoteko ni mogo\u010De pisati."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"htmlexportdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},

        {"htmlexportdialog.cancel", "Prekli\u010Di"},
        {"htmlexportdialog.confirm", "Potrdi"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "Navedena ciljna pot je absolutni imenik.\n"
      + "Prosim vnesite podatkovni imenik v ZIP datoteki."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "Navedeni podatkovni imenik ni veljaven."},

        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "Navedeni podatkovni imenik ne obstaja.\n"
      + "Ali naj ustvarim manjkajo\u010De podimenike?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Ustvarim podatkovni imenik?"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{HtmlExportResources.class.getName(), "si"});
  }
}
