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
 * PDFExportResources_ca.java
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
 * 04-09-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.pdf.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Catalan language resource for the PDF export GUI.
 *
 * @author Marc Casas
 */
public class PDFExportResources_ca extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public PDFExportResources_ca()
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
        {"action.save-as.name", "Desar com a PDF..."},
        {"action.save-as.description", "Desar en format PDF"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_A)},
        {"action.save-as.accelerator", createMenuKeystroke(KeyEvent.VK_S)},

        {"error.processingfailed.title", "Error al processament de l'informe"},
        {"error.processingfailed.message", "Error processant aquest informe: {0}"},
        {"error.savefailed.message", "Error guardant el fitxer PDF: {0}"},
        {"error.savefailed.title", "Error al desar"},

        {"file.save.pdfdescription", "Documents PDF"},

        {"pdfsavedialog.dialogtitle", "Desant l'informe en format PDF..."},
        {"pdfsavedialog.filename", "Nom de fitxer"},
        {"pdfsavedialog.author", "Autor"},
        {"pdfsavedialog.title", "Títol"},
        {"pdfsavedialog.selectFile", "Elegeix un fitxer"},
        {"pdfsavedialog.security", "Opcions de seguretat i encriptació"},
        {"pdfsavedialog.encoding", "Codificació"},

        {"pdfsavedialog.securityNone", "Sense encriptar"},
        {"pdfsavedialog.security40bit", "Encripta amb claus de 40 bits"},
        {"pdfsavedialog.security128bit", "Encripta amb claus de 128 bits"},
        {"pdfsavedialog.userpassword", "Contrassenya d'usuari"},
        {"pdfsavedialog.userpasswordconfirm", "Confirma"},
        {"pdfsavedialog.userpasswordNoMatch", "Les contrassenyes d'usuari no es corresponen."},
        {"pdfsavedialog.ownerpassword", "Contrassenya de propietari"},
        {"pdfsavedialog.ownerpasswordconfirm", "Confirma"},
        {"pdfsavedialog.ownerpasswordNoMatch", 
         "Les contrassenyes de propietari no es corresponen."},
        {"pdfsavedialog.ownerpasswordEmpty", 
         "La contrassenya de propietari és buida. Els usuaris podran "
         + "canviar les opcions de seguretat. Vols continuar?"},

        {"pdfsavedialog.warningTitle", "Atenció"},
        {"pdfsavedialog.errorTitle", "Error"},
        {"pdfsavedialog.targetIsEmpty", "Especifica el nom per al fitxer PDF."},
        {"pdfsavedialog.targetIsNoFile", "El fitxer especificat no és un fitxer normal."},
        {"pdfsavedialog.targetIsNotWritable", "No es pot escriure al fitxer especificat."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "El fitxer ''{0}'' existeix. El vols sobreescriure?"},
        {"pdfsavedialog.targetOverwriteTitle", "Sobreescriure el fitxer?"},

        {"pdfsavedialog.allowCopy", "Permetre copiar"},
        {"pdfsavedialog.allowPrinting", "Permetre imprimir"},
        {"pdfsavedialog.allowDegradedPrinting", "Permetre imprimir en baixa qualitat"},
        {"pdfsavedialog.allowScreenreader", "Permetre lectura en pantalla"},
        {"pdfsavedialog.allowAssembly", "Permetre modificació"},
        {"pdfsavedialog.allowModifyContents", "Permetre modificació del contingut"},
        {"pdfsavedialog.allowModifyAnnotations", "Permetre modificació de les anotacions"},
        {"pdfsavedialog.allowFillIn", "Permetre omplir les dades del formulari"},

        {"pdfsavedialog.option.noprinting", "No imprimir"},
        {"pdfsavedialog.option.degradedprinting", "Impressió a baixa qualitat"},
        {"pdfsavedialog.option.fullprinting", "Impressió permesa"},

        {"pdfsavedialog.cancel", "Cancel·lar"},
        {"pdfsavedialog.confirm", "Confirmar"},

        {"pdf-export.progressdialog.title", "Exportant a un fitxer PDF..."},
        {"pdf-export.progressdialog.message", "L'informe és exportat a un document PDF..."},

      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "ca"});
  }

}
