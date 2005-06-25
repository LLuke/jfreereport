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
 * $Id: DefaultContentFactory.java,v 1.10 2005/04/17 21:08:02 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version
 */
package org.jfree.report.content;

import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;

/**
 * The DefaultContentFactory provides a default implementation for output targets. The
 * factory must be configured to support all required content types.
 *
 * @author Thomas Morgner
 */
public class DefaultContentFactory implements ContentFactory
{
  /** The factories registered modules. */
  private ContentFactoryModule[] modules;
  /** The number of modules in this factory. */
  private int size;

  /**
   * Creates an empty DefaultContentFactory.
   */
  public DefaultContentFactory ()
  {
    modules = new ContentFactoryModule[10];
    size = 0;
  }

  /**
   * Adds a content factory module to the factory.
   *
   * @param module the ContentFactoryModule that should be added to the list of available
   *               modules.
   */
  public synchronized void addModule (final ContentFactoryModule module)
  {
    if (module == null)
    {
      throw new NullPointerException();
    }

    ensureCapacity(size);
    modules[size] = module;
    size += 1;
  }

  /**
   * Makes sure, that there is enough space to store at least <code>size</code> elements
   * in this list.
   *
   * @param size the new list capacity.
   */
  private void ensureCapacity (final int size)
  {
    if (modules.length <= size)
    {
      final ContentFactoryModule[] newData =
              new ContentFactoryModule[Math.max(modules.length + 10, size + 1)];
      System.arraycopy(modules, 0, newData, 0, size);
      modules = newData;
    }
  }

  /**
   * Creates content for an element. Delegates the request to the registered modules. If
   * this factory has no registered handler for the element's content type, then a
   * ContentCreationException is thrown.
   *
   * @param e      the element.
   * @param bounds the bounds.
   * @param ot     the output target.
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the OutputTarget or this
   *                                  factory is not able to handle this content type.
   */
  public synchronized Content createContentForElement
          (final Element e, final ElementLayoutInformation bounds, final LayoutSupport ot)
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
    for (int i = 0; i < modules.length; i++)
    {
      final ContentFactoryModule cfm = modules[i];
      if (cfm.canHandleContent(contentType))
      {
        final Content c = cfm.createContentForElement(e, bounds, ot);
        if (c == null)
        {
          throw new NullPointerException("Content returned must never be null.");
        }
        return c;
      }
    }
    throw new ContentCreationException("No module registered for the content-type.");
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise. Delegates the request to the registered modules.
   * Returns false, if this factory is not able to handle this content type.
   *
   * @param contentType the content type.
   * @return <code>true</code> or <code>false</code>.
   */
  public synchronized boolean canHandleContent (final String contentType)
  {
    for (int i = 0; i < size; i++)
    {
      final ContentFactoryModule cfm = modules[i];
      if (cfm.canHandleContent(contentType))
      {
        return true;
      }
    }
    return false;
  }
}
