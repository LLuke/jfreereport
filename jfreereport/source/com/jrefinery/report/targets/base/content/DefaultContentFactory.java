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
 * ------------------------
 * DefaultContentFactory.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultContentFactory.java,v 1.1 2003/02/07 22:40:39 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.base.ElementLayoutInformation;

import java.util.ArrayList;

/**
 *
 */
public class DefaultContentFactory implements ContentFactory
{
  /** a list of all registered modules. */
  private ArrayList modules;

  /**
   * Creates an empty DefaultContentFactory.
   */
  public DefaultContentFactory()
  {
    modules = new ArrayList();
  }

  /**
   * Adds a content factory module to the factory.
   *
   * @param module the ContentFactoryModule that should be added to the list of
   * available modules.
   */
  public void addModule (ContentFactoryModule module)
  {
    if (module == null) throw new NullPointerException();
    modules.add(0, module);
  }

  /**
   * Removes a content factory module from this factory.
   *
   * @param module removes a ContentFactoryModules from the list of available
   * modules.
   */
  public void removeModule (ContentFactoryModule module)
  {
    if (module == null) throw new NullPointerException();
    modules.remove(module);
  }

  /**
   * Creates content for an element. Delegates the request to the registered
   * modules. If this factory has no registered handler for the element's content
   * type, then a ContentCreationException is thrown.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the OutputTarget or
   * this factory is not able to handle this content type.
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
   * <code>false</code> otherwise. Delegates the request to the registered modules.
   * Returns false, if this factory is not able to handle this content type.
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
