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
 * $Id: PlainTextExportResources_si.java,v 1.5 2003/09/08 18:39:33 taqua Exp $
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
 * Slovenian language resource for the PlainText export GUI.
 *
 * @author Sergey M Mozgovoi
 */
public class PlainTextExportResources_si extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PlainTextExportResources_si()
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
        {"action.export-to-plaintext.name", "Shrani kot besedilno datoteko..."},
        {"action.export-to-plaintext.description", "Shrani v Plain-Text obliki"},

        {"error.processingfailed.title", "Obdelava poro\u010Dila ni uspela"},
        {"error.processingfailed.message", "Napaka pri obdelavi poro\u010Dila: {0}"},
        {"error.savefailed.message", "Napaka pri shranjevanju Plain-Text datoteke: {0}"},
        {"error.savefailed.title", "Napaka pri shranjevanju"},

        {"plain-text-exportdialog.dialogtitle", "Izvozi poro\u010Dilo v Plain-Text datoteko."},
        {"plain-text-exportdialog.filename", "Ime datoteke"},
        {"plain-text-exportdialog.encoding", "Kodiranje"},
        {"plain-text-exportdialog.printer", "Tip tiskalnika"},
        {"plain-text-exportdialog.printer.plain", "Izhod v Plain-Text obliki"},
        {"plain-text-exportdialog.printer.epson", "Zdru\u017Eljivo s tiskalnikom Epson ESC/P"},
        {"plain-text-exportdialog.printer.ibm", "Zdru\u017Eljivo s tiskalnikom IBM"},
        {"plain-text-exportdialog.selectFile", "Izberite datoteko"},

        {"plain-text-exportdialog.warningTitle", "Opozorilo"},
        {"plain-text-exportdialog.errorTitle", "Napaka"},
        {"plain-text-exportdialog.targetIsEmpty",
         "Prosim navedite ime za CSV datoteko."},
        {"plain-text-exportdialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"plain-text-exportdialog.targetIsNotWritable",
         "V izbrano datoteko ni mogo\u010De pisati."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},

        {"plain-text-exportdialog.cancel", "Prekli\u010Di"},
        {"plain-text-exportdialog.confirm", "Potrdi"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (znakov na palec)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (vrstic na palec)"},
        {"plain-text-exportdialog.font-settings", "Nastavitev pisave"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new PlainTextExportResources_si().generateResourceProperties("slovenian");
    System.exit(0);
  }
}
