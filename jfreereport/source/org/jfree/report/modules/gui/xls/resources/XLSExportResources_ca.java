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
 * XLSExportResources_ca.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Marc Casas
 * Contributor(s):   -
 *
 * $Id: XLSExportResources_ca.java,v 1.1 2003/09/08 18:41:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.09.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.xls.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Catalan language resource for the Html export GUI.
 *
 * @author Marc Casas
 */
public class XLSExportResources_ca extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public XLSExportResources_ca()
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
        {"action.export-to-excel.name", "Exportar a Excel..."},
        {"action.export-to-excel.description", "Desar en format MS-Excel format"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_E)},
        {"action.export-to-excel.accelerator", createMenuKeystroke(KeyEvent.VK_E)},

        {"error.processingfailed.title", "Error al processament de l'informe"},
        {"error.processingfailed.message", "Error processant aquest informe: {0}"},
        {"error.savefailed.message", "Error guardant el fitxer Excel: {0}"},
        {"error.savefailed.title", "Error al desar"},

        {"excelexportdialog.dialogtitle", "Exportar l'informe a un fitxer d'Excel..."},
        {"excelexportdialog.filename", "Nom del fitxer"},
        {"excelexportdialog.author", "Autor"},
        {"excelexportdialog.title", "Títol"},
        {"excelexportdialog.selectFile", "Elegeix el fitxer"},

        {"excelexportdialog.warningTitle", "Atenció"},
        {"excelexportdialog.errorTitle", "Error"},
        {"excelexportdialog.targetIsEmpty", "S'ha de posar un nom pel fitxer de sortida."},
        {"excelexportdialog.targetIsNoFile", "El fitxer especificat no és un fitxer normal."},
        {"excelexportdialog.targetIsNotWritable", "No es pot escriure al fitxer especificat."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "El fitxer ''{0}'' existeix. El vols sobreescriure?"},
        {"excelexportdialog.targetOverwriteTitle", "Sobreescriure el fitxer?"},

        {"excelexportdialog.cancel", "Cancel·lar"},
        {"excelexportdialog.confirm", "Confirmar"},
        {"excelexportdialog.strict-layout", "Fer una estricta presentació al exportar."},

        {"excel-export.progressdialog.title", "Exportant a un fitxer d'Excel..."},
        {"excel-export.progressdialog.message",
          "L'informe s'ha exportat a un llibre d'Excel ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new XLSExportResources_ca().generateResourceProperties("catalan");
    System.exit(0);
  }
}