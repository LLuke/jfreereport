/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------------
 * JFreeReportResources_es.java
 * -------------------------
 *
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

/**
 * Spanish Language Resources.
 *
 * @author Ramon Juanes
 */
public class JFreeReportResources_es extends JFreeReportResources
{

  /**
   * Returns the array of strings in the resource bundle.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents ()
  {
    return contents;
  }

  /** The resources to be localised. */
  private static final Object[][] contents =
          {
            {"action.save-as.name", "Guardar como..."},
            {"action.save-as.description", "Guardar en formato PDF"},
            {"action.save-as.mnemonic", new Integer (KeyEvent.VK_G)},

            {"action.page-setup.name", "Configurar P�gina"},
            {"action.page-setup.description", "Configurar P�gina"},
            {"action.page-setup.mnemonic", new Integer (KeyEvent.VK_P)},

            {"action.print.name", "Imprimir..."},
            {"action.print.description", "Imprimir el documento"},
            {"action.print.mnemonic", new Integer (KeyEvent.VK_I)},

            {"action.close.name", "Cerrar"},
            {"action.close.description", "Cerrar ventana de vista previa"},
            {"action.close.mnemonic", new Integer (KeyEvent.VK_C)},

            {"action.gotopage.name", "Ir a ..."},
            {"action.gotopage.description", "Visualizar una p�gina determinada."},

            {"dialog.gotopage.message", "Introduce un n�mero de p�gina"},
            {"dialog.gotopage.title", "Ir a la p�gina ..."},

            {"action.about.name", "Acerca de..."},
            {"action.about.description", "Acerca de JFreeReport"},
            {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},

            {"action.firstpage.name", "Inicio"},
            {"action.firstpage.description", "Ir a la primera p�gina"},

            {"action.lastpage.name", "Final"},
            {"action.lastpage.description", "Ir a la �ltima p�gina"},

            {"action.back.name", "Anterior"},
            {"action.back.description", "Ir a la p�gina anterior"},

            {"action.forward.name", "Siguiente"},
            {"action.forward.description", "Ir a la p�gina siguiente"},

            {"action.zoomIn.name", "Aumentar Zoom"},
            {"action.zoomIn.description", "Aumentar el zoom"},

            {"action.zoomOut.name", "Disminuir Zoom"},
            {"action.zoomOut.description", "Disminuir el zoom"},

            {"preview-frame.title", "Vista preliminar"},

            {"menu.file.name", "Archivo"},
            {"menu.file.mnemonic", new Character ('A')},

            {"menu.help.name", "Ayuda"},
            {"menu.help.mnemonic", new Character ('Y')},

            {"file.save.pdfdescription", "Ficheros PDF"},
            {"statusline.pages", "P�gina {0} de {1}"},
            {"statusline.error", "Error al generar el documento: {0}"},
            {"error.processingfailed.title", "Error en la ejecuci�n"},
            {"error.processingfailed.message", "Error de ejecuci�n: {0}"},
            {"error.savefailed.message", "Error de gravaci�n: {0}"},
            {"error.savefailed.title", "Error en la gravaci�n"},
            {"error.printfailed.message", "Error de impresi�n: {0}"},
            {"error.printfailed.title", "Error en la impresi�n"},

            {"pdfsavedialog.dialogtitle", "Guardar documento en formato PDF ..."},
            {"pdfsavedialog.filename", "Nombre fichero"},
            {"pdfsavedialog.author", "Autor"},
            {"pdfsavedialog.title", "T�tulo"},
            {"pdfsavedialog.selectFile", "Buscar"},
            {"pdfsavedialog.security", "Datos de Seguridad y Cifrado"},

            {"pdfsavedialog.securityNone", "Sin seguridad"},
            {"pdfsavedialog.security40bit", "Cifrar con clave de 40 bits"},
            {"pdfsavedialog.security128bit", "Cifrar con clave de 128 bits"},
            {"pdfsavedialog.userpassword", "Contrase�a de usuario"},
            {"pdfsavedialog.userpasswordconfirm", "Confirmar"},
            {"pdfsavedialog.userpasswordNoMatch", "Error al confirmar la contrase�a de usuario."},
            {"pdfsavedialog.ownerpassword", "Contrase�a de propietario"},
            {"pdfsavedialog.ownerpasswordconfirm", "Confirmar"},
            {"pdfsavedialog.ownerpasswordNoMatch", "Error al confirmar la contrase�a de propietario."},

            {"pdfsavedialog.errorTitle", "Error"},
            {"pdfsavedialog.targetIsEmpty", "Introduce el nombre del fichero PDF."},
            {"pdfsavedialog.targetIsNoFile", "Destino no v�lido."},
            {"pdfsavedialog.targetIsNotWritable", "No se puede escribir el fichero."},
            {"pdfsavedialog.targetOverwriteConfirmation", "Ya existe un fichero: ''{0}'' . �Desea sobreescribirlo?"},
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
}
