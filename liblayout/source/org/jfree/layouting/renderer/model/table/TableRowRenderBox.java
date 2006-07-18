/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
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
 * TableRowRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableRowRenderBox.java,v 1.1 2006/07/11 14:03:35 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.RenderBox;

/**
 * A table section box does not much rendering or layouting at all. It represents
 * one of the three possible sections and behaves like any other block box.
 * But (here it comes!) it refuses to be added to anything else than a
 * TableRenderBox (a small check to save me a lot of insanity ..).
 *
 * For a valid layout, the major and minor axes need to be flipped.
 *
 * @author Thomas Morgner
 */
public class TableRowRenderBox extends BlockRenderBox
{
  public TableRowRenderBox(final BoxDefinition boxDefinition)
          throws NormalizationException
  {
    super(boxDefinition);
  }


  public TableRenderBox getTable()
  {
    RenderBox parent = getParent();
    if (parent instanceof TableSectionRenderBox)
    {
      final TableSectionRenderBox tableSectionRenderBox =
              (TableSectionRenderBox) parent;
      return tableSectionRenderBox.getTable();
    }
    return null;
  }

  public TableColumnModel getColumnModel()
  {
    final TableRenderBox table = getTable();
    if (table == null)
    {
      return null;
    }
    return table.getColumnModel();
  }
}
