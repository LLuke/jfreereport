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
 * DemoResources.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: DemoResources.java,v 1.4 2003/08/25 14:29:28 taqua Exp $
 *
 * Changes
 * -------
 * 27-Mar-2002 : Version 1 (DG);
 * 16-May-2002 : Load images from jar
 *
 */

package org.jfree.report.demo.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;

/**
 * User interface items for the JFreeReport demonstration application.  These have been put into
 * a ResourceBundle to ease localisation of the application.
 *
 * @author David Gilbert
 */
public class DemoResources_fr extends JFreeReportResources
{
  /**
   * Default Constructor.
   */
  public DemoResources_fr()
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

    {"action.close.name", "Quitter"},
    {"action.close.description", "Quitte JFreeReportDemo"},
    {"action.close.mnemonic", new Integer(KeyEvent.VK_Q)},
    {"action.close.accelerator", createMenuKeystroke(KeyEvent.VK_Q)},

    {"action.print-preview.name", "Aperçu avant Impression..."},
    {"action.print-preview.description", "Pré visualise le rapport"},
    {"action.print-preview.mnemonic", new Integer(KeyEvent.VK_A)},
    {"action.print-preview.accelerator", createMenuKeystroke(KeyEvent.VK_A)},
    
    {"action.about.name", "A propos..."},
    {"action.about.description", "Information à propos de l'application"},
    {"action.about.mnemonic", new Integer(KeyEvent.VK_P)},
    
    {"menu.file.name", "Fichier"},
    {"menu.file.mnemonic", new Character('F')},
    {"menu.help.name", "Aide"},
    {"menu.help.mnemonic", new Character('i')},
    {"exitdialog.title", "Confirmation .."},
    {"exitdialog.message", "Etes vous sûr de vouloir quitter le programme?"},


    {"report.definitionnotfound", "Reportdefinition {0} n'a pas été trouvé dans le classpath."},
    {"report.definitionfailure.message", "Reportdefinition {0} n'a pas pu être chargé."},
    {"report.definitionfailure.title", "Erreur de chargement"},
    {"report.definitionnull", "Reportdefinition n'a pas été généré"},
    {"error", "Erreur"},
    {"example", "Example {0}"},

    {"report.previewfailure.message", "Une erreur est survenue lors de l'initialisation de l'aperçu avant impression."},
    {"report.previewfailure.title", "Erreur aperçu du rapport"},

  };

}
