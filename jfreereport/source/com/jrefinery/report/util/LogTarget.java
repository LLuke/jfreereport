/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * LogTarget.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 11-May-2002 : Initial version
 *
 */
package com.jrefinery.report.util;

/**
 * Use the LogTarget to redirect all logging messages to a suitable consumer.
 */
public interface LogTarget
{
  /**
   * Loglevel ERROR
   */
  public static final int ERROR = 0;

  /**
   * Loglevel WARN
   */
  public static final int WARN = 1;

  /**
   * Loglevel INFO
   */
  public static final int INFO = 2;

  /**
   * Loglevel DEBUG
   */
  public static final int DEBUG = 3;

  public static final String[] levels =
          {
            "ERROR: ",
            "WARN:  ",
            "INFO:  ",
            "DEBUG: "
          };

  public void log (int level, String message);

  public void log (int level, String message, Exception e);

  public void debug (String message);

  public void debug (String message, Exception e);

  public void info (String message);

  public void info (String message, Exception e);

  public void warn (String message);

  public void warn (String message, Exception e);

  public void error (String message);

  public void error (String message, Exception e);
}
