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
 * OperationModul.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.contents.Content;

import java.awt.geom.Rectangle2D;
import java.util.List;

public abstract class OperationModul
{
  private String moduleContentType;
  private boolean generic;

  public OperationModul (String content)
  {
    if (content == null) throw new NullPointerException();
    this.moduleContentType = content;
    this.generic = false;

    if (moduleContentType.endsWith("*"))
    {
      generic = true;
      moduleContentType = moduleContentType.substring(0, moduleContentType.length() - 1);
    }
  }

  public String getModuleContentType()
  {
    return moduleContentType;
  }

  public boolean isGeneric()
  {
    return generic;
  }

  public boolean canHandleContent (String contentType)
  {
    if (isGeneric())
    {
      return (contentType.startsWith(getModuleContentType()));
    }
    else
    {
      return (contentType.equals(getModuleContentType()));
    }
  }

  public abstract List createOperations (Element e, Content value, Rectangle2D bounds);

  public abstract Content createContentForElement(Element e, Rectangle2D bounds, OutputTarget ot)
      throws OutputTargetException;
}
