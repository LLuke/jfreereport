/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * JFreeReportResources_nl.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Hendri Smit;
 * Contributor(s):   -;
 *
 * $Id: JFreeReportResources_nl.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * Dutch Language Resources.
 *
 * @author Hendri Smit
 */
public class JFreeReportResources_nl extends JFreeReportResources
{

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
        {"action.close.name", "Sluiten"},
        {"action.close.description", "Sluit afdrukvoorbeeld"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_S)},

        {"action.gotopage.name", "Ga naar..."},
        {"action.gotopage.description", "Ga naar opgegeven pagina"},

        {"dialog.gotopage.message", "Geef pagina nummer"},
        {"dialog.gotopage.title", "Ga naar pagina ..."},

        {"action.about.name", "Info..."},
        {"action.about.description", "Informatie over de applicatie"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_I)},

        {"action.firstpage.name", "Begin"},
        {"action.firstpage.description", "Ga naar de eerste pagina"},

        {"action.lastpage.name", "Einde"},
        {"action.lastpage.description", "Ga naar de laatste pagina"},

        {"action.back.name", "Terug"},
        {"action.back.description", "Ga naar de vorige pagina"},

        {"action.forward.name", "Verder"},
        {"action.forward.description", "Ga naar de volgende pagina"},

        {"action.zoomIn.name", "Vergroten"},
        {"action.zoomIn.description", "Inzoomen"},

        {"action.zoomOut.name", "Verkleinen"},
        {"action.zoomOut.description", "Uitzoomen"},

        {"preview-frame.title", "Afdrukvoorbeeld"},

        {"menu.file.name", "Bestand"},
        {"menu.file.mnemonic", new Character('B')},

        {"menu.help.name", "Help"},
        {"menu.help.mnemonic", new Character('H')},

        {"menu.navigation.name", "Navigatie"},
        {"menu.navigation.mnemonic", new Character ('N')},

        {"menu.zoom.name", "In-/Uitzoomen"},
        {"menu.zoom.mnemonic", new Character ('Z')},

        {"statusline.pages", "Pagina {0} van {1}"},
        {"statusline.error", "Er is een fout ontstaan in de report generatie: {0}"},
        {"statusline.repaginate", "Paginanummering berekenen..."},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "nl"});
  }


}
