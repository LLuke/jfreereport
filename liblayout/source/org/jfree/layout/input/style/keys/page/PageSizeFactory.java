/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * PageSizeFactory.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.page;

import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This will be replaced by a 'media-names' implementation according to
 * ftp://ftp.pwg.org/pub/pwg/candidates/cs-pwgmsn10-20020226-5101.1.pdf
 *
 * @author Thomas Morgner
 */
public class PageSizeFactory
{
  private static PageSizeFactory factory;

  public static synchronized PageSizeFactory getInstance ()
  {
    if (factory == null)
    {
      factory = new PageSizeFactory();
      factory.registerKnownMedias();
    }
    return factory;
  }

  private HashMap knownPageSizes;

  private PageSizeFactory()
  {
    knownPageSizes = new HashMap();
  }

  public PageSize getPageSizeByName (String name)
  {
    return (PageSize) knownPageSizes.get(name.toLowerCase());
  }

  public String[] getPageSizeNames ()
  {
    return (String[]) knownPageSizes.keySet().toArray(new String[knownPageSizes.size()]);
  }

  private void registerKnownMedias ()
  {
    Field[] fields = PageSize.class.getFields();
    for (int i = 0; i < fields.length; i++)
    {
      try
      {
        Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers()) == false ||
            Modifier.isStatic(f.getModifiers()) == false)
        {
          continue;
        }
        final Object o = f.get(this);
        if (o instanceof PageSize == false)
        {
          // Log.debug ("Is no valid pageformat definition");
          continue;
        }
        final PageSize pageSize = (PageSize) o;
        knownPageSizes.put (f.getName().toLowerCase(), pageSize);
      }
      catch (IllegalAccessException aie)
      {
        // Log.debug ("There is no pageformat " + name + " accessible.");
      }
    }
  }

}
