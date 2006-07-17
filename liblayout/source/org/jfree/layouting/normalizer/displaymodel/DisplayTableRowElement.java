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
 * DisplayTableRowElement.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DisplayTableRowElement.java,v 1.1 2006/07/11 13:45:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.displaymodel;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * A table row.
 *
 * @author Thomas Morgner
 */
public class DisplayTableRowElement extends DisplayElement
{
  public DisplayTableRowElement(final LayoutContext context)
  {
    super(context);
  }


  public void add(DisplayNode node) throws NormalizationException
  {
//    if (node instanceof DisplayTableCellElement == false)
//    {
//      throw new NormalizationException();
//    }
    addInternal(node);
  }

  protected void signalFinish() throws NormalizationException
  {
    getRootFlow().getContentGenerator().finishedTableRow();
  }

  protected void signalStart() throws NormalizationException
  {
    getRootFlow().getContentGenerator().startedTableRow(this);
  }
}
