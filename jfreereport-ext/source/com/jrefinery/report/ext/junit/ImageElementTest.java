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
 * $Id: ImageElementTest.java,v 1.2 2003/04/11 19:29:27 taqua Exp $
 *
 * Changes
 * -------
 * 26.03.2003 : Initial version
 */
package com.jrefinery.report.ext.junit;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.table.DefaultTableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.WaitingImageObserver;

public class ImageElementTest
{
  private static Image createImage(Image source)
  {
    double scale = 0.2;
    double width = 300;
    double height = 400;

    WaitingImageObserver obs = new WaitingImageObserver(source);
    obs.waitImageLoaded();

    BufferedImage bImage = new BufferedImage((int) (width * scale), (int) (height * scale), BufferedImage.TYPE_INT_ARGB);

    Graphics2D graph = bImage.createGraphics();
    graph.setTransform(AffineTransform.getScaleInstance(scale, scale));
    if (graph.drawImage(source, AffineTransform.getScaleInstance(scale, scale), null) == false)
    {
      Log.debug("No i won't print it  :) ");
    }
    Log.debug("Image: " + source.getWidth(null) + " -> " + source.getHeight(null));
    graph.dispose();
    return bImage;
  }

  public static void main(String[] args)
      throws Exception
  {
    // add an image as a report property...
    URL imageURL = new String().getClass().getResource("/com/jrefinery/report/demo/gorilla.jpg");
    Image image = Toolkit.getDefaultToolkit().createImage(imageURL);

    Object[][] data = {{createImage(image), createImage(image), createImage(image)}};
    Object[] names = {"Foto1", "Foto2", "Foto3"};
    DefaultTableModel mod = new DefaultTableModel(data, names);

    JFreeReport report = TestSystem.loadReport("/com/jrefinery/report/ext/junit/image-element.xml", mod);
    if (report == null)
      System.exit(1);

    report.setProperty("GraphImage", image);
    report.getProperties().setMarked("GraphImage", true);
    TestSystem.showPreviewFrame(report);
  }

}
