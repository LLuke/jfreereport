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
 * JFreeReportResources_de.java
 * ----------------------------
 *
 * $Id: JFreeReportResources_de.java,v 1.34 2003/06/29 16:59:27 taqua Exp $
 *
 * Changes
 * -------
 * 16-Feb-2003 : Corrected version submitted by Helmut Leininger
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * German Language Resources.
 *
 * @author Thomas Morgner
 */
public class JFreeReportResources_de extends JFreeReportResources
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
        {"action.close.name", "Schliessen"},
        {"action.close.description", "Beendet die Seitenansicht"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_B)},

        {"action.gotopage.name", "Gehe zu ..."},
        {"action.gotopage.description", "Wechselt zu einer bestimmten Seite im Bericht."},

        {"dialog.gotopage.message", "Seitenzahl eingeben"},
        {"dialog.gotopage.title", "Gehe zu Seite ..."},

        {"action.about.name", "\u00dcber..."},
        {"action.about.description", "Informationen \u00fcber JFreeReport"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Anfang"},
        {"action.firstpage.description", "Gehe zum StartState"},

        {"action.lastpage.name", "Ende"},
        {"action.lastpage.description", "Gehe zum Ende"},

        {"action.back.name", "Zur\u00fcck"},
        {"action.back.description", "Wechselt zur vorherigen Seite"},

        {"action.forward.name", "N\u00e4chste"},
        {"action.forward.description", "Wechselt zur n\u00e4chsten Seite"},

        {"action.zoomIn.name", "Vergr\u00f6ssern"},
        {"action.zoomIn.description",
         "Zeigt die aktuelle Seite in einem gr\u00f6sseren Masstab an"},

        {"action.zoomOut.name", "Verkleinern"},
        {"action.zoomOut.description",
         "Zeigt die aktuelle Seite in einem kleineren Masstab an"},

        {"preview-frame.title", "Seitenansicht"},

        {"menu.file.name", "Datei"},
        {"menu.file.mnemonic", new Character('D')},

        {"menu.help.name", "Hilfe"},
        {"menu.help.mnemonic", new Character('H')},

        {"menu.zoom.name", "Zoom"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"statusline.pages", "Seite {0} von {1}"},
        {"statusline.error", "Die ReportGenerierung ist fehlgeschlagen: {0}"},
        {"statusline.repaginate", "Erzeuge Seitenumbr\u00fcche ..."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "de"});
  }

}
