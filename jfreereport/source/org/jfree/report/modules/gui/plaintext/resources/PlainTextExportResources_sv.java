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
 * PDFExportResources.java
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

package org.jfree.report.modules.gui.plaintext.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class PlainTextExportResources_sv extends JFreeReportResources
{
  public PlainTextExportResources_sv()
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
        {"action.export-to-plaintext.name", "Spara som text file..."},
        {"action.export-to-plaintext.description", "Spara till vanlig text fil"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},

        {"error.processingfailed.title", "Report generation misslyckades"},
        {"error.processingfailed.message", "Fel när rapporten skapades: {0}"},
        {"error.savefailed.message", "Fel inträffade under PDF sparning: {0}"},
        {"error.savefailed.title", "Fel under sparningen"},

        {"plain-text-exportdialog.dialogtitle", "Exportera rapporten till en vanlig text fil ..."},
        {"plain-text-exportdialog.filename", "Filnamn"},
        {"plain-text-exportdialog.encoding", "Kodningen"},
        {"plain-text-exportdialog.printer", "Skrivare typ"},
        {"plain-text-exportdialog.printer.plain", "Vanlig text utskrift"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P kompatibel"},
        {"plain-text-exportdialog.printer.ibm", "IBM kompatibel"},
        {"plain-text-exportdialog.selectFile", "Välja filen"},

        {"plain-text-exportdialog.warningTitle", "Varning"},
        {"plain-text-exportdialog.errorTitle", "Fel"},
        {"plain-text-exportdialog.targetIsEmpty",
         "Ange filnamnet till CSV filen."},
        {"plain-text-exportdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"plain-text-exportdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Skriva över filen?"},

        {"plain-text-exportdialog.cancel", "Avbryt"},
        {"plain-text-exportdialog.confirm", "Konfirmera"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (Antal tecken i en tum)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Rader i en tum)"},
        {"plain-text-exportdialog.font-settings", "Teckensnitt inställningar"},


      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PlainTextExportResources.class.getName(), "sv"});
  }
}
