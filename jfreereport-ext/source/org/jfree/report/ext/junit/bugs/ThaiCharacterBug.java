/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------
 * ThaiCharacterBug.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ThaiCharacterBug.java,v 1.1 2003/07/08 14:21:47 taqua Exp $
 *
 * Changes
 * -------
 * 09.04.2003 : Initial version
 */
package org.jfree.report.ext.junit.bugs;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.CharacterEntityParser;

public class ThaiCharacterBug
{
  public static JFreeReport getReport() throws Exception
  {
    String test =
        CharacterEntityParser.createXMLEntityParser().decodeEntities
        ("Sample Thai chars: &#3648;&#3614;&#3636;&#3656;&#3617;, &#3621;&#3641;&#3585;&#3588;&#3657;&#3634;");
    //String test = "\u3648\u3614\u3636\u3656\u3617\u3621\u3641\u3585\u3588\u3657\u3634";
    test = new String(test.getBytes("iso-8859-1"), "TIS620");

    final Element e = LabelElementFactory.createLabelElement(null,
        new Rectangle2D.Float(10, 10, 250, 50),
        null,
        ElementAlignment.CENTER,
        new FontDefinition("Serif", 14),
        test);
    final JFreeReport report = new JFreeReport();
    report.getReportHeader().addElement(e);
    return report;
  }

  public static void main(final String[] args) throws Exception
  {
    final PreviewFrame d = new PreviewFrame(getReport());
    d.pack();
    d.addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(final WindowEvent e)
      {
        System.exit(0);
      }
    });
    d.setVisible(true);
  }
}
