/**
 * Date: Feb 7, 2003
 * Time: 10:01:25 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.layout.LayoutSupport;

public interface ContentFactoryModule
{
  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType  the content type.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public abstract boolean canHandleContent (String contentType);

  /**
   * Creates content for an element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the Content creation.
   */
  public abstract Content createContentForElement(Element e, ElementLayoutInformation bounds, LayoutSupport ot)
      throws ContentCreationException;

}
