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
 * HtmlExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Hendri Smit;
 * Contributor(s):   -;
 *
 * $Id: HtmlExportResources_nl.java,v 1.2 2003/08/19 13:37:24 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.html.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Dutch language resource for the Html export GUI.
 * 
 * @author Hendri Smit
 */
public class HtmlExportResources_nl extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public HtmlExportResources_nl()
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
        {"action.export-to-html.name", "Exporteren naar HTML..."},
        {"action.export-to-html.description", "Opslaan in HTML formaat"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},

        {"error.processingfailed.title", "Fout tijdens report generatie"},
        {"error.processingfailed.message", "Report generatie mislukt: {0}"},
        {"error.savefailed.message", "Opslaan als PDF mislukt: {0}"},
        {"error.savefailed.title", "Fout tijdens opslaan"},
        {"error.validationfailed.message", "Validatie van gebruikersinvoer mislukt."},
        {"error.validationfailed.title", "Fout tijdens validatie"},

        {"htmlexportdialog.dialogtitle", "Opslaan als HTML bestand..."},
        {"htmlexportdialog.filename", "Bestandsnaam"},
        {"htmlexportdialog.author", "Auteur"},
        {"htmlexportdialog.title", "Titel"},

        {"htmlexportdialog.warningTitle", "Waarschuwing"},
        {"htmlexportdialog.errorTitle", "Fout"},
        {"htmlexportdialog.targetIsEmpty",
         "Geef een bestandsnaam voor het HTML bestand."},
        {"htmlexportdialog.targetIsNoFile",
         "Doelbestand is ongeldig."},
        {"htmlexportdialog.targetIsNotWritable",
         "Doelbestand kan niet worden beschreven."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"htmlexportdialog.targetOverwriteTitle", "Bestand overschrijven?"},

        {"htmlexportdialog.cancel", "Annuleren"},
        {"htmlexportdialog.confirm", "OK"},

        {"htmlexportdialog.strict-layout", "Exacte layout gebruiken."},
        {"htmlexportdialog.copy-external-references", "Externe referenties kopiëren."},
        {"htmlexportdialog.datafilename", "Data map"},
        {"htmlexportdialog.encoding", "Encoderen"},
        {"htmlexportdialog.generate-html4", "Genereer HTML4 uitvoer"},
        {"htmlexportdialog.generate-xhtml", "Genereer XHTML1.0 uitvoer"},

        {"htmlexportdialog.selectDirFile", "Selecteer Bestand"},
        {"htmlexportdialog.selectStreamFile", "Selecteer Bestand"},
        {"htmlexportdialog.selectZipFile", "Selecteer Bestand"},

        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "De opgegeven data map bestaat niet, "
      + "moeten de ontbrekende submappen worden gecreëerd?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Data map aanmaken?"},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "Data map is ongeldig."},
        {"htmlexportdialog.targetPathIsAbsolute",
         "Het opgegeven doel is geen map in het ZIP-bestand."},


      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{HtmlExportResources.class.getName(), "nl"});
  }
}
