/**
 * Date: Mar 7, 2003
 * Time: 3:29:37 PM
 *
 * $Id: DrawableOperationModule.java,v 1.3 2003/03/09 17:48:58 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.operations;

import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.util.Log;
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
   *
   * @return the list of operations.
   */
  public List createOperations(Element e, Content value, Rectangle2D bounds)
  {
    DrawableContent content = (DrawableContent) value.getContentForBounds(bounds);
    if (content == null)
    {
      return new ArrayList();
    }

    ArrayList array = new ArrayList ();
    array.add (new PhysicalOperation.SetBoundsOperation (bounds));
    array.add (new PhysicalOperation.ProcessDrawableOperation(content.getContent()));
    return array;
  }
}
