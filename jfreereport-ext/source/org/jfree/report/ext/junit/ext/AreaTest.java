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
 * AreaTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AreaTest.java,v 1.2 2003/07/03 16:06:19 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 18.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.ext;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class AreaTest extends JPanel
{
  public void paintComponent (final Graphics g)
  {
    final Graphics2D g2 = (Graphics2D) g;
    g2.setPaint(Color.white);
    g2.fill(getBounds());
    g2.setPaint(Color.black);

    final Rectangle2D shape = new Rectangle2D.Float (150, 50, 100, 100);
    final Rectangle2D clip = new Rectangle2D.Float (0, 0, 200, 200);

    final Area a = new Area(clip);
    a.subtract(new Area(clip.createIntersection(shape)));

    g2.fill(a);
  }

  public static void main(final String[] args)
  {
    /*
    JFrame frame = new JFrame();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      {
        System.exit (0);
      }
    });
    frame.setContentPane(new AreaTest());
    frame.setSize(400, 400);
    frame.setVisible(true);
    */

    final Rectangle2D shape = new Rectangle2D.Float (150, 50, 100, 100);
    final Rectangle2D clip = new Rectangle2D.Float (0, 0, 200, 200);

    final Area a = new Area(clip);
    a.subtract(new Area(clip.createIntersection(shape)));

    final Area aLine = new Area (new Line2D.Float (0,0, 10, 10));
    a.subtract(aLine);
    System.out.println(aLine.isEmpty());
    System.out.println(a.isEmpty());
  }

}
