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
 * JFreeReportResources_fr.java
 * ----------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  PR;
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportResources_fr.java,v 1.10 2003/06/29 16:59:27 taqua Exp $
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.awt.event.KeyEvent;

/**
 * French Language Resources.
 *
 * @author PR
 */
public class JFreeReportResources_fr extends JFreeReportResources
{
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
        {"action.close.name", "Fermer"},
        {"action.close.description", "Fermer l'aperçu avant impression"},
        {"action.close.mnemonic", new Integer(KeyEvent.VK_F)},

        {"action.gotopage.name", "Aller à la page ..."},
        {"action.gotopage.description", "Voir une page directement"},
        {"action.gotopage.mnemonic", new Integer(KeyEvent.VK_A)},

        {"dialog.gotopage.message", "Entrer un numéro de page"},
        {"dialog.gotopage.title", "Aller à la page"},

        {"action.about.name", "A propos..."},
        {"action.about.description", "Information à propos de l'application"},
        {"action.about.mnemonic", new Integer(KeyEvent.VK_A)},

        {"action.firstpage.name", "Début"},
        {"action.firstpage.description", "Aller à la première page"},

        {"action.back.name", "Précédent"},
        {"action.back.description", "Aller à la page précédente"},

        {"action.forward.name", "Suivant"},
        {"action.forward.description", "Aller à la page suivante"},

        {"action.lastpage.name", "Fin"},
        {"action.lastpage.description", "Aller à la dernière page"},

        {"action.zoomIn.name", "Agrandir"},
        {"action.zoomIn.description", "Agrandir"},

        {"action.zoomOut.name", "Rétrécir"},
        {"action.zoomOut.description", "Rétrécir"},

        // preview frame...
        {"preview-frame.title", "Aperçu avant impression"},

        // menu labels...
        {"menu.file.name", "Fichier"},
        {"menu.file.mnemonic", new Character('F')},

        {"menu.navigation.name", "Navigation"},
        {"menu.navigation.mnemonic", new Character('N')},

        {"menu.zoom.name", "Zoom"},
        {"menu.zoom.mnemonic", new Character('Z')},

        {"menu.help.name", "Aide"},
        {"menu.help.mnemonic", new Character('A')},

        {"statusline.pages", "Page {0} de {1}"},
        {"statusline.error", "Reportgeneration à produit une erreur: {0}"},
        {"statusline.repaginate", "Calcule de la coupure des pages, veuillez patienter."},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{JFreeReportResources.class.getName(), "fr"});
  }


}
