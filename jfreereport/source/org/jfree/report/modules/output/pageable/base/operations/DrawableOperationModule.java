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
 * --------------------------------
 * PhysicalOperationsCollector.java
 * --------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DrawableOperationModule.java,v 1.9 2003/06/29 16:59:29 taqua Exp $
 *
 * Changes
 * -------
 * 04.04.2003 : Initial version
 */
package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.content.Content;
import org.jfree.report.content.DrawableContent;

/**
 * A DrawableOperation module.
 *
 * @author Thomas Morgner.
 */
public class DrawableOperationModule extends OperationModule
{
  /**
   * Default constructor.
   */
  public DrawableOperationModule()
  {
    super("drawable/*");
  }

  /**
   * Creates a list of operations for an element.
   *
   * @param col  the operations collector.
   * @param e  the element.
   * @param value  the value.
   * @param bounds  the bounds.
   */
  public void createOperations(final PhysicalOperationsCollector col, final Element e, final Content value,
                               final Rectangle2D bounds)
  {
    final DrawableContent content = (DrawableContent) value.getContentForBounds(bounds);
    if (content == null)
    {
      return;
    }
    col.addOperation(new PhysicalOperation.SetBoundsOperation(bounds));
    col.addOperation(new PhysicalOperation.ProcessDrawableOperation(content.getContent()));
  }
}
