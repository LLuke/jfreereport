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
 * $Id: OperationFactory.java,v 1.4 2002/12/12 20:20:28 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import java.util.ArrayList;

/**
 * A collection of operation modules.
 *
 * @author Thomas Morgner
 */
public class OperationFactory
{
  /** The factory instance. */
  protected static OperationFactory factory;

  /** Storage for the modules. */
  private ArrayList modules;

  /**
   * Returns a single instance of this class.
   *
   * @return an operation factory.
   */
  public static OperationFactory getInstance()
  {
    if (factory == null)
    {
      factory = new OperationFactory();
      factory.registerModule(new ImageOperationModule());
      factory.registerModule(new ShapeOperationModule());
      factory.registerModule(new TextOperationModule());
    }
    return factory;
  }

  /**
   * Default constructor.
   */
  protected OperationFactory()
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
  public OperationModule getModul (String content)
  {
    for (int i = 0; i < modules.size(); i++)
    {
      OperationModule mod = (OperationModule) modules.get (i);
      if (mod.canHandleContent(content))
      {
        return mod;
      }
    }
    return null;
  }
}
