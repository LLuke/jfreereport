/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * WmfObject.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: WmfObject.java,v 1.2 2004/01/19 18:36:25 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

/**
 * Defines a generic WmfObject.
 */
public interface WmfObject
{
  public static final int OBJ_PEN = 1;
  public static final int OBJ_BRUSH = 2;
  public static final int OBJ_PALETTE = 3;
  public static final int OBJ_FONT = 4;
  public static final int OBJ_REGION = 5;

  public int getType ();
}
