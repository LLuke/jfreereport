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
 * Original Author:  Hendri Smit;
 * Contributor(s):   -;
 *
 * $Id: CSVExportResources_nl.java,v 1.5 2003/09/06 18:09:16 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.csv.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Dutch language resource for the CSV export GUI.
 *
 * @author Hendri Smit
 */
public class CSVExportResources_nl extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public CSVExportResources_nl()
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
        {"action.export-to-csv.name", "Exporteren naar CSV..."},
        {"action.export-to-csv.description", "Opslaan in CSV formaat"},
        {"action.export-to-csv.mnemonic", new Integer(KeyEvent.VK_C)},

        {"csvexportdialog.dialogtitle", "Opslaan als CSV bestand..."},
        {"csvexportdialog.filename", "Bestandsnaam"},
        {"csvexportdialog.encoding", "Encoderen"},
        {"csvexportdialog.separatorchar", "Scheidingsteken"},
        {"csvexportdialog.selectFile", "Selecteer Bestand"},

        {"csvexportdialog.warningTitle", "Waarschuwing"},
        {"csvexportdialog.errorTitle", "Fout"},
        {"csvexportdialog.targetIsEmpty",
         "Geef een bestandsnaam voor het CSV bestand."},
        {"csvexportdialog.targetIsNoFile", "Doelbestand is ongeldig."},
        {"csvexportdialog.targetIsNotWritable",
         "Doelbestand kan niet worden beschreven."},

        {"csvexportdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"csvexportdialog.targetOverwriteTitle", "Bestand overschrijven?"},

        {"csvexportdialog.cancel", "Annuleren"},
        {"csvexportdialog.confirm", "OK"},
        {"csvexportdialog.strict-layout", "Exacte layout gebruiken."},

        {"csvexportdialog.separator.tab", "Tabulator"},
        {"csvexportdialog.separator.colon", "Komma (,)"},
        {"csvexportdialog.separator.semicolon", "Punt-komma (;)"},
        {"csvexportdialog.separator.other", "Anders"},

        {"csvexportdialog.exporttype", "Selecteer export methode"},
        {"csvexportdialog.export.data", "Exporteer de brondata (Ruwe data)"},
        {"csvexportdialog.export.printed_elements",
         "Afgedrukte elementen (Layout data)"},

        {"error.processingfailed.title", "Fout tijdens report generatie"},
        {"error.processingfailed.title", "Fout tijdens rapport generatie"},
        {"error.processingfailed.message", "Rapport generatie mislukt: {0}"},
        {"error.savefailed.title", "Fout tijdens opslaan"},
        {"error.savefailed.message", "Opslaan als CSV bestand mislukt: {0}"},

        {"csvexportdialog.csv-file-description", "Comma Separated Value bestanden."},
        {"cvs-export.progressdialog.title", "Bezig te exporteren naar CSV bestand ..."},
        {"cvs-export.progressdialog.message", "Het rapport wordt omgezet in een CSV bestand ..."},
      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{CSVExportResources.class.getName(), "nl"});
  }

}
