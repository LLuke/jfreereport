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

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

public class PDFExportResources_si extends JFreeReportResources
{
  public PDFExportResources_si()
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
        {"action.save-as.name", "Shrani kot PDF..."},
        {"action.save-as.description", "Shrani v PDF obliki"},

        {"file.save.pdfdescription", "PDF dokumenti"},

        {"error.processingfailed.title", "Obdelava poro\u010Dila ni uspela"},
        {"error.processingfailed.message", "Napaka pri obdelavi poro\u010Dila: {0}"},
        {"error.savefailed.message", "Napaka pri shranjevanju PDF datoteke: {0}"},
        {"error.savefailed.title", "Napaka pri shranjevanju"},

        {"pdfsavedialog.dialogtitle", "Shranjevanje poro\u010Dila v PDF datoteko ..."},
        {"pdfsavedialog.filename", "Ime datoteke"},
        {"pdfsavedialog.author", "Avtor"},
        {"pdfsavedialog.title", "Naslov"},
        {"pdfsavedialog.selectFile", "Izberite datoteko"},
        {"pdfsavedialog.security", "Varnostne nastavitve in kodiranje"},
        {"pdfsavedialog.encoding", "Kodiranje"},

        {"pdfsavedialog.securityNone", "Ni Varnosti"},
        {"pdfsavedialog.security40bit", "Kodiraj z 40 bit-nim klju\u010Dem"},
        {"pdfsavedialog.security128bit", "Kodiraj z 128 bit-nim klju\u010Dem"},
        {"pdfsavedialog.userpassword", "Uporabni\u0161ko geslo"},
        {"pdfsavedialog.userpasswordconfirm", "Potrdi"},
        {"pdfsavedialog.userpasswordNoMatch", "Uporabni\u0161ki gesli se ne ujemata."},
        {"pdfsavedialog.ownerpassword", "Lastni\u0161ko geslo"},
        {"pdfsavedialog.ownerpasswordconfirm", "Potrdi"},
        {"pdfsavedialog.ownerpasswordNoMatch", "Lastni\u0161ki gesli se ne ujemata."},
        {"pdfsavedialog.ownerpasswordEmpty",
         "Lastni\u0161ko geslo je prazno. Uporabniki lahko spremenijo varnostne omejitve."
         + " \u017Delite vseeno nadaljevati?"},

        {"pdfsavedialog.warningTitle", "Opozorilo"},
        {"pdfsavedialog.errorTitle", "Napaka"},
        {"pdfsavedialog.targetIsEmpty", "Navedite ime PDF datoteke."},
        {"pdfsavedialog.targetIsNoFile", "Izbrani cilj ni navadna datoteka."},
        {"pdfsavedialog.targetIsNotWritable", "V izbrano datoteko ni mogo\u010De pisati."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Datoteka ''{0}'' obstaja. Ali jo \u017Eelite prepisati?"},
        {"pdfsavedialog.targetOverwriteTitle", "Ali \u017Eelite prepisati datoteko?"},


        {"pdfsavedialog.allowCopy", "Dovoli kopiranje"},
        {"pdfsavedialog.allowPrinting", "Dovoli tiskanje"},
        {"pdfsavedialog.allowDegradedPrinting", "Dovoli tiskanje slab\u0161e kakovosti"},
        {"pdfsavedialog.allowScreenreader", "Dovoli uporabo programov za zajem slike"},
        {"pdfsavedialog.allowAssembly", "Dovoli (ponovno) sestavo"},
        {"pdfsavedialog.allowModifyContents", "Dovoli spreminjanje vsebin"},
        {"pdfsavedialog.allowModifyAnnotations", "Dovoli spreminjanje pripomb"},
        {"pdfsavedialog.allowFillIn", "Dovoli vnos v obrazec"},

        {"pdfsavedialog.option.noprinting", "Ni mo\u017Enosti tiskanja"},
        {"pdfsavedialog.option.degradedprinting", "Slab\u0161a kakovost tiskanja"},
        {"pdfsavedialog.option.fullprinting", "Tiskanje dovoljeno"},

        {"pdfsavedialog.cancel", "Prekli\u010Di"},
        {"pdfsavedialog.confirm", "Potrdi"},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "si"});
  }
}
