/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * ----------------------------
 * JFreeReportResources_es.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Ramon Juanes;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_es.java,v 1.2 2003/07/18 17:56:38 taqua Exp $
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * Spanish Language Resources.
 *
 * @author Ramon Juanes
 */
public class JFreeReportResources_es extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public JFreeReportResources_es()
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
        {"action.close.name", "Cerrar"},
        {"action.close.description", "Cerrar ventana de vista previa"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_C)},

        {"action.gotopage.name", "Ir a ..."},
        {"action.gotopage.description", "Visualizar una página determinada."},

        {"dialog.gotopage.message", "Introduce un número de página"},
        {"dialog.gotopage.title", "Ir a la página ..."},

        {"action.about.name", "Acerca de..."},
        {"action.about.description", "Acerca de JFreeReport"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Inicio"},
        {"action.firstpage.description", "Ir a la primera página"},

        {"action.lastpage.name", "Final"},
        {"action.lastpage.description", "Ir a la última página"},

        {"action.back.name", "Anterior"},
        {"action.back.description", "Ir a la página anterior"},

        {"action.forward.name", "Siguiente"},
        {"action.forward.description", "Ir a la página siguiente"},

        {"action.zoomIn.name", "Aumentar Zoom"},
        {"action.zoomIn.description", "Aumentar el zoom"},

        {"action.zoomOut.name", "Disminuir Zoom"},
        {"action.zoomOut.description", "Disminuir el zoom"},

        {"preview-frame.title", "Vista preliminar"},

        {"menu.file.name", "Archivo"},
        {"menu.file.mnemonic", new Character('A')},

        {"menu.help.name", "Ayuda"},
        {"menu.help.mnemonic", new Character('Y')},

        {"statusline.pages", "Página {0} de {1}"},
        {"statusline.error", "Error al generar el documento: {0}"},
      };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "es"});
  }


}
