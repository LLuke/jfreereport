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
 * ---------------------
 * OperationFactory.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: OperationFactory.java,v 1.12 2003/04/09 15:52:52 mungady Exp $
 *
 * Changes
 * -------
 * 02-Dec-2002 : Initial version
 * 07-Feb-2003 : OperationFactory is no longer static, now acts as hub for all
 *               registered modules
 */
package com.jrefinery.report.targets.pageable.operations;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.pageable.OutputTargetException;

/**
 * The OperationFactory is used to transform content into OutputTarget operations.
 *
 * @see com.jrefinery.report.targets.pageable.OutputTarget
 * @see com.jrefinery.report.targets.base.content.Content
 *
 * @author Thomas Morgner
 */
public class OperationFactory
{
  /** Storage for the modules. */
  private ArrayList modules;

  /**
   * Default constructor.
   */
  public OperationFactory()
  {
    modules = new ArrayList();
  }

  /**
   * Registered a module with the factory.
   *
   * @param module  the module.
   */
  public void registerModule(OperationModule module)
  {
    modules.add(0, module);
  }

  /**
   * Deregisters a module with the factory.
   *
   * @param module  the module.
   */
  public void unregisterModule(OperationModule module)
  {
    modules.remove(module);
  }

  /**
   * Returns the first module in the factory that can handle the specified content.
   * ToDo: Add better support for specific and generic content handler.
   * Specific handler should be always preferred to more generic handlers.
   *
   * @param content  the content type.
   *
   * @return the module or null if no handler is registered for that content-type.
   * @throws OutputTargetException if no module was found for the given content.
   */
  public OperationModule getModule(String content)
      throws OutputTargetException
  {
    for (int i = 0; i < modules.size(); i++)
    {
      OperationModule mod = (OperationModule) modules.get(i);
      if (mod.canHandleContent(content))
      {
        return mod;
      }
    }
    throw new OutputTargetException("No operation module for " + content);
  }

  /**
   * Tests, whether this implementation would be able to handle the given content
   * type.
   *
   * @param contentType the to be tested content type.
   * @return true, if this kind of content can be handled, false otherwise.
   */
  public boolean canHandleContent(String contentType)
  {
    for (int i = 0; i < modules.size(); i++)
    {
      OperationModule mod = (OperationModule) modules.get(i);
      if (mod.canHandleContent(contentType))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates the required Operations to output the content on an OutputTarget.
   *
   * @param e the element that contained the raw data for the content.
   * @param value the content that should be printed.
   * @param bounds the content bounds.
   * @param col the operations collector for the ops.
   * @throws OutputTargetException if this factory is not able to handle that content.
   */
  public void createOperations(PhysicalOperationsCollector col, Element e, Content value,
                               Rectangle2D bounds)
      throws OutputTargetException
  {
    String contentType = e.getContentType();
    for (int i = 0; i < modules.size(); i++)
    {
      OperationModule mod = (OperationModule) modules.get(i);
      if (mod.canHandleContent(contentType))
      {
        // todo element alignment !
        mod.createOperations(col, e, value, bounds);
        return;
      }
    }
    throw new OutputTargetException("No operation module for " + contentType);
  }

}
