/**
 * ===========================================================
 * LibRepository : a free Java content repository access layer
 * ===========================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/librepository/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * RepositoryUtilities.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Creation-Date: 02.12.2006, 13:38:01
 *
 * @author Thomas Morgner
 */
public class RepositoryUtilities
{
  private RepositoryUtilities()
  {

  }

  public static ContentEntity getEntity (Repository repository, String[] name)
      throws ContentIOException
  {
    if (name.length == 0)
    {
      return repository.getRoot();
    }

    ContentLocation node = repository.getRoot();
    for (int i = 0; i < name.length - 1; i++)
    {
      final String nameItem = name[i];
      final ContentEntity entry = node.getEntry(nameItem);
      if (entry instanceof ContentLocation == false)
      {
        // its ok, if we hit the last item
        throw new ContentIOException("No such item.");
      }
      node = (ContentLocation) entry;
    }
    return node.getEntry(name[name.length - 1]);
  }

  public static ContentItem createItem (Repository repository, String[] name)
      throws ContentIOException
  {
    if (name.length == 0)
    {
      throw new IllegalArgumentException("Empty name not permitted.");
    }

    ContentLocation node = repository.getRoot();
    for (int i = 0; i < name.length - 1; i++)
    {
      final String nameItem = name[i];
      if (node.exists(nameItem) == false)
      {
        // create it
        node = node.createLocation(nameItem);
      }
      else
      {
        final ContentEntity entry = node.getEntry(nameItem);
        if (entry instanceof ContentLocation == false)
        {
          // its ok, if we hit the last item
          throw new ContentIOException("No such item.");
        }
        node = (ContentLocation) entry;
      }
    }
    return node.createItem(name[name.length - 1]);
  }

  public static ContentLocation createLocation (Repository repository, String[] name)
      throws ContentIOException
  {
    if (name.length == 0)
    {
      throw new IllegalArgumentException("Empty name not permitted.");
    }

    ContentLocation node = repository.getRoot();
    for (int i = 0; i < name.length - 1; i++)
    {
      final String nameItem = name[i];
      if (node.exists(nameItem) == false)
      {
        // create it
        node = node.createLocation(nameItem);
      }
      else
      {
        final ContentEntity entry = node.getEntry(nameItem);
        if (entry instanceof ContentLocation == false)
        {
          // its ok, if we hit the last item
          throw new ContentIOException("No such item.");
        }
        node = (ContentLocation) entry;
      }
    }
    return node.createLocation(name[name.length - 1]);
  }

  public static String[] split (String name, String separator)
  {
    final StringTokenizer strtok = new StringTokenizer(name, separator, false);
    final int tokenCount = strtok.countTokens();
    final String[] retval = new String[tokenCount];
    int i = 0;
    while (strtok.hasMoreTokens())
    {
      retval[i] = strtok.nextToken();
      i += 1;
    }
    return retval;
  }

  public static String[] buildNameArray (ContentEntity entity)
  {
    final LinkedList collector = new LinkedList();
    while (entity != null)
    {
      ContentLocation parent = entity.getParent();
      if (parent != null)
      {
        // this filters out the root ..
        collector.add(0, entity.getName());
      }
      entity = parent;
    }
    return (String[]) collector.toArray(new String[collector.size()]);
  }

  public static String buildName (ContentEntity entity, String separator)
  {
    final ArrayList collector = new ArrayList();
    while (entity != null)
    {
      ContentLocation parent = entity.getParent();
      if (parent != null)
      {
        // this filters out the root ..
        collector.add(entity.getName());
      }
      entity = parent;
    }

    final StringBuffer builder = new StringBuffer();
    final int maxIdx = collector.size() - 1;
    for (int i = maxIdx; i >= 0; i--)
    {
      final String s = (String) collector.get(i);
      if (i != maxIdx)
      {
        builder.append("/");
      }
      builder.append(s);
    }
    return builder.toString();
  }
}
