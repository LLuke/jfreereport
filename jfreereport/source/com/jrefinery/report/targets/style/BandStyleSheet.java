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
 * BandStyleSheet.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.style;


public class BandStyleSheet extends ElementStyleSheet
{
  public static final StyleKey PAGEBREAK_BEFORE = StyleKey.getStyleKey("pagebreak-before", Boolean.class);
  public static final StyleKey PAGEBREAK_AFTER = StyleKey.getStyleKey("pagebreak-after", Boolean.class);
  public static final StyleKey DISPLAY_ON_FIRSTPAGE = StyleKey.getStyleKey("display-on-firstpage", Boolean.class);
  public static final StyleKey DISPLAY_ON_LASTPAGE = StyleKey.getStyleKey("display-on-lastpage", Boolean.class);
  public static final StyleKey REPEAT_HEADER = StyleKey.getStyleKey("repeat-header", Boolean.class);

  public BandStyleSheet(String name)
  {
    super(name);
  }
}
