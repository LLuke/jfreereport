/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * --------------------------
 * DefaultContentFactory.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DefaultContentFactory.java,v 1.4 2004/03/16 15:09:22 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version
 */
package org.jfree.report.content;

import java.util.ArrayList;

import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;

/**
 * The DefaultContentFactory provides a default implementation for output targets,
 * which support all known content types.
 * <p>
 * The implementation provides support for <code>text</code>, <code>image</code>
 * and <code>shape</code> content.
 *
 * @author Thomas Morgner
 */
public class DefaultContentFactory implements ContentFactory
{
  /** a list of all registered modules. */
  private final ArrayList modules;

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
  public void addModule(final ContentFactoryModule module)
  {
    if (module == null)
    {
      throw new NullPointerException();
    }
    modules.add(0, module);
  }

  /**
   * Removes a content factory module from this factory.
   *
   * @param module removes a ContentFactoryModules from the list of available
   * modules.
   */
  public void removeModule(final ContentFactoryModule module)
  {
    if (module == null)
    {
      throw new NullPointerException();
    }
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
  public Content createContentForElement(final Element e, final ElementLayoutInformation bounds,
                                         final LayoutSupport ot)
      throws ContentCreationException
  {
    if (bounds == null)
    {
      throw new NullPointerException("Bounds is null.");
    }
    if (ot == null)
    {
      throw new NullPointerException("LayoutSupport is null.");
    }
    if (e == null)
    {
      throw new NullPointerException("Element is null.");
    }
    final String contentType = e.getContentType();
    for (int i = 0; i < modules.size(); i++)
    {
      final ContentFactoryModule cfm = (ContentFactoryModule) modules.get(i);
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
  public boolean canHandleContent(final String contentType)
  {
    for (int i = 0; i < modules.size(); i++)
    {
      final ContentFactoryModule cfm = (ContentFactoryModule) modules.get(i);
      if (cfm.canHandleContent(contentType))
      {
        return true;
      }
    }
    return false;
  }
}
