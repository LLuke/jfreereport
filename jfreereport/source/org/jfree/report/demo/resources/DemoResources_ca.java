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
 * ------------------
 * DemoResources_ca.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  Marc Casas
 * Contributor(s):   -;
 *
 * $Id: $
 *
 * Changes
 * -------
 * 05-09-2003 : Version 1;
 *
 */

package org.jfree.report.demo.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 * Catalan translation
 *
 * @author Marc Casas
 */
public class DemoResources_ca extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public DemoResources_ca()
  {
  }

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
    {"main-frame.title.pattern", "JFreeReport {0} Demo"},

    {"action.close.name", "Sortir"},
    {"action.close.description", "Surt del JFreeReportDemo"},
    {"action.close.mnemonic", new Integer(KeyEvent.VK_S)},
    {"action.close.accelerator", createMenuKeystroke(KeyEvent.VK_O)},

    {"action.print-preview.name", "Vista prèvia..."},
    {"action.print-preview.description", "Vista prèvia de l'informe"},
    {"action.print-preview.mnemonic", new Integer(KeyEvent.VK_P)},
    {"action.print-preview.accelerator", createMenuKeystroke(KeyEvent.VK_P)},
    {"action.print-preview.small-icon",
     getIcon("org/jfree/report/modules/gui/base/resources/PrintPreview16.gif")},
    {"action.print-preview.icon",
     getIcon("org/jfree/report/modules/gui/base/resources/PrintPreview24.gif")},

    {"action.about.name", "Quan a..."},
    {"action.about.description", "Informació sobre l'aplicació"},
    {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},
    {"action.about.small-icon", getIcon("org/jfree/report/modules/gui/base/resources/About16.gif")},
    {"action.about.icon", getIcon("org/jfree/report/modules/gui/base/resources/About24.gif")},

    {"menu.file.name", "Fitxer"},
    {"menu.file.mnemonic", new Character('F')},
    {"menu.help.name", "Ajuda"},
    {"menu.help.mnemonic", new Character('J')},
    {"exitdialog.title", "Confirmació.."},
    {"exitdialog.message", "Segur que vols sortir del programa?"},


    {"report.definitionnotfound", "La definició de l'informe {0} no s'ha trobat al classpath"},
    {"report.definitionfailure.message", "La definició de l'informe {0} no s'ha pogut carregar."},
    {"report.definitionfailure.title", "Error de cargador"},
    {"report.definitionnull", "La definició de l'informe no s'ha generat"},
    {"error", "Error"},
    {"example", "Exemple {0}"},

    {"report.previewfailure.message", 
      "Ha ocorregut un error mentre s'inicialitzava la finestra de " +      "previsualització."},
    {"report.previewfailure.title", "Error de visualització de l'informe"},

  };


  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{DemoResources.class.getName(), "ca"});
  }

}
