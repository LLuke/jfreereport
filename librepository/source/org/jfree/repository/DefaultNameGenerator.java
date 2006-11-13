/**
 * ===========================================================
 * LibRepository : a free Java content repository access layer
 * ===========================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/librepository/
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * DefaultNameGenerator.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.repository;

public class DefaultNameGenerator implements NameGenerator
{
  private ContentLocation location;

  public DefaultNameGenerator(final ContentLocation location)
  {
    this.location = location;
  }

  /**
   * Generates a new, unique name for storing resources in the output
   * repository. Assuming that proper synchronization has been applied, the
   * generated name will be unique within that repository.
   *
   * @param nameHint a user defined name for that resource.
   * @param mimeType the mime type of the resource to be stored in the
   *                 repository.
   * @return the generated, fully qualified name.
   */
  public String generateName(String nameHint, String mimeType)
          throws ContentIOException
  {
    String name;
    if (nameHint != null)
    {
      name = nameHint;
    }
    else
    {
      name = "file";
    }

    String suffix = getSuffixForType(mimeType, location);
    String firstFileName = name + "." + suffix;
    if (location.exists(firstFileName) == false)
    {
      return firstFileName;
    }
    int counter = 0;
    while (true)
    {
      if (counter < 0) // wraparound should not happen..
      {
        throw new ContentIOException ();
      }

      final String filename = name + counter + "." + suffix;
      if (location.exists(filename) == false)
      {
        return filename;
      }
      counter += 1;
    }
  }

  private String getSuffixForType(final String mimeType,
                                  final ContentLocation location)
  {
    final Repository repository = location.getRepository();
    final MimeRegistry mimeRegistry = repository.getMimeRegistry();
    return mimeRegistry.getSuffix(mimeType);
  }
}
