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
 * LineShapeCreation.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LineShapeCreation.java,v 1.1 2003/03/26 22:54:40 taqua Exp $
 *
 * Changes
 * -------
 * 22.03.2003 : Initial version
 */
package com.jrefinery.report.ext.junit.bugs;

import java.awt.geom.Line2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.util.Log;

public class LineShapeCreation
{
  public static void main(String[] args)
  {
    Line2D line = new Line2D.Double(26.0, 8.0, 26.0, -5.0);
    Element e = ItemFactory.createLineShapeElement(null, null, null, line);
    printLine((Line2D) e.getValue());
  }

  private static void printLine(Line2D line)
  {
    Log.debug(line.getP1() + " -> " + line.getP2());
  }

}
