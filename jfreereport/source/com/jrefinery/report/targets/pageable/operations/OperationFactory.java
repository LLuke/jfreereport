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
 * ---------------------
 * OperationFactory.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: OperationFactory.java,v 1.8 2003/02/09 23:09:15 taqua Exp $
 *
 * Changes
 * -------
 * 02-Dec-2002 : Initial version
 * 07-Feb-2003 : OperationFactory is no longer static, now acts as hub for all
 *               registered modules
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.Element;
import com.jrefinery.report.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Rectangle2D;

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
  public void registerModule (OperationModule module)
  {
    modules.add (0, module);
  }

  /**
   * Deregisters a module with the factory.
   *
   * @param module  the module.
   */
  public void unregisterModule (OperationModule module)
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
   */
  public OperationModule getModule (String content)
    throws OutputTargetException
  {
    for (int i = 0; i < modules.size(); i++)
    {
      OperationModule mod = (OperationModule) modules.get (i);
      if (mod.canHandleContent(content))
      {
        return mod;
      }
    }
    throw new OutputTargetException ("No operation module for " + content);
  }

  public boolean canHandleContent (String contentType)
  {
    for (int i = 0; i < modules.size(); i++)
    {
      OperationModule mod = (OperationModule) modules.get (i);
      if (mod.canHandleContent(contentType))
      {
        return true;
      }
    }
    return false;
  }

  public List createOperations (Element e, Content value, Rectangle2D bounds)
    throws OutputTargetException
  {
    String contentType = e.getContentType();
    for (int i = 0; i < modules.size(); i++)
    {
      OperationModule mod = (OperationModule) modules.get (i);
      if (mod.canHandleContent(contentType))
      {
        // todo element alignment !
        return mod.createOperations(e, value, bounds);
      }
    }
    throw new OutputTargetException ("No operation module for " + contentType);
  }

}
