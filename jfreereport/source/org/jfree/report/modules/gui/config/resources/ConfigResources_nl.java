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
 * ------------------------------
 * ConfigResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigResources_nl.java,v 1.1 2003/11/05 17:33:16 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;

/**
 * Provides localizable resources for the Configuration editor.
 *
 * @author Thomas Morgner
 */
public class ConfigResources_nl extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public ConfigResources_nl()
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
        {"action.add-entry.name", "Item toevoegen"},
        {"action.add-entry.small-icon",
          getIcon("org/jfree/report/modules/gui/config/resources/Add16.gif")},

        {"action.remove-entry.name", "Item verwijderen"},
        {"action.remove-entry.small-icon",
          getIcon("org/jfree/report/modules/gui/config/resources/Remove16.gif")},

        {"action.import.name", "Importeren"},
        {"action.import.small-icon",
          getIcon("org/jfree/report/modules/gui/config/resources/Import24.gif")},

        {"action.new.name", "Nieuw"},
        {"action.new.small-icon",
          getIcon("org/jfree/report/modules/gui/config/resources/New24.gif")},

        {"action.load.name", "Openen"},
        {"action.load.small-icon",
          getIcon("org/jfree/report/modules/gui/config/resources/Open24.gif")},

        {"action.save.name", "Opslaan"},
        {"action.save.small-icon",
          getIcon("org/jfree/report/modules/gui/config/resources/Save24.gif")},

        {"action.exit.name", "Afsluiten"},
        {"action.update.name", "Bijwerken"},
        {"action.cancel.name", "Annuleren"},
        {"action.boolean.name", "Boolean"},

        {"action.remove-enum-entry.name", "Verwijderen"},
        {"action.add-enum-entry.name", "Toevoegen"},
        {"action.update-enum-entry.name", "Bijwerken"},

        {"default-editor.error-icon",
          getIcon("org/jfree/report/modules/gui/config/resources/Stop16.gif")},

        {"config-editor.title", "Configuratie Editor"},

        {"config-description-editor.import-complete", "Importeren is gedaan"},
        {"config-description-editor.unnamed-entry", "<naamloos item>"},
        {"config-description-editor.title", "Configuratie Definitie Editor"},
        {"config-description-editor.xml-files", "XML bestanden"},
        {"config-description-editor.welcome", "Welcome..."},
        {"config-description-editor.baseclass", "Basis klasse:"},
        {"config-description-editor.text-editor-message",
            "De tekst editor eist geen setup."},
        {"config-description-editor.keyname", "Key-Naam:"},
        {"config-description-editor.description", "Omschrijving:"},
        {"config-description-editor.type", "Type:"},
        {"config-description-editor.global", "Global:"},
        {"config-description-editor.hidden", "Verborgen:"},
        {"config-description-editor.type-text", "Tekst"},
        {"config-description-editor.type-class", "Klasse"},
        {"config-description-editor.type-enum", "Enum"},
        {"config-description-editor.save-complete", "Opslaan was sucessvol."},
        {"config-description-editor.save-failed", "Opslaan mislukt: {0}"},
        {"config-description-editor.load-complete", "Laden was successvol."},
        {"config-description-editor.load-failed", "Laden mislukt: {0}"},
        {"config-description-editor.update-complete", "Bijwerken van het item gedaan."},
      };

  public static void main(final String[] args)
   {
     new ConfigResources_nl().generateResourceProperties("dutch");
     System.exit(0);
   }

}
