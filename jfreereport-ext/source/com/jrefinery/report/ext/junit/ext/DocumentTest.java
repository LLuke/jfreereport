/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * DocumentTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DocumentTest.java,v 1.1 2003/05/26 14:25:35 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.05.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.ext;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;

public class DocumentTest
{
  public static void main(String[] args) throws Exception
  {
    JEditorPane jpe = new JEditorPane();
    jpe.setContentType("text/html");
    jpe.setEditable(false);
    jpe.setText("<html><head><title>A mans tale</title></head><body><p>This is a text<b>bold</b><i>and italic</i></p></body></html>");
    Document doc = jpe.getDocument();
    System.out.println("Length: " + doc.getLength());
    ElementIterator ei = new ElementIterator(doc);
    Element e = ei.next();
    ;
    while (e != null)
    {
      System.out.println(e.toString());
      if (e.isLeaf())
      {
        try
        {
          int start = e.getStartOffset();
          int end = e.getEndOffset();

          System.err.print(doc.getText(start, end - start) + " ");
        }
        catch (BadLocationException le)
        {
          System.err.println("BLE: " + le.offsetRequested());
        }
      }
      e = ei.next();
    }
  }

}
