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
 * VerifyCell.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: VerifyCell.java,v 1.1 2003/10/11 21:36:07 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 10.10.2003 : Initial version
 */

package org.jfree.report.ext.junit.base.basic.modules.table.tableverify;

public class VerifyCell
{
  private int x;
  private int y;
  private int colspan;
  private int rowspan;

  public VerifyCell(int x, int y, int colspan, int rowspan)
  {
    this.x = x;
    this.y = y;
    this.colspan = colspan;
    this.rowspan = rowspan;
  }

  public int getColspan()
  {
    return colspan;
  }

  public int getRowspan()
  {
    return rowspan;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public String toString ()
  {
    StringBuffer buffer = new StringBuffer ();
    buffer.append ("VerifyCell={");
    buffer.append(x);
    buffer.append(",");
    buffer.append(y);
    buffer.append(",");
    buffer.append(colspan);
    buffer.append(",");
    buffer.append(rowspan);
    buffer.append("}");
    return buffer.toString();
  }

  public boolean equals(Object o)
  {
    if (this == o)
    { 
      return true;
    }
    if (!(o instanceof VerifyCell))
    { 
      return false;
    }

    final VerifyCell verifyCell = (VerifyCell) o;

    if ((colspan != verifyCell.colspan) || 
        (rowspan != verifyCell.rowspan) ||
        (x != verifyCell.x) ||
        (y != verifyCell.y))
    {
     return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = x;
    result = 29 * result + y;
    result = 29 * result + colspan;
    result = 29 * result + rowspan;
    return result;
  }
}
