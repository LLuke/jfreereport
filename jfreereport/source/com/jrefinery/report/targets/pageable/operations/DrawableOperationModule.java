/**
 * Date: Mar 7, 2003
 * Time: 3:29:37 PM
 *
 * $Id: DrawableOperationModule.java,v 1.5 2003/04/05 18:57:16 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.base.content.DrawableContent;

public class DrawableOperationModule extends OperationModule
{
  public DrawableOperationModule()
  {
    super("drawable/*");
  }

  /**
   * Creates a list of operations for an element.
   *
   * @param e  the element.
   * @param value  the value.
   * @param bounds  the bounds.
   */
  public void createOperations(PhysicalOperationsCollector col, Element e, Content value, Rectangle2D bounds)
  {
    DrawableContent content = (DrawableContent) value.getContentForBounds(bounds);
    if (content == null)
    {
      return;
    }
    col.addOperation (new PhysicalOperation.SetBoundsOperation (bounds));
    col.addOperation (new PhysicalOperation.ProcessDrawableOperation(content.getContent()));
  }
}
