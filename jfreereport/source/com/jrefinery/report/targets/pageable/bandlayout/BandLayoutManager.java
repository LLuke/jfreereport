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
 * BandLayoutManager.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.bandlayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.targets.pageable.OutputTarget;

import java.awt.geom.Dimension2D;

/**
 * See awt-layoutmanager for the idea :)
 */
public interface BandLayoutManager
{
  public static final StyleKey LAYOUTMANAGER = StyleKey.getStyleKey("layoutmanager", BandLayoutManager.class);

  public Dimension2D preferredLayoutSize(Band b);

  public Dimension2D minimumLayoutSize(Band b);

  public void doLayout(Band b);

  public void setOutputTarget (OutputTarget target);

  public OutputTarget getOutputTarget ();

  // forget everyting that might been cached 
  public void flushLayout();
}
