/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * PageSizeFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PageSizeFactory.java,v 1.2 2006/04/17 20:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.keys.page;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

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
