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
 * Original Author:  Thomas Dilts;
 * Contributor(s):   -;
 *
 * $Id: HtmlExportResources_sv.java,v 1.4 2003/08/25 14:29:30 taqua Exp $
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
 * Swedish language resource for the Html export GUI.
 *
 * @author Thomas Dilts
 */
public class HtmlExportResources_sv extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public HtmlExportResources_sv()
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
        {"action.export-to-html.name", "Exportera till html..."},
        {"action.export-to-html.description", "Spara till HTML format"},
        {"action.export-to-html.mnemonic", new Integer(KeyEvent.VK_H)},
        {"action.export-to-html.accelerator", createMenuKeystroke(KeyEvent.VK_H)},

        {"htmlexportdialog.dialogtitle", "Exportera rapporten till en Html-Fil ..."},

        {"htmlexportdialog.filename", "Filnamn"},
        {"htmlexportdialog.datafilename", "Data katalog"},
        {"htmlexportdialog.copy-external-references", "Kopiera externa referenser"},

        {"htmlexportdialog.author", "Författaren"},
        {"htmlexportdialog.title", "Titeln"},
        {"htmlexportdialog.encoding", "Kodningen"},
        {"htmlexportdialog.selectZipFile", "Välja filen"},
        {"htmlexportdialog.selectStreamFile", "Välja filen"},
        {"htmlexportdialog.selectDirFile", "Välja filen"},

        {"htmlexportdialog.strict-layout", "Använda stränga tabell format reglar för exporten."},
        {"htmlexportdialog.generate-xhtml", "Generera XHTML 1.0 output"},
        {"htmlexportdialog.generate-html4", "Generera HTML 4.0 output"},

        {"htmlexportdialog.warningTitle", "Varning"},
        {"htmlexportdialog.errorTitle", "Fel"},
        {"htmlexportdialog.targetIsEmpty", "Ange filnamnet till Html filen."},
        {"htmlexportdialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"htmlexportdialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"htmlexportdialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"htmlexportdialog.targetOverwriteTitle", "Skriva över filen?"},

        {"htmlexportdialog.cancel", "Avbryt"},
        {"htmlexportdialog.confirm", "Konfirmera"},
        {"htmlexportdialog.targetPathIsAbsolute",
         "Mål path är en absolut katalog.\n" + 
         "Ange katalogen i en ZIP fil."},
        {"htmlexportdialog.targetDataDirIsNoDirectory",
         "Angiven katalog är ogiltig."},
        {"htmlexportdialog.targetCreateDataDirConfirmation",
         "Angiven katalog finns inte.\n"
      + "Ska alla underkatalog skapas?"},
        {"htmlexportdialog.targetCreateDataDirTitle", "Skapa katalogen?"},

        {"htmlexportdialog.html-documents", "HTML dokument"},
        {"htmlexportdialog.zip-archives", "ZIP arkiv"},

        {"htmlexportdialog.stream-export", "Fil stream export"},
        {"htmlexportdialog.directory-export", "Katalog export"},
        {"htmlexportdialog.zip-export", "ZIP fil export"},

        {"error.processingfailed.title", "Report generation misslyckades"},
        {"error.processingfailed.message", "Fel när rapporten skapades: {0}"},
        {"error.savefailed.message", "Fel inträffade under PDF sparning: {0}"},
        {"error.savefailed.title", "Fel under sparningen"},
        {"error.validationfailed.message", "Fel under valideringen av angiven information."},
        {"error.validationfailed.title", "Fel under valideringen"},

        {"html-export.progressdialog.title", "Export till HTML format på gång..."},
        {"html-export.progressdialog.message", "Rapporten exporterades till en HTML fil ..."},

      };

      
  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{HtmlExportResources.class.getName(), "sv"});
  }
  
}
