/*
 * Copyright (c) 1998, 1999 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */
package com.jrefinery.report.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class FilesystemFilter extends FileFilter implements FilenameFilter
{
  private ArrayList fileext;
  private String descr;
  private boolean accDirs;

  public FilesystemFilter(String fileext, String descr)
  {
    this(fileext, descr, true);
  }

  public FilesystemFilter(String fileext, String descr, boolean accDirs)
  {
    this (new String[]{fileext}, descr, accDirs);
  }

  public FilesystemFilter(String[] fileext, String descr, boolean accDirs)
  {
    this.fileext = new ArrayList();
    for (int i = 0; i < fileext.length; i++)
    {
      this.fileext.add(fileext[i]);
    }
    this.descr = descr;
    this.accDirs = accDirs;
  }

  public boolean accept(File dir, String name)
  {
    File f = new File(dir, name);
    if (f.isDirectory() && acceptsDirectories())
      return true;

    for (int i = 0; i < fileext.size(); i++)
    {
      String ext = (String) fileext.get(i);
      if (StringUtil.endsWithIgnoreCase(name, ext))
        return true;
    }
    return false;
  }

  public boolean accept(File dir)
  {
    if (dir.isDirectory() && acceptsDirectories())
      return true;

    String name = dir.getName();
    for (int i = 0; i < fileext.size(); i++)
    {
      String ext = (String) fileext.get(i);
      if (StringUtil.endsWithIgnoreCase(name, ext))
        return true;
    }
    return false;
  }

  public String getDescription()
  {
    return descr;
  }

  public void acceptDirectories(boolean b)
  {
    accDirs = b;
  }

  public boolean acceptsDirectories()
  {
    return accDirs;
  }

  public void addExtension (String ext)
  {
    fileext.add(ext);
  }
}
