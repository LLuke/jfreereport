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
 * Original Author:  Demeter F. Tamás;
 * Contributor(s):   -;
 *
 * $Id: PDFExportResources_hu.java,v 1.1 2003/07/07 22:44:06 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.pdf.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Hungarian language resource for the PDF export GUI.
 * 
 * @author Demeter F. Tamás
 */
public class PDFExportResources_hu extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PDFExportResources_hu()
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
        {"action.save-as.name", "Mentés mint PDF..."},
        {"action.save-as.description", "Mentés PDF formátumban"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_M)},

        {"file.save.pdfdescription", "PDF dokumentumok"},

        {"error.processingfailed.title", "A lista feldolgozása nem sikerült"},
        {"error.processingfailed.message", "A lista készítése során hiba történt: {0}"},
        {"error.savefailed.message", "Hiba a PDF fájl mentésekor: {0}"},
        {"error.savefailed.title", "Hiba a mentéskor"},

        {"pdfsavedialog.dialogtitle", "Lista mentése PDF-fájlba ..."},
        {"pdfsavedialog.filename", "Fájlnév"},
        {"pdfsavedialog.author", "Szerzõ"},
        {"pdfsavedialog.title", "Cím"},
        {"pdfsavedialog.selectFile", "Válassz egy fájlt"},
        {"pdfsavedialog.security", "Biztonsági beállítások és titkosítás"},
        {"pdfsavedialog.encoding", "Karakterkódolás"},

        {"pdfsavedialog.securityNone", "Nincs titkosítás"},
        {"pdfsavedialog.security40bit", "Titkosítsd 40 bites kulccsal"},
        {"pdfsavedialog.security128bit", "Titkosítsd 128 bites kulccsal"},
        {"pdfsavedialog.userpassword", "Felhasználói jelszó"},
        {"pdfsavedialog.userpasswordconfirm", "Megerõsítés"},
        {"pdfsavedialog.userpasswordNoMatch", "A jelszó nem egyezik"},
        {"pdfsavedialog.ownerpassword", "Tulajdonos jelszó"},
        {"pdfsavedialog.ownerpasswordconfirm", "Megerõsítés"},
        {"pdfsavedialog.ownerpasswordNoMatch", "A tulajdonosi jelszó nem egyezik."},
        {"pdfsavedialog.ownerpasswordEmpty", "A tulajdonosi jelszó üres. A felhasználók "
      + "megváltoztathatják a biztonsági megszorításokat. Folytathatom?"},

        {"pdfsavedialog.warningTitle", "Figyelmeztetés"},
        {"pdfsavedialog.errorTitle", "Hiba"},
        {"pdfsavedialog.targetIsEmpty", "Kérlek nevezd el a pdf fájlt."},
        {"pdfsavedialog.targetIsNoFile", "A kiválasztott cél nem fájl."},
        {"pdfsavedialog.targetIsNotWritable", "A kiválasztott fájl nem írható."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' fájl létezik. Felülírhatom?"},
        {"pdfsavedialog.targetOverwriteTitle", "Felülírhatom a fájlt?"},


        {"pdfsavedialog.allowCopy", "Másolás engedélyezése"},
        {"pdfsavedialog.allowPrinting", "Nyomtatás engedélyezése"},
        {"pdfsavedialog.allowDegradedPrinting", "Gyenge minõségû nyomtatás engedélyezése"},
        {"pdfsavedialog.allowScreenreader", "Képolvasók használatának engedélyezése"},
        {"pdfsavedialog.allowAssembly", "(Újra-)szerkeszés engedélyezése"},
        {"pdfsavedialog.allowModifyContents", "Tartalom módosításának engedélyezése"},
        {"pdfsavedialog.allowModifyAnnotations", "Megjegyzések módosításának engedélyezése"},
        {"pdfsavedialog.allowFillIn", "Kitöltések engedélyezése formanyomtatványban"},

        {"pdfsavedialog.option.noprinting", "Nem nyomtatható"},
        {"pdfsavedialog.option.degradedprinting", "Alacsony minõségû nyomtatás"},
        {"pdfsavedialog.option.fullprinting", "Nyomtatás engedélyezve"},

        {"pdfsavedialog.cancel", "Megszakít"},
        {"pdfsavedialog.confirm", "Jóváhagy"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "hu"});
  }
}
