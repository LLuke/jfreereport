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
 * PrintExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Hendri Smit;
 * Contributor(s):   -;
 *
 * $Id: PrintExportResources_nl.java,v 1.5 2003/09/08 18:39:33 taqua Exp $
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
 * Dutch language resource for the printing export GUI.
 *
 * @author Hendri Smit
 */
public class PrintExportResources_nl extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PrintExportResources_nl()
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
        {"action.page-setup.name", "Pagina-instelling..."},
        {"action.page-setup.description", "Pagina instellen"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_G)},

        {"action.print.name", "Afdrukken..."},
        {"action.print.description", "Afdrukken"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_D)},

        {"error.printfailed.message", "Afdrukken mislukt: {0}"},
        {"error.printfailed.title", "Fout tijdens afdrukken"},
        {"printing-export.progressdialog.title", "Bezig met afdrukken van het rapport ..."},
        {"printing-export.progressdialog.message", "Het rapport wordt nu afgedrukt ..."},


      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new PrintExportResources_nl().generateResourceProperties("dutch");
    System.exit(0);
  }
}
