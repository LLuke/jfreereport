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
 * VerifyBandArea.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 10.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules.table.tableverify;

import java.awt.geom.Rectangle2D;

import org.jfree.report.modules.output.table.base.TableBandArea;

public class VerifyBandArea extends TableBandArea
{
  private String name;

  public VerifyBandArea(final Rectangle2D outerBounds, String name)
  {
    super(outerBounds);
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public String toString ()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("VerifyCellContent={name=");
    buffer.append(getName());
    buffer.append(", bounds=");
    buffer.append(getBounds());
    buffer.append("}");
    return buffer.toString();
  }
}
