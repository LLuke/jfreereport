/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ---------------------
 * DemoResources_hu.java
 * ---------------------
 * (C)opyright 2003, by Demeter F. Tam�s
 *
 * Original Author:  Demeter F. Tam�s;
 * Contributor(s):   -;
 *
 * $Id: DemoResources_hu.java,v 1.3 2003/08/28 17:45:42 taqua Exp $
 *
 *
 */
package org.jfree.report.demo.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 *
 * @author Demeter F. Tam�s
 */
public class DemoResources_hu extends JFreeReportResources
{
  /**
   * Returns the contents of the resource bundle.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS = {
    // in the title pattern, leave in the '{0}' as it gets replaced with the version number
    {"main-frame.title.pattern", "JFreeReport {0} Dem�"},

    {"action.close.name", "Kil�p�s"},
    {"action.close.description", "kil�p�s a JFreeReportDemo-b�l"},
    {"action.close.mnemonic", new Integer(KeyEvent.VK_K)},

    {"action.print-preview.name", "Nyomtat�si el�n�zet..."},
    {"action.print-preview.description", "Lista megtekint�se"},
    {"action.print-preview.mnemonic", new Integer(KeyEvent.VK_N)},

    {"action.about.name", "N�vjegy..."},
    {"action.about.description", "Inform�ci�k az alkalmaz�sr�l"},
    {"action.about.mnemonic", new Integer(KeyEvent.VK_N)},

    {"menu.file.name", "F�jl"},
    {"menu.file.mnemonic", new Character('F')},
    {"menu.help.name", "S�g�"},
    {"menu.help.mnemonic", new Character('S')},
    {"exitdialog.title", "J�v�hagy�s .."},
    {"exitdialog.message", "Biztos, hogy kil�psz a programb�l?"},


    {"report.definitionnotfound", "Reportdefinition {0} nem tal�lhat� classpath el�r�si �ton"},
    {"report.definitionfailure.message", "Reportdefinition {0} nem tudom bet�lteni."},
    {"report.definitionfailure.title", "Bet�lt� hiba"},
    {"report.definitionnull", "A listadefin�ci� nem lett l�trehoozva"},
    {"error", "Hiba"},
    {"example", "P�lda {0}"}

  };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new DemoResources_hu().generateResourceProperties("hungarian");
    System.exit(0);
  }

}
