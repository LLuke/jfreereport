/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libloader/
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jfree.resourceloader.ResourceKeyCreationException;

/**
 * Creation-Date: 05.04.2006, 16:02:53
 *
 * @author Thomas Morgner
 */
public class LoaderUtils
{
  private LoaderUtils()
  {
  }

  /**
   * Merges two paths. A path is recognized as an absolute path, if
   * it has an URL-schema definition attached. A parent is recognized as
   * container path (a directory, in the common language), if it ends with
   * a slash.
   *
   * Todo: Introduce escaping using "\" as escape char.
   *
   * @param parent
   * @param child
   * @return
   * @throws ResourceKeyCreationException
   */
  public static String mergePaths(final String parent,
                                  final String child)
          throws ResourceKeyCreationException
  {
    final int childPrefix = child.indexOf("://");
    if (childPrefix > 0) return child;

    String parentResource;
    String parentPrefix;
    final int parentPrefixPos = parent.indexOf("://");
    if (parentPrefixPos > 0)
    {
      parentResource = parent.substring(parentPrefixPos + 3);
      parentPrefix = parent.substring(0, parentPrefixPos + 3);
    }
    else
    {
      parentResource = parent;
      parentPrefix = "";
    }

    final List parentList;
    if (parentResource.endsWith("/"))
    {
      parentList = parseName(parentResource, false);
    }
    else
    {
      parentList = parseName(parentResource, true);
    }
    // construct the full name ...
    parentList.addAll(parseName(child, false));
    // and normalize it by removing all '.' and '..' elements.
    final ArrayList normalizedList = new ArrayList();
    for (int i = 0; i < parentList.size(); i++)
    {
      String o = (String) parentList.get(i);
      if (".".equals(o))
      {
        continue;
      }
      if ("..".equals(o))
      {
        if (normalizedList.isEmpty() == false)
        {
          // remove last element
          normalizedList.remove(normalizedList.size() - 1);
        }
      }
      else
      {
        normalizedList.add(o);
      }
    }

    if (normalizedList.isEmpty())
    {
      throw new ResourceKeyCreationException("Unable to build a valid key.");
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append(parentPrefix);

    for (int i = 0; i < normalizedList.size(); i++)
    {
      String s = (String) normalizedList.get(i);
      if (i > 0)
      {
        buffer.append("/");
      }
      buffer.append(s);
    }
    return buffer.toString();
  }


  /**
   * Parses the given name and returns the name elements as List of Strings.
   *
   * @param name the name, that should be parsed.
   * @return the parsed name.
   */
  private static List parseName(final String name, final boolean skipLast)
  {
    final ArrayList list = new ArrayList();
    final StringTokenizer strTok = new StringTokenizer(name, "/");
    while (strTok.hasMoreElements())
    {
      final String s = (String) strTok.nextElement();
      if (s.length() != 0)
      {
        list.add(s);
      }
    }
    if (skipLast && list.isEmpty() == false)
    {
      list.remove(list.size() - 1);
    }
    return list;
  }


  /**
   * Extracts the file name from the URL.
   *
   * @param url the url.
   * @return the extracted filename.
   */
  public static String getFileName(final String file)
  {
    final int last = file.lastIndexOf("/");
    if (last < 0)
    {
      return file;
    }
    return file.substring(last + 1);
  }

}
