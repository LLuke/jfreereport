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
 * Original Author:  Hendri Smit;
 * Contributor(s):   -;
 *
 * $Id: PlainTextExportResources_nl.java,v 1.4 2003/08/25 14:29:30 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.plaintext.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Dutch language resource for the PDF export GUI.
 *
 * @author Hendri Smit
 */
public class PlainTextExportResources_nl extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PlainTextExportResources_nl()
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
        {"action.export-to-plaintext.name", "Opslaan als Tekst Bestand..."},
        {"action.export-to-plaintext.description", "Opslaan als Tekst zonder opmaak"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"plain-text-exportdialog.cancel", "Annuleren"},
        {"plain-text-exportdialog.chars-per-inch", "kpi (Karakters per inch)"},
        {"plain-text-exportdialog.confirm", "OK"},
        {"plain-text-exportdialog.dialogtitle", "Opslaan als Text Bestand..."},
        {"plain-text-exportdialog.encoding", "Encoderen"},
        {"plain-text-exportdialog.errorTitle", "Fout"},
        {"plain-text-exportdialog.filename", "Bestandsnaam"},
        {"plain-text-exportdialog.font-settings", "Lettertype-instellingen"},
        {"plain-text-exportdialog.lines-per-inch", "rpi (Regels per inch)"},
        {"plain-text-exportdialog.printer", "Printer"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P compatible printer"},
        {"plain-text-exportdialog.printer.ibm", "IBM compatible printer"},
        {"plain-text-exportdialog.printer.plain", "Simpele Tekstuitvoer"},
        {"plain-text-exportdialog.selectFile", "Selecteer bestand"},
        {"plain-text-exportdialog.targetIsEmpty", "Geef een bestandsnaam"},
        {"plain-text-exportdialog.targetIsNoFile", "Doelbestand is ongeldig"},
        {"plain-text-exportdialog.targetIsNotWritable", "Doelbestand kan niet worden beschreven."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Bestand overschrijven?"},
        {"plain-text-exportdialog.warningTitle", "Waarschuwing"},

        {"error.processingfailed.title", "Fout tijdens report generatie"},
        {"error.processingfailed.title", "Fout tijdens rapport generatie"},
        {"error.processingfailed.message", "Rapport generatie mislukt: {0}"},
        {"error.savefailed.title", "Fout tijdens opslaan"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PlainTextExportResources.class.getName(), "nl"});
  }
}
