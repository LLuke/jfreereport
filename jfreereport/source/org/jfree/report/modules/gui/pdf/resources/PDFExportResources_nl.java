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
 * PDFExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.pdf.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class PDFExportResources_nl extends JFreeReportResources
{
  public PDFExportResources_nl()
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
        {"action.save-as.name", "Opslaan Als PDF..."},
        {"action.save-as.description", "Opslaan in PDF-formaat"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_L)},

        {"file.save.pdfdescription", "PDF Documenten"},

        {"error.processingfailed.title", "Fout tijdens report generatie"},
        {"error.processingfailed.message", "Report generatie mislukt: {0}"},
        {"error.savefailed.message", "Opslaan als PDF mislukt: {0}"},
        {"error.savefailed.title", "Fout tijdens opslaan"},

        {"pdfsavedialog.warningTitle", "Waarschuwing"},
        {"pdfsavedialog.dialogtitle", "Opslaan in PDF formaat..."},
        {"pdfsavedialog.filename", "Bestandsnaam"},
        {"pdfsavedialog.author", "Auteur"},
        {"pdfsavedialog.title", "Titel"},
        {"pdfsavedialog.selectFile", "Selecteer Bestand"},
        {"pdfsavedialog.security", "Beveiligingsinstellingen en Encryptie"},
        {"pdfsavedialog.encoding", "Encoderen"},

        {"pdfsavedialog.securityNone", "Geen Beveiliging"},
        {"pdfsavedialog.security40bit", "Encryptie met 40 bit sleutels"},
        {"pdfsavedialog.security128bit", "Encryptie met 128 bit sleutels"},
        {"pdfsavedialog.userpassword", "Gebruikerswachtwoord"},
        {"pdfsavedialog.userpasswordconfirm", "Herhalen"},
        {"pdfsavedialog.userpasswordNoMatch", "De gebruikerswachtwoorden komen niet overeen."},
        {"pdfsavedialog.ownerpassword", "Eigenaarswachtwoord"},
        {"pdfsavedialog.ownerpasswordconfirm", "Herhalen"},
        {"pdfsavedialog.ownerpasswordNoMatch", "De eigenaarswachtwoorden komen niet overeen."},

        {"pdfsavedialog.ownerpasswordEmpty", "Het eigenaarswachtwoord is leeg. Gebruikers kunnen "
      + "beveiligingsinstellingen wijzigen. Doorgaan?"},

        {"pdfsavedialog.errorTitle", "Fout"},
        {"pdfsavedialog.targetIsEmpty", "Geef een bestandsnaam voor het PDF bestand."},
        {"pdfsavedialog.targetIsNoFile", "Doelbestand is ongeldig."},
        {"pdfsavedialog.targetIsNotWritable", "Doelbestand kan niet worden beschreven."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"pdfsavedialog.targetOverwriteTitle", "Bestand overschrijven?"},


        {"pdfsavedialog.allowCopy", "Kopieren toestaan"},
        {"pdfsavedialog.allowPrinting", "Afdrukken toestaan"},
        {"pdfsavedialog.allowDegradedPrinting", "Afdrukken in verminderde kwaliteit toestaan"},
        {"pdfsavedialog.allowScreenreader", "Gebruik van Screenreaders toestaan"},
        {"pdfsavedialog.allowAssembly", "Assemblage toestaan"},
        {"pdfsavedialog.allowModifyContents", "Inhoud wijzigen toestaan"},
        {"pdfsavedialog.allowModifyAnnotations", "Opmerkingen wijzigen toestaan"},
        {"pdfsavedialog.allowFillIn", "Formulierdata invullen toestaan"},

        {"pdfsavedialog.option.noprinting", "Afdrukken niet toegestaan"},
        {"pdfsavedialog.option.degradedprinting", "Afdrukken in verminderde kwaliteit"},
        {"pdfsavedialog.option.fullprinting", "Afdrukken geoorloofd"},

        {"pdfsavedialog.cancel", "Annuleren"},
        {"pdfsavedialog.confirm", "OK"},


      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "nl"});
  }
}
