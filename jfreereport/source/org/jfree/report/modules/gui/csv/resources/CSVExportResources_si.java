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
 * CSVExportResources_es.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Sergey M Mozgovoi;
 * Contributor(s):   -;
 *
 * $Id: CSVExportResources_si.java,v 1.5 2003/09/08 18:39:33 taqua Exp $
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
 * Slovenian language resource for the CSV export GUI.
 *
 * @author Sergey M Mozgovoi
 */
public class CSVExportResources_si extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public CSVExportResources_si()
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
        {"action.export-to-csv.name", "Izvozi v CSV..."},
        {"action.export-to-csv.description", "Shrani v CSV obliki"},

        {"error.processingfailed.title", "Obdelava poro\u010Dila ni uspela"},
        {"error.processingfailed.message", "Napaka pri obdelavi poro\u010Dila: {0}"},
        {"error.savefailed.message", "Napaka pri shranjevanju CSV datoteke: {0}"},
        {"error.savefailed.title", "Napaka pri shranjevanju"},

        {"csvexportdialog.dialogtitle", "Izvozi poro\u010Dilo v CSV datoteko ..."},
        {"csvexportdialog.filename", "Ime datoteke"},
        {"csvexportdialog.encoding", "Kodiranje"},
        {"csvexportdialog.separatorchar", "Lo\u010Dilo"},
        {"csvexportdialog.selectFile", "Izberite datoteko"},

        {"csvexportdialog.warningTitle", "Opozorilo"},
        {"csvexportdialog.errorTitle", "Napaka"},
        {"csvexportdialog.targetIsEmpty", "Prosim navedite ime za CSV datoteko."},
        {"csvexportdialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"csvexportdialog.targetIsNotWritable", "V izbrano datoteko ni mogo\u010De pisati."},
        {"csvexportdialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"csvexportdialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},

        {"csvexportdialog.cancel", "Prekli\u010Di"},
        {"csvexportdialog.confirm", "Potrdi"},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Vejica (,)"},
        {"csvexportdialog.separator.semicolon", "Podpi\u010Dje (;)"},
        {"csvexportdialog.separator.other", "Ostalo"},

        {"csvexportdialog.exporttype", "Izberi izvozni mehanizem"},
        {"csvexportdialog.export.data", "Izvozi podatkovno vrstico (neobdelani podatki)"},
        {"csvexportdialog.export.printed_elements", "Natisnjeni elementi (urejeni podatki)"},
        {"csvexportdialog.strict-layout", "Izvedi dosledno urejanje tabele pri izvozu."},

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new CSVExportResources_si().generateResourceProperties("slovenian");
    System.exit(0);
  }

}
