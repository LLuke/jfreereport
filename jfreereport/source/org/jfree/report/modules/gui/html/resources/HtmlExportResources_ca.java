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
 * HtmlExportResources_ca.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Marc Casas;
 * Contributor(s):   -
 *
 * $Id: HtmlExportResources_ca.java,v 1.1 2003/09/08 18:41:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 03-09-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.html.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Catalan language resource for the Html export GUI.
 *
 * @author Marc Casas
 */
public class HtmlExportResources_ca extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public HtmlExportResources_ca()
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
        {"action.export-to-html.name", "Exportar a html..."},
        {"action.export-to-html.description", "Desar en format HTML"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},
        {"action.export-to-html.accelerator", createMenuKeystroke(KeyEvent.VK_H)},
        // temporarily using the same icon as "Save to PDF", till we have a better one

        {"htmlexportdialog.dialogtitle", "Exportar informe a un fitxer HTML..."},

        {"htmlexportdialog.filename", "Nom de fitxer"},
        {"htmlexportdialog.datafilename", "Directori de dades"},
        {"htmlexportdialog.copy-external-references", "Copiar referències externes"},

        {"htmlexportdialog.author", "Autor"},
        {"htmlexportdialog.title", "Títol"},
        {"htmlexportdialog.encoding", "Codificació"},
        {"htmlexportdialog.selectZipFile", "Elegeix el fitxer"},
        {"htmlexportdialog.selectStreamFile", "Elegeix el fitxer"},
        {"htmlexportdialog.selectDirFile", "Elegeix el fitxer"},

        {"htmlexportdialog.strict-layout", "Fer una estricta presentació al exportar."},
        {"htmlexportdialog.generate-xhtml", "Generar sortida en format XHTML 1.0"},
        {"htmlexportdialog.generate-html4", "Generar sortida en format HTML 4.0"},

        {"htmlexportdialog.warningTitle", "Atenció"},
        {"htmlexportdialog.errorTitle", "Error"},
        {"htmlexportdialog.targetIsEmpty", "S'ha de posar un nom pel fitxer Html."},
        {"htmlexportdialog.targetIsNoFile", "El fitxer especificat no és un fitxer normal."},
        {"htmlexportdialog.targetIsNotWritable", "No es pot escriure al fitxer especificat."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "El fitxer ''{0}'' existeix. El vols sobreescriure?"},
        {"htmlexportdialog.targetOverwriteTitle", "Sobreescriure el fitxer?"},

        {"htmlexportdialog.cancel", "Cancel·lar"},
        {"htmlexportdialog.confirm", "Confirmar"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "La direcció de sortida especificada apunta a un directori absolut.\n"
      + "Si us plau, introdueix un nom de directori dins el fitxer ZIP."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "El directori especificat no és vàlid."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "El directori especificat no existeix.\n"
      + "S'han de crear els directoris que falten?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Crear el directori?"},

        {"htmlexportdialog.html-documents", "Documents HTML"},
        {"htmlexportdialog.zip-archives", "Arxius ZIP"},

        {"htmlexportdialog.stream-export", "Exportació a un fitxer"},
        {"htmlexportdialog.directory-export", "Exportació a un directori"},
        {"htmlexportdialog.zip-export", "Exportació a un fitxer ZIP"},

        {"error.processingfailed.title", "Error al processament de l'informe"},
        {"error.processingfailed.message", "Error processant aquest informe: {0}"},
        {"error.savefailed.message", "Error guardant el fitxer: {0}"},
        {"error.savefailed.title", "Error al desar"},
        {"error.validationfailed.message", "Error validant les dades entrades per l'usuari."},
        {"error.validationfailed.title", "Error durant la validació"},

        {"html-export.progressdialog.title", "Exportant a format HTML..."},
        {"html-export.progressdialog.message", "L'informe s'ha exportat a un fitxer Html..."},

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new HtmlExportResources_ca().generateResourceProperties("catalan");
    System.exit(0);
  }

}