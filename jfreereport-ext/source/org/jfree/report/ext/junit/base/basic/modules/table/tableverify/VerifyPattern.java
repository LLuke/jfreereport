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
 * VerifyPattern.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: VerifyPattern.java,v 1.1 2003/10/11 21:36:07 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 10.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules.table.tableverify;

import java.util.ArrayList;

/**
 * A pattern to verify the report creationg process. The pattern contains 
 * the expected results for the tablelayouter which is then matched against
 * the created table.
 * 
 * @author Thomas Morgner
 */
public class VerifyPattern
{
  private ArrayList rows;

  public VerifyPattern()
  {
    rows = new ArrayList();
  }

  public void addRow ()
  {
    rows.add (new ArrayList());
  }

  private ArrayList currentRow ()
  {
    if (rows.size() == 0)
    {
      throw new IllegalStateException("No current row");
    }
    return (ArrayList) rows.get(rows.size() - 1);
  }

  public void addCell (VerifyCell content)
  {
    currentRow().add(content);
  }

  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof VerifyPattern))
    { 
      return false;
    }

    final VerifyPattern verifyPattern = (VerifyPattern) o;

    if (!rows.equals(verifyPattern.rows))
    { 
      return false;
    }
    return true;
  }

  public String toString ()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("VerifyPattern={Rows=");
    buffer.append (rows.size());
    buffer.append("}\n");
    for (int i = 0; i < rows.size(); i++)
    {
      buffer.append("   Row:");
      buffer.append(i);
      buffer.append(": {\n");
      ArrayList rowContent = (ArrayList) rows.get(i);
      for (int x = 0; x < rowContent.size(); x++)
      {
        buffer.append("          ");
        buffer.append(rowContent.get(x));
        buffer.append("\n");
      }
      buffer.append("    }\n");
    }
    buffer.append("}");
    return buffer.toString();
  }

  public int hashCode()
  {
    return rows.hashCode();
  }
}
