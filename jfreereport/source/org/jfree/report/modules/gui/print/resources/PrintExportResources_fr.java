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
 * Original Author:  PR;
 * Contributor(s):   -;
 *
 * $Id: PrintExportResources_fr.java,v 1.6 2003/09/14 19:33:25 taqua Exp $
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
 * French language resource for the printing export GUI.
 *
 * @author PR
 */
public class PrintExportResources_fr extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PrintExportResources_fr()
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
        {"action.page-setup.name", "Mise en page"},
        {"action.page-setup.description", "Mise en page"},
        {"action.page-setup.mnemonic", new Integer(KeyEvent.VK_M)},

        {"action.print.name", "Impression..."},
        {"action.print.description", "Impression du rapport"},
        {"action.print.mnemonic", new Integer(KeyEvent.VK_I)},

        {"error.printfailed.message", "Erreur à l'impression du rapport: {0}"},
        {"error.printfailed.title", "Erreur à l'impression"},
        
        {"printing-export.progressdialog.title", "Impression du rapport ..."},
        {"printing-export.progressdialog.message", "Le rapport va maintenant être imprimé ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new PrintExportResources_fr().generateResourceProperties("french");
    System.exit(0);
  }
}
