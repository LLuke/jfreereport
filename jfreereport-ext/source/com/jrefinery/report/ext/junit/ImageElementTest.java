/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ImageElementTest.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 26.03.2003 : Initial version
 */
package com.jrefinery.report.ext.junit;

import java.awt.Toolkit;
import java.awt.Image;
import java.net.URL;
import javax.swing.table.DefaultTableModel;

import com.jrefinery.report.demo.SampleData2;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.WaitingImageObserver;

public class ImageElementTest
{

  public static void main (String [] args)
    throws Exception
  {
    JFreeReport report = TestSystem.loadReport("/com/jrefinery/report/ext/junit/image-element.xml", new DefaultTableModel());
    if (report == null)
      System.exit (1);

    // add an image as a report property...
    URL imageURL = new String().getClass().getResource("/com/jrefinery/report/demo/gorilla.jpg");
    Image image = Toolkit.getDefaultToolkit().createImage(imageURL);

    report.setProperty("GraphImage", image);
    report.getProperties().setMarked("GraphImage", true);
    TestSystem.showPreviewFrame (report);
  }

}
