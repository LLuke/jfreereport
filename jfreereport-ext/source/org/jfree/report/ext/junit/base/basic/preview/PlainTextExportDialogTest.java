/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * PlainTextExportDialogTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextExportDialogTest.java,v 1.3 2003/09/09 10:27:57 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 29.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.preview;

import junit.framework.TestCase;
import org.jfree.report.modules.gui.plaintext.PlainTextExportDialog;
import org.jfree.report.util.Log;

public class PlainTextExportDialogTest extends TestCase
{
  public PlainTextExportDialogTest()
  {
  }

  public PlainTextExportDialogTest(final String s)
  {
    super(s);
  }

  public void testSelectEncoding ()
  {
    if (System.getProperty("java.awt.headless", "false").equals("true"))
    {
      Log.debug ("Headless enviroment: Do not perform test: " + PlainTextExportDialogTest.class);
      return;
    }
    final PlainTextExportDialog d = new PlainTextExportDialog();
    d.setModal(true);
    d.setEncoding("Cp850");
    assertEquals("Cp850", d.getEncoding());

    d.setSelectedPrinter(PlainTextExportDialog.TYPE_EPSON9_OUTPUT);
    assertEquals("Cp850", d.getEncoding());

    d.setSelectedPrinter(PlainTextExportDialog.TYPE_EPSON24_OUTPUT);
    assertEquals("Cp850", d.getEncoding());

    d.setSelectedPrinter(PlainTextExportDialog.TYPE_IBM_OUTPUT);
    assertEquals("Cp850", d.getEncoding());

    d.setSelectedPrinter(PlainTextExportDialog.TYPE_PLAIN_OUTPUT);
    assertEquals("Cp850", d.getEncoding());
  }
}
