/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * FilesystemFilter.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: FilesystemFilter.java,v 1.3 2003/08/25 14:29:29 taqua Exp $
 *
 * Changes
 * -------
 * 30-Jan-2003 : Initial version
 */
package org.jfree.report.modules.gui.base.components;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter;

import org.jfree.report.util.StringUtil;

/**
 * A generic filesystem filter which implements FilenameFilter and the
 * Swing FileFilter. Multiple extensions can be registered for a single filetype.
 *
 * @author Thomas Morgner
 */
public class FilesystemFilter extends FileFilter implements FilenameFilter
{
  /** File extensions. */
  private final ArrayList fileext;

  /** A description. */
  private final String descr;

  /** Accept directories. */
  private boolean accDirs;

  /**
   * Creates a filesystem filter for the given extension with the description
   * supplied in <code>descr</code>. Directories are accepted by default.
   *
   * @param fileext the file extension that should be accepted
   * @param descr the description for this filetype
   */
  public FilesystemFilter(final String fileext, final String descr)
  {
    this(fileext, descr, true);
  }

  /**
   * Creates a filesystem filter for the given extension with the description
   * supplied in <code>descr</code>. Directories are accepted if <code>accDirs</code>
   * is set to <code>true</code>.
   *
   * @param fileext the file extension that should be accepted
   * @param descr the description for this filetype
   * @param accDirs true, if directories should be acceptable for this filter.
   */
  public FilesystemFilter(final String fileext, final String descr, final boolean accDirs)
  {
    this(new String[]{fileext}, descr, accDirs);
  }

  /**
   * Creates a filesystem filter for the given extensions with the description
   * supplied in <code>descr</code>. Directories are accepted if <code>accDirs</code>
   * is set to <code>true</code>.
   *
   * @param fileext the file extensions that should be accepted
   * @param descr the description for this filetype
   * @param accDirs true, if directories should be acceptable for this filter.
   */
  public FilesystemFilter(final String[] fileext, final String descr, final boolean accDirs)
  {
    this.fileext = new ArrayList();
    for (int i = 0; i < fileext.length; i++)
    {
      this.fileext.add(fileext[i]);
    }
    this.descr = descr;
    this.accDirs = accDirs;
  }

  /**
   * Tests if a specified file should be included in a file list.
   *
   * @param   dir    the directory in which the file was found.
   * @param   name   the name of the file.
   * @return  <code>true</code> if and only if the name should be
   * included in the file list; <code>false</code> otherwise.
   */
  public boolean accept(final File dir, final String name)
  {
    final File f = new File(dir, name);
    if (f.isDirectory() && isAcceptDirectories())
    {
      return true;
    }
    for (int i = 0; i < fileext.size(); i++)
    {
      final String ext = (String) fileext.get(i);
      if (StringUtil.endsWithIgnoreCase(name, ext))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Whether the given file is accepted by this filter.
   *
   * @param dir the file that should be checked.
   * @return true, if the file should be accepted for this filter, false otherwise.
   */
  public boolean accept(final File dir)
  {
    if (dir.isDirectory() && isAcceptDirectories())
    {
      return true;
    }
    final String name = dir.getName();
    for (int i = 0; i < fileext.size(); i++)
    {
      final String ext = (String) fileext.get(i);
      if (StringUtil.endsWithIgnoreCase(name, ext))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * The description of this filter. For example: "JPG and GIF Images"
   * @see javax.swing.filechooser.FileView#getName
   * @return the description for this filter.
   */
  public String getDescription()
  {
    return descr;
  }

  /**
   * Sets whether this filter accepts directories.
   *
   * @param b set to <code>true</code> to accept directories, false otherwise
   */
  public void setAcceptDirectories(final boolean b)
  {
    accDirs = b;
  }

  /**
   * checks, whether this filter should accept directories.
   *
   * @return true, if this filter accepts directories, false otherwise
   */
  private boolean isAcceptDirectories()
  {
    return accDirs;
  }

  /**
   * Add this extension to the filter.
   *
   * @param ext the extension that should be added to this filter.
   */
  public void addExtension(final String ext)
  {
    fileext.add(ext);
  }
}
