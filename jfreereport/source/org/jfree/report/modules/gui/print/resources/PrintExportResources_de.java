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
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PrintExportResources_de.java,v 1.3 2003/08/24 15:08:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.print.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * German language resource for the printing export GUI.
 *
 * @author Thomas Morgner
 */
public class PrintExportResources_de extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PrintExportResources_de()
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
        {"action.page-setup.name", "Seite einrichten"},
        {"action.page-setup.description", "Seite einrichten"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_E)},

        {"action.print.name", "Drucken..."},
        {"action.print.description", "Druckt den Bericht"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_D)},

        {"error.printfailed.message", "Das Drucken ist fehlgeschlagen: {0}"},
        {"error.printfailed.title", "Druck fehlgeschlagen"},

        {"printing-export.progressdialog.title", "Drucke den Bericht ..."},
        {"printing-export.progressdialog.message", "Der Bericht wird nun gedruckt ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PrintExportResources.class.getName(), "de"});
  }
}
