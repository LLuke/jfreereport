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
 * SwingResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SwingResources.java,v 1.1 2003/09/12 17:51:05 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report.ext;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.UIManager;

/**
 * Prints a list of all defined Swing-UI properties.
 * 
 * @author Thomas Morgner
 */
public final class SwingResources
{
  /**
   * DefaultConstructor.
   */
  private SwingResources ()
  {
  }
  /**
   * Starts the program.
   * @param args ignored
   */
  public static void main(String[] args)
  {
    Hashtable table = UIManager.getDefaults();
    Enumeration keys = table.keys();
    ArrayList list = new ArrayList();

    char[] pad = new char[40];
    Arrays.fill(pad, ' ');

    while (keys.hasMoreElements())
    {
      Object key = keys.nextElement();
      Object value = table.get(key);
      StringBuffer b = new StringBuffer(key.toString());
      if (b.length() < 40)
      {
        b.append(pad, 0, 40 - b.length());
      }
      b.append (" = ");
      b.append (value);
      list.add (b.toString());
    }

    Collections.sort(list);
    for (int i = 0; i < list.size(); i++)
    {
      System.out.println(list.get(i));
    }
    System.exit(0);
  }
}
