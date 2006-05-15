/**
 * ========================================
 * Pixie : a free Java vector image library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/pixie/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * PixieViewer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.pixie;

import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.pixie.wmf.WmfFile;

/**
 * Creation-Date: 13.05.2006, 09:47:26
 *
 * @author Thomas Morgner
 */
public class PixieViewer extends JFrame
{
  public PixieViewer(String filename) throws IOException
  {
    final WmfFile wmf = new WmfFile(filename, 800, 600);
    System.out.println(wmf);
    Image img = wmf.replay();
    setContentPane(new JLabel(new ImageIcon(img)));
  }

  public static void main(String[] args)
          throws IOException
  {
    if (args.length == 0)
    {
      System.err.println("Need a file parameter.");
      System.exit(1);
    }

    PixieViewer viewer = new PixieViewer(args[0]);
    viewer.pack();
    viewer.setVisible(true);
  }
}
