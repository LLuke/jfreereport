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
 * PrintExportResources_ca.java
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

package org.jfree.report.modules.gui.print.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Catalan language resource for the printing export GUI.
 *
 * @author Marc Casas
 */
public class PrintExportResources_ca extends JFreeReportResources
{
  /**
   * Default Constructor.
   *
   */
  public PrintExportResources_ca()
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
        {"action.page-setup.name", "Configuració de pàgina"},
        {"action.page-setup.description", "Configuració de pàgina"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_G)},

        {"action.print.name", "Imprimir..."},
        {"action.print.description", "Imprimir l'informe"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_P)},
        {"action.print.accelerator", createMenuKeystroke(KeyEvent.VK_P)},

        {"error.printfailed.message", "Error imprimint l'informe: {0}"},
        {"error.printfailed.title", "Error imprimint"},

        {"printing-export.progressdialog.title", "Imprimint l'informe..."},
        {"printing-export.progressdialog.message", "L'informe s'ha imprés..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PrintExportResources.class.getName(), "ca"});
  }

}
