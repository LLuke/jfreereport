/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * ----------------------------------
 * OperationFactory.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import java.util.ArrayList;

public class OperationFactory
{
  protected static OperationFactory factory;
  private ArrayList modules;

  public static OperationFactory getInstance()
  {
    if (factory == null)
    {
      factory = new OperationFactory();
      factory.registerModule(new ImageOperationModul());
      factory.registerModule(new ShapeOperationModul());
      factory.registerModule(new TextOperationModul());
    }
    return factory;
  }

  public OperationFactory()
  {
    modules = new ArrayList();
  }

  public void registerModule (OperationModul modul)
  {
    modules.add (0, modul);
  }

  public void unregisterModule (OperationModul modul)
  {
    modules.remove(modul);
  }

  public OperationModul getModul (String content)
  {
    for (int i = 0; i < modules.size(); i++)
    {
      OperationModul mod = (OperationModul) modules.get (i);
      if (mod.canHandleContent(content))
      {
        return mod;
      }
    }
    return null;
  }
}
