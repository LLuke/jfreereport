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
 * ----------------------------
 * ConverterResources.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  PR;
 * Contributor(s):   -;
 *
 * $Id: ConverterResources_fr.java,v 1.6 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 */
package org.jfree.report.modules.gui.converter.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * French language resource for the report converter GUI.
 *
 * @author PR
 */
public class ConverterResources_fr extends JFreeReportResources
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

        {"convertdialog.targetIsEmpty", "Le fichier cible n'est pas spécifié"},
        {"convertdialog.errorTitle", "Erreur"},
        {"convertdialog.targetIsNoFile",
         "Le fichier cible spécifié n'est pas un fichier ordinaire."},
        {"convertdialog.targetIsNotWritable", "Le fichier sélectionner est en lecture seule."},
        {"convertdialog.targetOverwriteConfirmation",
         "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
        {"convertdialog.targetOverwriteTitle", "Ecraser le fichier?"},
        {"convertdialog.encoding", "Encodage"},
        {"convertdialog.targetFile", "Fichier cible"},
        {"convertdialog.sourceIsEmpty", "Le fichier source n'est pas spécifié"},
        {"convertdialog.sourceIsNoFile",
         "Le fichier source spécifié n'est pas un fichier ordinaire."},
        {"convertdialog.sourceIsNotReadable", "Le fichier source n'est pas lisible."},
        {"convertdialog.sourceFile", "Fichier source"},

        {"convertdialog.action.selectTarget.name", "Sélectionner"},
        {"convertdialog.action.selectTarget.description", "Sélectionner un fichier cible."},
        {"convertdialog.action.selectSource.name", "Sélectionner"},
        {"convertdialog.action.selectSource.description", "Sélectionner un fichier source."},
        {"convertdialog.action.convert.name", "Convertir"},
        {"convertdialog.action.convert.description", "Convertir les fichiers source."},

        {"convertdialog.title", "Convertisseur de rapport"},


      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{ConverterResources.class.getName(), "fr"});
  }

}
