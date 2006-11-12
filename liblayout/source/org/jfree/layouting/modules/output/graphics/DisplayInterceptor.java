/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.graphics;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JDialog;

import org.jfree.layouting.output.pageable.graphics.GraphicsContentInterceptor;
import org.jfree.layouting.output.pageable.graphics.PageDrawable;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PhysicalPageKey;
import org.jfree.ui.DrawablePanel;

/**
 * Creation-Date: 10.11.2006, 20:41:29
 *
 * @author Thomas Morgner
 */
public class DisplayInterceptor implements GraphicsContentInterceptor
{
  private LogicalPageKey logicalPageKey;

  public DisplayInterceptor(final LogicalPageKey logicalPageKey)
  {
    this.logicalPageKey = logicalPageKey;
  }

  public boolean isLogicalPageAccepted(LogicalPageKey key)
  {
    return logicalPageKey.equals(key);
  }

  public void processLogicalPage(LogicalPageKey key, PageDrawable page)
  {
    final DrawablePanel comp = new DrawablePanel();
    comp.setDrawable(page);

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(comp, BorderLayout.CENTER);

    JDialog dialog = new JDialog();
    dialog.setModal(true);
    dialog.setContentPane(contentPane);
    dialog.setSize(800, 600);
    dialog.setVisible(true);
  }

  public boolean isPhysicalPageAccepted(PhysicalPageKey key)
  {
    return false;
  }

  public void processPhysicalPage(PhysicalPageKey key, PageDrawable page)
  {

  }
}
