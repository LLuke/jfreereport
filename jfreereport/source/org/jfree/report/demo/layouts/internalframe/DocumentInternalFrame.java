/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * MyInternalFrame.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.12.2005 : Initial version
 */
package org.jfree.report.demo.layouts.internalframe;

import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

/**
 * Creation-Date: 11.12.2005, 12:50:21
 *
 * @author Thomas Morgner
 *//* Used by InternalFrameDemo.java. */
public class DocumentInternalFrame extends JInternalFrame
{
  public DocumentInternalFrame()
  {
    super("Document", true, true, true, true);
    final Container contentPane = getContentPane();
    contentPane.setLayout(new FlowLayout());
    contentPane.add(new JLabel("Some text"));
    contentPane.add(new JButton("A button"));
    contentPane.add(new JLabel("Some more text"));
  }

}
