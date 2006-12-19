/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: ResourceContentToken.java,v 1.3 2006/12/03 18:57:58 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.content.statics;

import org.jfree.layouting.layouter.content.type.GenericType;
import org.jfree.layouting.layouter.content.type.ResourceType;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceException;

public class ResourceContentToken extends StaticToken
        implements ResourceType, GenericType
{
  private Resource content;

  public ResourceContentToken (Resource content)
  {
    if (content == null)
    {
      throw new NullPointerException();
    }
    this.content = content;
  }

  public Object getRaw()
  {
    try
    {
      return content.getResource();
    }
    catch (ResourceException e)
    {
      return null;
    }
  }

  public Resource getContent ()
  {
    return content;
  }
}
