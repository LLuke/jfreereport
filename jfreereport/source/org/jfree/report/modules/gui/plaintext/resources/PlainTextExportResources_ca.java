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
 * PlainTextExportResources_ca.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Marc Casas
 * Contributor(s):   -
 *
 * $Id: $
 *
 * Changes
 * -------------------------
 * 05.09.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.plaintext.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Catalan language resource for the PlainText export GUI.
 *
 * @author Marc Casas
 */
public class PlainTextExportResources_ca extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PlainTextExportResources_ca()
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
        {"action.export-to-plaintext.name", "Desar com a fitxer de text..."},
        {"action.export-to-plaintext.description", "Desar en format de text"},
        {"action.export-to-plaintext.mnemonic", new Integer(KeyEvent.VK_T)},
        {"action.export-to-plaintext.accelerator", createMenuKeystroke(KeyEvent.VK_T)},

        {"error.processingfailed.title", "Error al processament de l'informe"},
        {"error.processingfailed.message", "Error processant aquest informe: {0}"},
        {"error.savefailed.message", "Error guardant el fitxer text: {0}"},
        {"error.savefailed.title", "Error al desar"},

        {"plain-text-exportdialog.dialogtitle", "Exportar informe a un fitxer de text pla..."},
        {"plain-text-exportdialog.filename", "Nom de fitxer"},
        {"plain-text-exportdialog.encoding", "Codificació"},
        {"plain-text-exportdialog.printer", "Tipus d'impressora"},
        {"plain-text-exportdialog.printer.plain", "Sortida de text pur"},
        {"plain-text-exportdialog.printer.epson", "Epson ESC/P i compatibles"},
        {"plain-text-exportdialog.printer.ibm", "IBM i compatibles"},
        {"plain-text-exportdialog.selectFile", "Elegeix el fitxer"},

        {"plain-text-exportdialog.warningTitle", "Atenció"},
        {"plain-text-exportdialog.errorTitle", "Error"},
        {"plain-text-exportdialog.targetIsEmpty",
         "S'ha de posar un nom pel fitxer de sortida."},
        {"plain-text-exportdialog.targetIsNoFile", "El fitxer especificat no és un fitxer normal."},
        {"plain-text-exportdialog.targetIsNotWritable", 
          "No es pot escriure al fitxer especificat."},
        {"plain-text-exportdialog.targetOverwriteConfirmation",
         "El fitxer ''{0}'' existeix. El vols sobreescriure?"},
        {"plain-text-exportdialog.targetOverwriteTitle", "Sobreescriure el fitxer?"},

        {"plain-text-exportdialog.cancel", "Cancel·lar"},
        {"plain-text-exportdialog.confirm", "Confirmar"},

        {"plain-text-exportdialog.chars-per-inch", "cpi (Caràcters per polzada)"},
        {"plain-text-exportdialog.lines-per-inch", "lpi (Linies per polzada)"},
        {"plain-text-exportdialog.font-settings", "Tipus de lletra"},

        {"plaintext-export.progressdialog.title", "Exportant a un fitxer de text..."},
        {"plaintext-export.progressdialog.message",
          "L'informe s'ha exportat a un fitxer de text..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PlainTextExportResources.class.getName(), "ca"});
  }
}