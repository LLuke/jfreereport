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
 * Original Author:  Ramon Juanes;
 * Contributor(s):   -;
 *
 * $Id: PDFExportResources_es.java,v 1.2 2003/08/19 13:37:24 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.pdf.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Spanish language resource for the PDF export GUI.
 * 
 * @author Ramon Juanes
 */
public class PDFExportResources_es extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PDFExportResources_es()
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
        {"action.save-as.name", "Guardar como..."},
        {"action.save-as.description", "Guardar en formato PDF"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_G)},

        {"file.save.pdfdescription", "Ficheros PDF"},

        {"error.processingfailed.title", "Error en la ejecución"},
        {"error.processingfailed.message", "Error de ejecución: {0}"},
        {"error.savefailed.message", "Error de gravación: {0}"},
        {"error.savefailed.title", "Error en la gravación"},

        {"pdfsavedialog.dialogtitle", "Guardar documento en formato PDF ..."},
        {"pdfsavedialog.filename", "Nombre fichero"},
        {"pdfsavedialog.author", "Autor"},
        {"pdfsavedialog.title", "Título"},
        {"pdfsavedialog.selectFile", "Buscar"},
        {"pdfsavedialog.security", "Datos de Seguridad y Cifrado"},

        {"pdfsavedialog.securityNone", "Sin seguridad"},
        {"pdfsavedialog.security40bit", "Cifrar con clave de 40 bits"},
        {"pdfsavedialog.security128bit", "Cifrar con clave de 128 bits"},
        {"pdfsavedialog.userpassword", "Contraseña de usuario"},
        {"pdfsavedialog.userpasswordconfirm", "Confirmar"},
        {"pdfsavedialog.userpasswordNoMatch", "Error al confirmar la contraseña de usuario."},
        {"pdfsavedialog.ownerpassword", "Contraseña de propietario"},
        {"pdfsavedialog.ownerpasswordconfirm", "Confirmar"},
        {"pdfsavedialog.ownerpasswordNoMatch",
         "Error al confirmar la contraseña de propietario."},

        {"pdfsavedialog.errorTitle", "Error"},
        {"pdfsavedialog.targetIsEmpty", "Introduce el nombre del fichero PDF."},
        {"pdfsavedialog.targetIsNoFile", "Destino no válido."},
        {"pdfsavedialog.targetIsNotWritable", "No se puede escribir el fichero."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Ya existe un fichero: ''{0}'' . ¿Desea sobreescribirlo?"},
        {"pdfsavedialog.targetOverwriteTitle", "Aviso"},


        {"pdfsavedialog.allowCopy", "Copiable"},
        {"pdfsavedialog.allowPrinting", "Imprimible"},
        {"pdfsavedialog.allowDegradedPrinting", "Imprimible en baja calidad"},
        {"pdfsavedialog.allowScreenreader", "Autorizar 'Screenreaders'"},
        {"pdfsavedialog.allowAssembly", "Reensamblable"},
        {"pdfsavedialog.allowModifyContents", "Contenido modificable"},
        {"pdfsavedialog.allowModifyAnnotations", "Anotaciones modificables"},
        {"pdfsavedialog.allowFillIn", "Campos formularios rellenables"},

        {"pdfsavedialog.option.noprinting", "No imprimible"},
        {"pdfsavedialog.option.degradedprinting", "Imprimible en baja calidad"},
        {"pdfsavedialog.option.fullprinting", "Inprimible"},

        {"pdfsavedialog.cancel", "Cancelar"},
        {"pdfsavedialog.confirm", "Aceptar"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "es"});
  }
}
