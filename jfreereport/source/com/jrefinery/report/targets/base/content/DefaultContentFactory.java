/**
 * Date: Feb 7, 2003
 * Time: 10:03:06 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.base.ElementLayoutInformation;

import java.util.ArrayList;

public class DefaultContentFactory implements ContentFactory
{
  private ArrayList modules;

  public DefaultContentFactory()
  {
    modules = new ArrayList();
  }

  public void addModule (ContentFactoryModule module)
  {
    if (module == null) throw new NullPointerException();
    modules.add(0, module);
  }

  public void removeModule (ContentFactoryModule module)
  {
    if (module == null) throw new NullPointerException();
    modules.remove(module);
  }

  /**
   * Creates content for an element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the OutputTarget.
   */
  public Content createContentForElement(Element e, ElementLayoutInformation bounds, LayoutSupport ot)
      throws ContentCreationException
  {
    String contentType = e.getContentType();
    for (int i = 0; i < modules.size(); i++)
    {
      ContentFactoryModule cfm = (ContentFactoryModule) modules.get(i);
      if (cfm.canHandleContent(contentType))
      {
        return cfm.createContentForElement(e, bounds, ot);
      }
    }
    throw new ContentCreationException("No module registered for the content-type.");
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType  the content type.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent(String contentType)
  {
    for (int i = 0; i < modules.size(); i++)
    {
      ContentFactoryModule cfm = (ContentFactoryModule) modules.get(i);
      if (cfm.canHandleContent(contentType))
        return true;
    }
    return false;
  }
}
